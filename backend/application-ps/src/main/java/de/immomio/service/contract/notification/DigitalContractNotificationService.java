package de.immomio.service.contract.notification;

import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.landlord.bean.user.AgentInfo;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.config.QueueConfigUtils;
import de.immomio.messaging.container.mail.LandlordMailBrokerContainer;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import de.immomio.service.landlord.AbstractNotificationService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DigitalContractNotificationService extends AbstractNotificationService {

    public static final String CONTRACT_PS_STOPPED_PROCESS_SUBJECT = "contract.ps.stopped.process.subject";
    public static final String CONTRACT_PS_AES_FAILED_SUBJECT = "contract.ps.aes.failed.subject";

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public DigitalContractNotificationService(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
        this.rabbitTemplate = rabbitTemplate;
    }

    public void notifyLandlordDataNotCorrect(DigitalContractSigner signer) {

        var agentEmail = signer.getDigitalContract().getAgentInfo().getEmail();

        if (isAgentNotSigner(signer, agentEmail)) {
            notifyLandlord(signer, MailTemplate.DIGITAL_CONTRACT_LL_AGENT_MUST_REVIEW_DATA);
        }

    }

    public void notifyFlatNotVisited(DigitalContractSigner signer) {
        notifySignerCanceled(signer, MailTemplate.DIGITAL_CONTRACT_LL_SIGNER_FLAT_NOT_VISITED, MailTemplate.DIGITAL_CONTRACT_LL_SIGNER_FLAT_NOT_VISITED, CONTRACT_PS_STOPPED_PROCESS_SUBJECT);
        if (isAgentNotSigner(signer, signer.getDigitalContract().getAgentInfo().getEmail())) {
            notifyLandlord(signer, MailTemplate.DIGITAL_CONTRACT_LL_AGENT_FLAT_NOT_VISITED);
        }
    }

    public void notifySignersDataNotCorrect(DigitalContractSigner signer) {
        notifySignerCanceled(signer, MailTemplate.DIGITAL_CONTRACT_PS_DATA_WRONG, MailTemplate.DIGITAL_CONTRACT_PS_DATA_WRONG_TO_LL, CONTRACT_PS_STOPPED_PROCESS_SUBJECT);
    }

    public void notifySignerCanceled(DigitalContractSigner signer, MailTemplate templateTenant, MailTemplate templateLandlord, String subject) {
        DigitalContract contract = signer.getDigitalContract();

        contract.getSigners().forEach(oneSigner -> {
            if (oneSigner.getType() == DigitalContractSignerType.LANDLORD) {
                Map<String, Object> data = new HashMap<>();
                data.put(ModelParams.MODEL_CONTRACT_SIGNER_DATA, oneSigner.getData());
                data.put(ModelParams.MODEL_CONTRACT_PROPERTY_DATA, oneSigner.getDigitalContract().getPropertyData());
                data.put(ModelParams.MODEL_CONTRACT_CANCELED_SIGNER_DATA, signer.getData());

                LandlordMailBrokerContainer container = new LandlordMailBrokerContainer(
                        oneSigner.getData().getEmail(),
                        templateLandlord,
                        subject,
                        data
                );

                sendLandlordEmailMessage(container);
            } else if (oneSigner.getType() == DigitalContractSignerType.TENANT) {
                Map<String, Object> data = new HashMap<>();
                LandlordCustomer customer = contract.getCustomer();

                data.put(ModelParams.MODEL_CONTRACT_SIGNER_DATA, oneSigner.getData());
                data.put(ModelParams.MODEL_CONTRACT_PROPERTY_DATA, oneSigner.getDigitalContract().getPropertyData());
                data.put(ModelParams.MODEL_CONTRACT_CANCELED_SIGNER_DATA, signer.getData());
                data.put(ModelParams.MODEL_BRANDING_LOGO, customer.getBrandingLogo());
                data.put(ModelParams.MODEL_ALLOW_BRANDING, customer.isBrandingAllowed());
                PropertySearcherProfileMailBrokerContainer container = new PropertySearcherProfileMailBrokerContainer(
                        customer.getId(),
                        oneSigner.getData().getEmail(),
                        templateTenant,
                        subject,
                        data
                );
                sendPropertySearcherEmailMessage(container);

            }
        });
    }

    public void notifyLandlordAesFailed(DigitalContractSigner failedSigner) {
        DigitalContract digitalContract = failedSigner.getDigitalContract();
        List<DigitalContractSigner> landlordSigners = digitalContract.getSigners().stream()
                .filter(signer -> signer.getType() == DigitalContractSignerType.LANDLORD)
                .collect(Collectors.toList());

        landlordSigners.forEach(landlordSigner -> {
            Map<String, Object> data = new HashMap<>();
            data.put(ModelParams.MODEL_CONTRACT_SIGNER_DATA, landlordSigner.getData());
            data.put(ModelParams.MODEL_CONTRACT_PROPERTY_DATA, digitalContract.getPropertyData());
            data.put(ModelParams.MODEL_CONTRACT_CANCELED_SIGNER_DATA, failedSigner.getData());
            LandlordMailBrokerContainer container = new LandlordMailBrokerContainer(
                    landlordSigner.getData().getEmail(),
                    MailTemplate.DIGITAL_CONTRACT_PS_AES_FAILED_TO_LL,
                    CONTRACT_PS_AES_FAILED_SUBJECT,
                    data
            );
            sendLandlordEmailMessage(container);
        });

        AgentInfo user = digitalContract.getAgentInfo();
        if (user != null) {
            notifyLandlord(failedSigner, MailTemplate.DIGITAL_CONTRACT_PS_AES_FAILED_TO_LL_AGENT);
        }

    }

    public void sendLandlordEmailMessage(LandlordMailBrokerContainer messageBean) {
        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(
                QueueConfigUtils.MailLandlordBrokerConfig.EXCHANGE_NAME,
                QueueConfigUtils.MailLandlordBrokerConfig.ROUTING_KEY,
                messageBean);
    }

    private void notifyLandlord(DigitalContractSigner signer, MailTemplate digitalContractLlAgentFlatNotVisited) {
        DigitalContract digitalContract = signer.getDigitalContract();
        Map<String, Object> data = new HashMap<>();
        data.put(ModelParams.MODEL_CONTRACT_SIGNER_DATA, signer.getData());
        data.put(ModelParams.MODEL_CONTRACT_PROPERTY_DATA, digitalContract.getPropertyData());

        data.put(ModelParams.MODEL_USER, digitalContract.getAgentInfo());
        LandlordMailBrokerContainer container = new LandlordMailBrokerContainer(
                digitalContract.getAgentInfo().getId(),
                digitalContractLlAgentFlatNotVisited,
                CONTRACT_PS_STOPPED_PROCESS_SUBJECT,
                data
        );

        sendLandlordEmailMessage(container);
    }


    private boolean isAgentNotSigner(DigitalContractSigner signer, String agentEmail) {
        return signer.getDigitalContract().getSigners().stream().filter(contractSigner -> contractSigner.getType() == DigitalContractSignerType.LANDLORD)
                .noneMatch(landlord -> landlord.getData().getEmail().equals(agentEmail));
    }

}
