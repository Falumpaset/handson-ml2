package de.immomio.service.notification;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import de.immomio.service.shared.EmailModelProvider;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static de.immomio.mail.sender.templates.MailTemplate.APPLICATION_REJECTED_V1;
import static de.immomio.mail.sender.templates.MailTemplate.APPLICATION_REJECTED_V2;
import static de.immomio.mail.sender.templates.MailTemplate.APPLICATION_REJECTED_V3;
import static de.immomio.mail.sender.templates.MailTemplate.INVITE_ANONYMOUS_USER_TO_REGISTER;
import static de.immomio.messaging.config.QueueConfigUtils.MailPropertySearcherBrokerConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.MailPropertySearcherBrokerConfig.ROUTING_KEY;

public abstract class AbstractNotificationService {

    private static final String APPLICATION_REJECTED_SUBJECT_KEY = "application.rejected.subject";
    private static final String APPLICATION_REJECTED_V2_SUBJECT_KEY = "application.rejected.v2.subject";
    private static final String APPLICATION_REJECTED_V3_SUBJECT_KEY = "application.rejected.v3.subject";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private EmailModelProvider emailModelProvider;

    public void sendRejectedApplicationNotification(PropertyApplication application) {
        sendRejectedApplicationNotification(application, MailTemplate.APPLICATION_REJECTED_V1);
    }

    public void sendRejectedApplicationNotification(PropertyApplication application, MailTemplate mailTemplate) {
        PropertySearcherUserProfile userProfile = application.getUserProfile();
        Property property = application.getProperty();
        LandlordCustomer customer = property.getCustomer();
        Map<String, Object> model = createApplicationModel(userProfile, property);

        Object[] subjectFormat = new String[]{property.getData().getName()};

        PropertySearcherProfileMailBrokerContainer container;
        if (mailTemplate != null) {
            switch (mailTemplate) {
                case APPLICATION_REJECTED_V2:
                    container = new PropertySearcherProfileMailBrokerContainer(
                            userProfile.getId(),
                            MailTemplate.APPLICATION_REJECTED_V2,
                            APPLICATION_REJECTED_V2_SUBJECT_KEY,
                            subjectFormat,
                            model,
                            customer.getId());
                    break;
                case APPLICATION_REJECTED_V3:
                    container = new PropertySearcherProfileMailBrokerContainer(
                            userProfile.getId(),
                            MailTemplate.APPLICATION_REJECTED_V3,
                            APPLICATION_REJECTED_V3_SUBJECT_KEY,
                            subjectFormat,
                            model,
                            customer.getId());
                    break;
                default:
                    container = new PropertySearcherProfileMailBrokerContainer(
                            userProfile.getId(),
                            MailTemplate.APPLICATION_REJECTED_V1,
                            APPLICATION_REJECTED_SUBJECT_KEY,
                            subjectFormat,
                            model,
                            customer.getId());
                    break;
            }
        } else {
            container = new PropertySearcherProfileMailBrokerContainer(
                    userProfile.getId(),
                    MailTemplate.APPLICATION_REJECTED_V1,
                    APPLICATION_REJECTED_SUBJECT_KEY,
                    subjectFormat,
                    model,
                    customer.getId());
        }

        sendPropertySearcherEmailMessage(container, userProfile);
    }

    public void sendRejectedApplicationNotification(List<PropertyApplication> rejectedApplications) {
        rejectedApplications.forEach(this::sendRejectedApplicationNotification);
    }

    protected Map<String, Object> createApplicationModel(PropertySearcherUserProfile userProfile, Property property) {
        return emailModelProvider.createUserProfilePropertyModel(userProfile, property);
    }

    protected void sendPropertySearcherEmailMessage(PropertySearcherProfileMailBrokerContainer messageBean,
                                                    PropertySearcherUserProfile userProfile) {
        if (userProfile.isAnonymous() && !isEmailAllowedToSend(messageBean.getTemplate())) {
            return;
        }

        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, messageBean);
    }

    protected boolean isEmailAllowedToSend(MailTemplate template) {
        return template == APPLICATION_REJECTED_V1 || template == APPLICATION_REJECTED_V2 ||
               template == APPLICATION_REJECTED_V3 || template == INVITE_ANONYMOUS_USER_TO_REGISTER;
    }

}
