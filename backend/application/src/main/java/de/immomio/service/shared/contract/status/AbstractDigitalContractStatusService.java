package de.immomio.service.shared.contract.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.history.DigitalContractHistoryState;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState;
import de.immomio.docusign.service.DocuSignService;
import de.immomio.docusign.service.DocuSignSignerService;
import de.immomio.docusign.service.DocuSignStatusService;
import de.immomio.docusign.service.beans.DigitalContractEnvelopeStatusBean;
import de.immomio.docusign.service.beans.DigitalContractSignerStatusBean;
import de.immomio.docusign.service.beans.DocuSignEnvelopeStatusType;
import de.immomio.docusign.service.beans.DocuSignRecipientStatusType;
import de.immomio.model.repository.core.shared.contract.BaseDigitalContractRepository;
import de.immomio.model.repository.core.shared.contract.signer.BaseDigitalContractSignerRepository;
import de.immomio.service.shared.contract.AbstractDigitalContractHistoryService;
import de.immomio.service.shared.contract.AbstractDigitalContractService;
import de.immomio.service.shared.contract.AbstractDigitalContractSignerHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static de.immomio.service.shared.contract.DigitalContractFilenameHelper.getContractFilenameWithoutEnding;

/**
 * @author Niklas Lindemann
 */
@Slf4j
public class AbstractDigitalContractStatusService
        <CHS extends AbstractDigitalContractHistoryService<?, DCR>,
                DCR extends BaseDigitalContractRepository,
                CSR extends BaseDigitalContractSignerRepository,
                DS extends AbstractDigitalContractService,
                SHS extends AbstractDigitalContractSignerHistoryService<?, CSR>,
                SNS extends AbstractDigitalContractStatusNotificationService> {

    private final DCR contractRepository;
    private final CSR contractSignerRepository;
    private final CHS contractHistoryService;
    private final SHS crawlerContractSignerHistoryService;
    private final DocuSignService docuSignService;
    private final DocuSignSignerService docuSignSignerService;
    private final DocuSignStatusService docuSignStatusService;
    private final SNS contractNotificationService;
    private final DS digitalContractService;

    private static final Set<DigitalContractHistoryState> DOCUSIGN_IN_PROGRESS_STATES = Set.of(
            DigitalContractHistoryState.DOCUSIGN_CREATED,
            DigitalContractHistoryState.DOCUSIGN_UPDATED,
            DigitalContractHistoryState.DOCUSIGN_SENT,
            DigitalContractHistoryState.DOCUSIGN_UPDATED_AFTER_INTERRUPTED,
            DigitalContractHistoryState.DOCUSIGN_SENT_AFTER_INTERRUPTED
    );

    private static final Set<DigitalContractHistoryState> STATES_GREATER_EQUAL_SENT = Set.of(
            DigitalContractHistoryState.DOCUSIGN_SENT,
            DigitalContractHistoryState.DOCUSIGN_COMPLETED,
            DigitalContractHistoryState.INTERNAL_CANCELED,
            DigitalContractHistoryState.INTERNAL_INTERRUPTED,
            DigitalContractHistoryState.DOCUSIGN_UPDATED_AFTER_INTERRUPTED,
            DigitalContractHistoryState.DOCUSIGN_SENT_AFTER_INTERRUPTED,
            DigitalContractHistoryState.CONTRACT_DELETED
    );

    public AbstractDigitalContractStatusService(
            DCR contractRepository,
            CSR contractSignerRepository,
            CHS contractHistoryService,
            SHS crawlerContractSignerHistoryService,
            DocuSignService docuSignService,
            DocuSignSignerService docuSignSignerService,
            DocuSignStatusService docuSignStatusService,
            SNS contractNotificationService,
            DS digitalContractService) {
        this.contractRepository = contractRepository;
        this.contractSignerRepository = contractSignerRepository;
        this.contractHistoryService = contractHistoryService;
        this.crawlerContractSignerHistoryService = crawlerContractSignerHistoryService;
        this.docuSignService = docuSignService;
        this.docuSignSignerService = docuSignSignerService;
        this.docuSignStatusService = docuSignStatusService;
        this.contractNotificationService = contractNotificationService;
        this.digitalContractService = digitalContractService;
    }

    public void handleEnvelopeStatusChanges() {
        List<DigitalContract> contractsByState =
                contractRepository.findByCurrentStateIn(DOCUSIGN_IN_PROGRESS_STATES);

        if (contractsByState == null || contractsByState.isEmpty()) {
            return;
        }

        List<UUID> envelopeIds = contractsByState
                .stream()
                .map(DigitalContract::getDocuSignEnvelopeId)
                .collect(Collectors.toList());

        List<DigitalContractEnvelopeStatusBean> envelopeStatusChanges =
                docuSignStatusService.getEnvelopeStatusChanges(envelopeIds);

        envelopeStatusChanges.forEach(statusBean -> {
            processEnvelopeStatus(statusBean.getEnvelopeId(), statusBean.getStatus());
            processSignerStatus(statusBean.getSigners());
        });
    }

    public void updateCompletedContractsWithMissingSignedDocuments() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -60);
        Date dateBefore30Days = cal.getTime();
        Page<DigitalContract> contractsByState =
                contractRepository.customFindByCurrentStateAndSignedDocumentArchiveFileIsNull(dateBefore30Days, PageRequest.of(0, 10, Sort.unsorted()));
        if (!contractsByState.getContent().isEmpty()) {
            contractsByState.getContent().forEach(this::loadAndSaveSignedDocuments);
        }
    }

    public void triggerUpdateContractStatus(UUID envelopeId) {
        List<DigitalContractEnvelopeStatusBean> envelopeStatusChanges =
                docuSignStatusService.getEnvelopeStatusChanges(List.of(envelopeId));

        envelopeStatusChanges.forEach(statusBean -> {
            processEnvelopeStatus(statusBean.getEnvelopeId(), statusBean.getStatus());
            processSignerStatus(statusBean.getSigners());
        });
    }

    private void processEnvelopeStatus(UUID envelopeId, DocuSignEnvelopeStatusType status) {
        DigitalContract digitalContract =
                contractRepository.customFindByDocuSignEnvelopeId(envelopeId);
        // according to different stages on our side but only one docusign
        if (digitalContract != null) {
            log.info(digitalContract.getCurrentState() + ", new state=" + status);
            if (status != null) {
                switch (status) {
                    case ENVELOPE_STATUS_SENT:
                        if (digitalContract.getCurrentState() == DigitalContractHistoryState.DOCUSIGN_UPDATED_AFTER_INTERRUPTED) {
                            handleMailSendingAfterInterrupted(digitalContract);
                        } else if (!STATES_GREATER_EQUAL_SENT.contains(digitalContract.getCurrentState())) {
                            List<DigitalContractSigner> signers = contractSignerRepository.findByDigitalContract(digitalContract);
                            docuSignSignerService.addAdditionalDocuments(digitalContract, signers, envelopeId.toString());
                            contractSignerRepository.saveAll(signers);
                        }

                        if (digitalContract.getCurrentState() == DigitalContractHistoryState.DOCUSIGN_UPDATED ||
                                !contractHistoryService.contractHistoryContainsState(
                                        digitalContract,
                                        DigitalContractHistoryState.DOCUSIGN_SENT)
                        ) {
                            contractHistoryService.historyDocuSignSent(digitalContract);
                        }
                        break;
                    case ENVELOPE_STATUS_COMPLETED:
                        if (!contractHistoryService.contractHistoryContainsState(
                                digitalContract,
                                DigitalContractHistoryState.DOCUSIGN_COMPLETED)
                        ) {
                            contractHistoryService.historyDocuSignCompleted(digitalContract);
                            loadAndSaveSignedDocuments(digitalContract);
                            notifyAllSignersContractFinished(digitalContract);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void processSignerStatus(List<DigitalContractSignerStatusBean> signerStatusBeans) {
        signerStatusBeans.forEach(statusBean -> {
            Long recipientId = statusBean.getRecipientId();
            DocuSignRecipientStatusType status = statusBean.getStatus();
            Optional<DigitalContractSigner> dcSigner = contractSignerRepository.findById(recipientId);
            dcSigner.ifPresent(digitalContractSigner -> {
                crawlerContractSignerHistoryService.updateDocusignStatus(digitalContractSigner, status);
                if (status == DocuSignRecipientStatusType.SIGNER_STATUS_SENT) {
                    handleMailSending(digitalContractSigner);
                }
            });
        });
    }

    private void handleMailSendingAfterInterrupted(DigitalContract contract) {
        List<DigitalContractSigner> signers = contractSignerRepository.findByDigitalContract(contract);
        // TODO: 31.03.20 send in correct order
//        signers.forEach(this::sendMailToSigner);
        contractHistoryService.historyDocusignSentAfterInterrupted(contract);
    }

    private void handleMailSending(DigitalContractSigner digitalContractSigner) {
        DigitalContractSignerHistoryState currentHistoryState = digitalContractSigner.getCurrentState().getSignerState();
        if (currentHistoryState != null && (currentHistoryState.getLevel() >= DigitalContractSignerHistoryState.INTERNAL_MAIL_SENT.getLevel())) {
            log.info("Signer already got a mail: {}", digitalContractSigner.getId());
        } else {
            log.info("Sending mail to {}: {}", digitalContractSigner.getType(), digitalContractSigner.getId());
            sendMailToSigner(digitalContractSigner);
            crawlerContractSignerHistoryService.signerHistoryMailSent(digitalContractSigner);
        }
    }

    // call this to send the mail, don't forget to start broker and mailsender
    private void sendMailToSigner(DigitalContractSigner digitalContractSigner) {
        try {
            if (digitalContractSigner.getType() == DigitalContractSignerType.TENANT) {
                contractNotificationService.sendContractSignPS(digitalContractSigner);
            } else {
                contractNotificationService.sendContractSignLL(digitalContractSigner);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void notifyAllSignersContractFinished(DigitalContract digitalContract) {
        contractSignerRepository.findByDigitalContract(digitalContract)
                .forEach(signer -> {
                    try {
                        if (signer.getType() == DigitalContractSignerType.LANDLORD) {
                            contractNotificationService.sendContractFinishedLL(signer);
                        } else if (signer.getType() == DigitalContractSignerType.TENANT) {
                            contractNotificationService.sendContractFinishedPS(signer, digitalContract);
                        }
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage(), e);
                    }
                });
    }

    private void loadAndSaveSignedDocuments(DigitalContract digitalContract) {
        String filenameWithoutEnding = getContractFilenameWithoutEnding(digitalContract);

        docuSignService.loadAndSaveSignedDocuments(digitalContract, filenameWithoutEnding);

        contractRepository.save(digitalContract);
    }

}
