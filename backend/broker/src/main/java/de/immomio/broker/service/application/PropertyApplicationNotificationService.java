package de.immomio.broker.service.application;

import de.immomio.broker.service.MailMessageService;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.service.notification.AbstractNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PropertyApplicationNotificationService extends AbstractNotificationService {

    private static final String PROPERTY_ACCEPTED_SUBJECT_KEY = "flat.acceptance.subject";

    private final MailMessageService mailMessageService;

    @Autowired
    public PropertyApplicationNotificationService(MailMessageService mailMessageService) {
        this.mailMessageService = mailMessageService;
    }

    public void propertyAccepted(PropertySearcherUserProfile userProfile, Property property) {
        if (userProfile.isAnonymous() && !isEmailAllowedToSend(MailTemplate.FLAT_ACCEPTED)) {
            return;
        }

        Map<String, Object> model = createApplicationModel(userProfile, property);
        LandlordCustomer customer = property.getCustomer();

        PropertySearcherProfileMailBrokerContainer container = new PropertySearcherProfileMailBrokerContainer(
                userProfile.getId(),
                MailTemplate.FLAT_ACCEPTED,
                PROPERTY_ACCEPTED_SUBJECT_KEY,
                model,
                customer.getId());

        mailMessageService.onMessage(container);
    }

}
