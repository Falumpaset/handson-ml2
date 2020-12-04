package de.immomio.service.shared.contract.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.immomio.data.base.type.contract.DigitalContractSignatureType;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.shared.bean.contract.DigitalContractSignerData;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.mail.LandlordMailBrokerContainer;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.service.landlord.AbstractNotificationService;
import de.immomio.service.shared.EmailModelProvider;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Niklas Lindemann
 */
public abstract class AbstractDigitalContractStatusNotificationService extends AbstractNotificationService {

    private static final String DIGITAL_CONTRACT_SIGN_PS_SUBJECT =
            "tenant.contract.sign.subject";

    private static final String DIGITAL_CONTRACT_SIGN_LL_SUBJECT =
            "landlord.contract.sign.subject";

    private static final String DIGITAL_CONTRACT_FINISHED_PS_SUBJECT =
            "tenant.contract.finished.subject";

    private static final String DIGITAL_CONTRACT_FINISHED_LL_SUBJECT =
            "landlord.contract.finished.subject";
    public static final String DIGITAL_CONTRACT_PS_AES_CODE_READY_SUBJECT = "DIGITAL_CONTRACT_PS_AES_CODE_READY_SUBJECT";

    private final EmailModelProvider emailModelProvider;

    private final List<String> ITP_FAILED_EMAIL_ADDRESSES = List.of("ahansen@immomio.de", "fbeck@immomio.de", "nlindemann@immomio.de", "jhiemer@immomio.de", "aschoenfeldt@immomio.de");

    public AbstractDigitalContractStatusNotificationService(RabbitTemplate rabbitTemplate, EmailModelProvider emailModelProvider) {
        super(rabbitTemplate);
        this.emailModelProvider = emailModelProvider;
    }

    public void sendContractSignPS(DigitalContractSigner signer)
            throws JsonProcessingException {

        if (shouldNotSendMail(signer)) {
            return;
        }

        String token = generateDigitalContractSignToken(signer.getId());
        LandlordCustomer customer = signer.getDigitalContract().getCustomer();

        var data = emailModelProvider.buildBaseContractMailData(token, signer);
        emailModelProvider.appendReturnUrl(data, getPropertySearcherAppUrl());

        PropertySearcherProfileMailBrokerContainer mailBrokerContainer = new PropertySearcherProfileMailBrokerContainer(
                customer.getId(),
                signer.getData().getEmail(),
                MailTemplate.DIGITAL_CONTRACT_TENANT_SIGN,
                DIGITAL_CONTRACT_SIGN_PS_SUBJECT,
                data);

        sendPropertySearcherEmailMessage(mailBrokerContainer);
    }

    public void sendContractSignLL(DigitalContractSigner signer)
            throws JsonProcessingException {

        if (shouldNotSendMail(signer)) {
            return;
        }

        String token = generateDigitalContractSignToken(signer.getId());
        Map<String, Object> data = emailModelProvider.buildBaseContractMailData(token, signer);

        data.put(ModelParams.RETURN_URL, getLandlordAppUrl());
        LandlordMailBrokerContainer mailBrokerContainer = new LandlordMailBrokerContainer(
                signer.getData().getEmail(),
                MailTemplate.DIGITAL_CONTRACT_LANDLORD_SIGN,
                DIGITAL_CONTRACT_SIGN_LL_SUBJECT,
                data);

        sendLandlordEmailMessage(mailBrokerContainer);
    }

    public void sendContractFinishedPS(DigitalContractSigner signer, DigitalContract digitalContract)
            throws JsonProcessingException {

        String token = generateDigitalContractSignToken(signer.getId());
        Map<String, Object> data = emailModelProvider.buildBaseContractMailData(token, signer);

        LandlordCustomer customer = digitalContract.getCustomer();

        emailModelProvider.appendReturnUrl(data, getPropertySearcherAppUrl());

        PropertySearcherProfileMailBrokerContainer mailBrokerContainer = new PropertySearcherProfileMailBrokerContainer(
                signer.getDigitalContract().getCustomer().getId(),
                signer.getData().getEmail(),
                MailTemplate.DIGITAL_CONTRACT_FINISHED_PS,
                DIGITAL_CONTRACT_FINISHED_PS_SUBJECT,
                data);

        sendPropertySearcherEmailMessage(mailBrokerContainer);
    }

    public void sendContractFinishedLL(DigitalContractSigner signer)
            throws JsonProcessingException {

        String token = generateDigitalContractSignToken(signer.getId());
        Map<String, Object> data = emailModelProvider.buildBaseContractMailData(token, signer);

        data.put(ModelParams.RETURN_URL, getLandlordAppUrl());
        LandlordMailBrokerContainer mailBrokerContainer = new LandlordMailBrokerContainer(
                signer.getData().getEmail(),
                MailTemplate.DIGITAL_CONTRACT_FINISHED_LL,
                DIGITAL_CONTRACT_FINISHED_LL_SUBJECT,
                data);

        sendLandlordEmailMessage(mailBrokerContainer);
    }

    public void sendAesTokenReadyPs(DigitalContractSigner signer) throws JsonProcessingException {
        if (shouldNotSendMail(signer)) {
            return;
        }

        String token = generateDigitalContractSignToken(signer.getId());
        Map<String, Object> data = emailModelProvider.buildBaseContractMailData(token, signer);
        emailModelProvider.appendReturnUrl(data, getPropertySearcherAppUrl());

        PropertySearcherProfileMailBrokerContainer mailBrokerContainer = new PropertySearcherProfileMailBrokerContainer(
                signer.getDigitalContract().getCustomer().getId(),
                signer.getData().getEmail(),
                MailTemplate.DIGITAL_CONTRACT_PS_AES_CODE_READY,
                DIGITAL_CONTRACT_PS_AES_CODE_READY_SUBJECT,
                data);

        sendPropertySearcherEmailMessage(mailBrokerContainer);
    }

    public void sendAesTokenFailedPs(DigitalContractSigner signer) throws JsonProcessingException {
        if (shouldNotSendMail(signer)) {
            return;
        }

        var token = generateDigitalContractSignToken(signer.getId());
        var data = emailModelProvider.buildBaseContractMailData(token, signer);

        DigitalContractSignerData signerData = signer.getData();
        emailModelProvider.appendReturnUrl(data, getPropertySearcherAppUrl());
        PropertySearcherProfileMailBrokerContainer mailBrokerContainer = new PropertySearcherProfileMailBrokerContainer(
                signer.getDigitalContract().getCustomer().getId(),
                signerData.getEmail(),
                MailTemplate.DIGITAL_CONTRACT_PS_AES_CODE_INSTANT_TRANSFER_FAILED,
                DIGITAL_CONTRACT_PS_AES_CODE_READY_SUBJECT,
                data);

        sendPropertySearcherEmailMessage(mailBrokerContainer);
    }

    public void sendItpFailed(DigitalContractSigner failedSigner, List<DigitalContractSigner> signers) {
        signers.forEach(signer -> {
            if(signer.getDigitalContract().getSignatureType() == DigitalContractSignatureType.AES_OFFICE) {
                return;
            }

            Map<String, Object> data = new HashMap<>();
            data.put(ModelParams.MODEL_CONTRACT_SIGNER_DATA, signer.getData());
            data.put(ModelParams.MODEL_CONTRACT_PROPERTY_DATA, signer.getDigitalContract().getPropertyData());
            data.put(ModelParams.MODEL_CONTRACT_CANCELED_SIGNER_DATA, failedSigner.getData());

            if (signer.getType() == DigitalContractSignerType.TENANT) {
                PropertySearcherProfileMailBrokerContainer mailBrokerContainer = new PropertySearcherProfileMailBrokerContainer(
                        signer.getDigitalContract().getCustomer().getId(),
                        signer.getData().getEmail(),
                        MailTemplate.DIGITAL_CONTRACT_PS_AES_FAILED_TO_LL,
                        DIGITAL_CONTRACT_PS_AES_CODE_READY_SUBJECT,
                        data);
                sendPropertySearcherEmailMessage(mailBrokerContainer);
            } else {
                LandlordMailBrokerContainer mailBrokerContainer = new LandlordMailBrokerContainer(
                        signer.getData().getEmail(),
                        MailTemplate.DIGITAL_CONTRACT_PS_AES_FAILED_TO_LL,
                        DIGITAL_CONTRACT_FINISHED_LL_SUBJECT,
                        data);

                sendLandlordEmailMessage(mailBrokerContainer);
            }

        });
    }

    public void sentInternalItpWarn(DigitalContractSigner signer) {
        sendItpMailInternal(signer, MailTemplate.DIGITAL_CONTRACT_INTERNAL_ITP_WARN, "itp.warn.subject");
    }

    private void sendItpMailInternal(DigitalContractSigner signer, MailTemplate digitalContractInternalItpWarn, String subject) {
        Map<String, Object> data = new HashMap<>();
        data.put(ModelParams.MODEL_CONTRACT_SIGNER_DATA, signer.getData());
        data.put("contractid", signer.getDigitalContract().getId());

        ITP_FAILED_EMAIL_ADDRESSES.forEach(mailAddress -> {
            LandlordMailBrokerContainer mailBrokerContainer = new LandlordMailBrokerContainer(
                    mailAddress,
                    digitalContractInternalItpWarn,
                    subject,
                    data);
            sendLandlordEmailMessage(mailBrokerContainer);
        });
    }

    public void sentInternalItpFailed(DigitalContractSigner signer) {
        sendItpMailInternal(signer, MailTemplate.DIGITAL_CONTRACT_INTERNAL_ITP_FAILED, "itp.failed.subject");
    }

    protected abstract String generateDigitalContractSignToken(Long signerId);

    protected abstract String getPropertySearcherAppUrl();

    protected abstract String getLandlordAppUrl();

    private boolean shouldNotSendMail(DigitalContractSigner signer) {
        if (signer.getDigitalContract().getSignatureType() == DigitalContractSignatureType.AES_OFFICE && (signer.isOnsiteHost() || signer.getType() == DigitalContractSignerType.TENANT)) {
            return true;
        }
        return false;
    }
}
