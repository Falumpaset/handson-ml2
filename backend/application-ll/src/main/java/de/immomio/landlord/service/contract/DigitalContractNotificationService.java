package de.immomio.landlord.service.contract;

import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.mail.configurator.LandlordMailConfigurator;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.mail.LandlordMailBrokerContainer;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.service.landlord.AbstractNotificationService;
import de.immomio.service.shared.EmailModelProvider;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DigitalContractNotificationService extends AbstractNotificationService {

    private static final String DIGITAL_CONTRACT_SIGN_PS_SUBJECT =
            "tenant.contract.sign.subject";

    private static final String DIGITAL_CONTRACT_SIGN_LL_SUBJECT =
            "landlord.contract.sign.subject";
    private final EmailModelProvider emailModelProvider;
    private final LandlordMailConfigurator landlordMailConfigurator;

    @Autowired
    public DigitalContractNotificationService(RabbitTemplate rabbitTemplate, EmailModelProvider emailModelProvider, LandlordMailConfigurator landlordMailConfigurator1) {
       super(rabbitTemplate);
        this.emailModelProvider = emailModelProvider;
        this.landlordMailConfigurator = landlordMailConfigurator1;
    }


    public void resendSignerEmail(DigitalContractSigner signer, String token) {
        LandlordCustomer customer = signer.getDigitalContract().getCustomer();

        Map<String, Object> data = emailModelProvider.buildBaseContractMailData(token, signer);

        if (signer.getType() == DigitalContractSignerType.TENANT) {
            emailModelProvider.appendReturnUrl(data, landlordMailConfigurator.buildPsAppUrl());
            PropertySearcherProfileMailBrokerContainer mailBrokerContainer = new PropertySearcherProfileMailBrokerContainer(
                    customer.getId(),
                    signer.getData().getEmail(),
                    MailTemplate.DIGITAL_CONTRACT_TENANT_SIGN,
                    DIGITAL_CONTRACT_SIGN_PS_SUBJECT,
                    data);
            sendPropertySearcherEmailMessage(mailBrokerContainer);
        } else {
            emailModelProvider.appendReturnUrl(data, landlordMailConfigurator.buildAppUrl());
            LandlordMailBrokerContainer mailBrokerContainer = new LandlordMailBrokerContainer(signer.getData().getEmail(), MailTemplate.DIGITAL_CONTRACT_LANDLORD_SIGN, DIGITAL_CONTRACT_SIGN_LL_SUBJECT, data);
            sendLandlordEmailMessage(mailBrokerContainer);
        }

    }
}
