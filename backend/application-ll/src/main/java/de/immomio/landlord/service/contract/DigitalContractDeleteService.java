package de.immomio.landlord.service.contract;

import de.immomio.cloud.amazon.s3.LandlordS3FileManager;
import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.landlord.entity.contract.DigitalContractApiUser;
import de.immomio.data.shared.bean.contract.DigitalContractSignerData;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState;
import de.immomio.docusign.service.DocuSignService;
import de.immomio.landlord.service.contract.history.DigitalContractHistoryService;
import de.immomio.landlord.service.contract.history.DigitalContractSignerHistoryService;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.config.QueueConfigUtils;
import de.immomio.messaging.container.mail.LandlordMailBrokerContainer;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import de.immomio.model.repository.shared.contract.DigitalContractApiUserRepository;
import de.immomio.model.repository.shared.contract.DigitalContractRepository;
import de.immomio.model.repository.shared.contract.signer.DigitalContractSignerRepository;
import de.immomio.service.shared.EmailModelProvider;
import de.immomio.service.shared.contract.AbstractDigitalContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DigitalContractDeleteService extends AbstractDigitalContractService {

    private static final String DIGITAL_CONTRACT_DELETED_LL_SUBJECT =
            "landlord.contract.deleted.subject";

    private final DigitalContractHistoryService digitalContractHistoryService;
    private final DigitalContractRepository digitalContractRepository;
    private final DigitalContractSignerRepository digitalContractSignerRepository;
    private final DigitalContractSignerHistoryService digitalContractSignerHistoryService;
    private final DigitalContractApiUserRepository digitalContractApiUserRepository;
    private final DocuSignService docuSignService;
    private final LandlordS3FileManager s3FileManager;
    private final RabbitTemplate rabbitTemplate;
    private final EmailModelProvider emailModelProvider;


    @Autowired
    public DigitalContractDeleteService(
            DigitalContractHistoryService digitalContractHistoryService,
            DigitalContractRepository digitalContractRepository,
            DigitalContractSignerRepository digitalContractSignerRepository,
            DigitalContractSignerHistoryService digitalContractSignerHistoryService,
            DigitalContractApiUserRepository digitalContractApiUserRepository,
            DocuSignService docuSignService,
            LandlordS3FileManager s3FileManager,
            RabbitTemplate rabbitTemplate,
            EmailModelProvider emailModelProvider
    ) {
        this.digitalContractHistoryService = digitalContractHistoryService;
        this.digitalContractRepository = digitalContractRepository;
        this.digitalContractSignerRepository = digitalContractSignerRepository;
        this.digitalContractSignerHistoryService = digitalContractSignerHistoryService;
        this.digitalContractApiUserRepository = digitalContractApiUserRepository;
        this.docuSignService = docuSignService;
        this.s3FileManager = s3FileManager;
        this.rabbitTemplate = rabbitTemplate;
        this.emailModelProvider = emailModelProvider;
    }

    public void deleteContract(final UUID internalContractId) {
        log.info("Deleting contract with internalContractId '{}'", internalContractId);
        DigitalContract digitalContract = digitalContractRepository.findByInternalContractId(internalContractId);
        voidEnvelope(digitalContract);
        deleteContractDocuments(digitalContract);
        digitalContractHistoryService.historyContractDeleted(digitalContract);
        notifiyLLAboutDeletedContract(digitalContract);
        deletePersonalDataOfSigners(digitalContract);
    }

    private void voidEnvelope(DigitalContract digitalContract) {
        try {
            Optional<DigitalContractSigner> contractSigner = digitalContract.getSigners()
                    .stream()
                    .filter(signer -> signer.getCurrentState().getSignerState().getLevel() >= DigitalContractSignerHistoryState.DOCUSIGN_SIGNED.getLevel())
                    .findAny();
            if (contractSigner.isEmpty() && digitalContract.getDocuSignEnvelopeId() != null) {
                DigitalContractApiUser apiUser = digitalContractApiUserRepository.findByCustomer(digitalContract.getCustomer()).orElseThrow();
                docuSignService.voidEnvelope(digitalContract.getDocuSignEnvelopeId(), apiUser.getDocuSignApiUserId());
            }
        } catch (Exception ex) {
            log.error("Error voiding the contract. ", ex);
        }
    }

    private void deleteContractDocuments(DigitalContract digitalContract) {
        digitalContract.getDocumentFiles().forEach(s3File -> {
            try {
                s3FileManager.deleteFile(
                        s3File.getType(),
                        s3File.getIdentifier(),
                        s3File.getExtension(),
                        s3FileManager.getBucket(s3File.getUrl())
                );
            } catch (S3FileManagerException e) {
                e.printStackTrace();
            }
        });
    }

    private void deletePersonalDataOfSigners(final DigitalContract digitalContract) {
        List<DigitalContractSigner> signers = digitalContract.getSigners();
        signers.forEach(signer -> {
            signer.getData().setAddress(null);
            signer.getData().setDateOfBirth(null);
            signer.getData().setEmail(null);
            signer.getData().setGender(null);
            digitalContractSignerHistoryService.updateSignerHistory(DigitalContractSignerHistoryState.CONTRACT_DELETED, signer);
        });
        digitalContractSignerRepository.saveAll(signers);
    }

    private void notifiyLLAboutDeletedContract(final DigitalContract digitalContract) {
        List<DigitalContractSigner> signers = digitalContract.getSigners();
        List<DigitalContractSignerData> tenantsData = signers.stream()
                .filter(signer -> signer.getType() == DigitalContractSignerType.TENANT)
                .map(DigitalContractSigner::getData)
                .collect(Collectors.toList());
        signers.stream()
                .filter(signer -> signer.getType() == DigitalContractSignerType.LANDLORD)
                .forEach(signer -> sendContractDeletedLL(digitalContract, signer, tenantsData));
    }

    private void sendContractDeletedLL(final DigitalContract digitalContract, final DigitalContractSigner signer, List<DigitalContractSignerData> tenantsData) {
        Map<String, Object> data = new HashMap<>();
        DigitalContractSignerData signerData = signer.getData();
        String signerEmail = signerData.getEmail();
        if (StringUtils.isEmpty(signerEmail)) {
            log.error("Can not send send contract deleted mail to signer with internalId: {}, because the email field is empty.", signerData.getInternalSignerId());

            return;
        }

        emailModelProvider.appendFormattedDate(digitalContract.getCreated(), data, ModelParams.MODEL_APPOINTMENT_DATE_FORMATTED);

        data.put(ModelParams.MODEL_CONTRACT_SIGNER_DATA, signerData);
        data.put(ModelParams.MODEL_CONTRACT_PROPERTY_DATA, digitalContract.getPropertyData());
        data.put(ModelParams.MODEL_CONTRACT_TENANTS_DATA, tenantsData);
        LandlordMailBrokerContainer mailBrokerContainer = new LandlordMailBrokerContainer(
                signerEmail,
                MailTemplate.DIGITAL_CONTRACT_DELETED_LL,
                DIGITAL_CONTRACT_DELETED_LL_SUBJECT,
                data);

        sendLandlordEmailMessage(mailBrokerContainer);
    }

    private void sendLandlordEmailMessage(
            LandlordMailBrokerContainer messageBean
    ) {
        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(QueueConfigUtils.MailLandlordBrokerConfig.EXCHANGE_NAME, QueueConfigUtils.MailLandlordBrokerConfig.ROUTING_KEY, messageBean);
    }

}
