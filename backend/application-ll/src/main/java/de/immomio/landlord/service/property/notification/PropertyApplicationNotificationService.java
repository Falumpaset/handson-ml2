package de.immomio.landlord.service.property.notification;

import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.security.service.JWTTokenService;
import de.immomio.service.landlord.AbstractNotificationService;
import de.immomio.service.shared.EmailModelProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PropertyApplicationNotificationService extends AbstractNotificationService {

    private static final String APPLICATION_REJECTED_SUBJECT_KEY = "application.rejected.subject";
    private static final String APPLICATION_REJECTED_V2_SUBJECT_KEY = "application.rejected.v2.subject";
    private static final String APPLICATION_REJECTED_V3_SUBJECT_KEY = "application.rejected.v3.subject";
    private static final String APPLICATION_ACCEPTED_SUBJECT_KEY = "application.accepted.subject";
    private static final String APPLICATION_ACCEPTED_ANONYMOUS_USER_SUBJECT_KEY =
            "application.accepted.subject.anonymous";
    private static final String APPLICATION_ACCEPTED_V2_SUBJECT_KEY = "application.accepted.v2.subject";
    private static final String APPLICATION_ACCEPTED_V3_SUBJECT_KEY = "application.accepted.v3.subject";
    private static final String PROPERTY_ACCEPTED_SUBJECT_KEY = "flat.acceptance.subject";
    private static final String APPLICATION_UNREJECTED_SUBJECT_KEY = "application.unrejected.subject";
    private static final String PRE_TENANT_VIEWING_SUBJECT = "pre.tenant.viewing.subject";

    private final EmailModelProvider emailModelProvider;
    private final JWTTokenService jwtTokenService;

    @Autowired
    public PropertyApplicationNotificationService(EmailModelProvider emailModelProvider, RabbitTemplate rabbitTemplate, JWTTokenService jwtTokenService) {
        super(rabbitTemplate);
        this.emailModelProvider = emailModelProvider;
        this.jwtTokenService = jwtTokenService;
    }

    public void applicationAccepted(PropertyApplication application, MailTemplate mailTemplate, String token) {
        PropertySearcherUserProfile userProfile = application.getUserProfile();
        Property property = application.getProperty();
        LandlordCustomer customer = property.getCustomer();
        Map<String, Object> model = createApplicationModel(userProfile, property);
        emailModelProvider.appendApplicationId(model, application);
        emailModelProvider.appendToken(model, token);

        PropertySearcherProfileMailBrokerContainer container;
        if (mailTemplate != null) {
            switch (mailTemplate) {
                case APPLICATION_ACCEPTED_V2:
                    container = new PropertySearcherProfileMailBrokerContainer(
                            userProfile.getId(),
                            MailTemplate.APPLICATION_ACCEPTED_V2,
                            APPLICATION_ACCEPTED_V2_SUBJECT_KEY,
                            model,
                            customer.getId());
                    break;
                case APPLICATION_ACCEPTED_V3:
                    container = new PropertySearcherProfileMailBrokerContainer(
                            userProfile.getId(),
                            MailTemplate.APPLICATION_ACCEPTED_V3,
                            APPLICATION_ACCEPTED_V3_SUBJECT_KEY,
                            model,
                            customer.getId());
                    break;
                default:
                    container = new PropertySearcherProfileMailBrokerContainer(
                            userProfile.getId(),
                            MailTemplate.APPLICATION_ACCEPTED_V1,
                            APPLICATION_ACCEPTED_SUBJECT_KEY,
                            model,
                            customer.getId());
                    break;
            }
        } else {
            container = new PropertySearcherProfileMailBrokerContainer(
                    userProfile.getId(),
                    MailTemplate.APPLICATION_ACCEPTED_V1,
                    APPLICATION_ACCEPTED_SUBJECT_KEY,
                    model,
                    customer.getId());
        }

        sendPropertySearcherEmailMessage(container, userProfile);
    }

    public void applicationAcceptedAnonymousUser(PropertyApplication application) {
        PropertySearcherUserProfile userProfile = application.getUserProfile();
        Property property = application.getProperty();
        LandlordCustomer customer = property.getCustomer();
        Map<String, Object> model = createApplicationModel(userProfile, property);

        PropertySearcherProfileMailBrokerContainer container = new PropertySearcherProfileMailBrokerContainer(
                userProfile.getId(),
                MailTemplate.INVITE_ANONYMOUS_USER_TO_REGISTER,
                APPLICATION_ACCEPTED_ANONYMOUS_USER_SUBJECT_KEY,
                model,
                customer.getId());

        sendPropertySearcherEmailMessage(container, userProfile);
    }

    public void sendAcceptedApplicationNotification(PropertyApplication application, MailTemplate template) {
        if (application.getUserProfile().getType() == PropertySearcherUserProfileType.ANONYMOUS) {
            applicationAcceptedAnonymousUser(application);
        } else {

            String token = null;
            if (application.getUserProfile().getType() == PropertySearcherUserProfileType.GUEST) {
                try {
                    token = jwtTokenService.generateGuestUserToken(application);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }

            applicationAccepted(application, template, token);
        }
    }

    public void sendPreTenantViewingNotification(PropertyApplication application, String token) {
        PropertySearcherUserProfile userProfile = application.getUserProfile();
        Property property = application.getProperty();

        Object[] subjectFormat = new String[]{property.getData().getName()};

        Map<String, Object> model = createApplicationModel(userProfile, property);

        Map<String, Object> intentMailModel = getIntentMailModel(application, token);

        model.putAll(intentMailModel);
        PropertySearcherProfileMailBrokerContainer container = new PropertySearcherProfileMailBrokerContainer(
                userProfile.getId(),
                MailTemplate.PRE_TENANT_APPOINTMENT,
                PRE_TENANT_VIEWING_SUBJECT,
                subjectFormat,
                model,
                property.getCustomer().getId());

        sendPropertySearcherEmailMessage(container, userProfile);
    }

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

    public void propertyAccepted(PropertySearcherUserProfile userProfile, Property property) {
        Map<String, Object> model = createApplicationModel(userProfile, property);
        LandlordCustomer customer = property.getCustomer();

        PropertySearcherProfileMailBrokerContainer container = new PropertySearcherProfileMailBrokerContainer(
                userProfile.getId(),
                MailTemplate.FLAT_ACCEPTED,
                PROPERTY_ACCEPTED_SUBJECT_KEY,
                model,
                customer.getId());

        sendPropertySearcherEmailMessage(container, userProfile);
    }

    private Map<String, Object> createApplicationModel(PropertySearcherUserProfile userProfile, Property property) {
        return emailModelProvider.createUserProfilePropertyModel(userProfile, property);
    }

    public void applicationUnrejected(PropertyApplication application) {
        PropertySearcherUserProfile userProfile = application.getUserProfile();
        Property property = application.getProperty();
        Map<String, Object> model = createApplicationModel(userProfile, property);

        Object[] subjectFormat = new String[]{property.getData().getName()};

        PropertySearcherProfileMailBrokerContainer container = new PropertySearcherProfileMailBrokerContainer(
                userProfile.getId(),
                MailTemplate.APPLICATION_UNREJECTED,
                APPLICATION_UNREJECTED_SUBJECT_KEY,
                subjectFormat,
                model,
                property.getCustomer().getId());

        sendPropertySearcherEmailMessage(container, userProfile);
    }

    public void sendRejectedApplicationNotification(List<PropertyApplication> rejectedApplications) {
        rejectedApplications.forEach(this::sendRejectedApplicationNotification);
    }

    public void askForIntentNotification(PropertyApplication application, MailTemplate template, String token, String subject) {
        Map<String, Object> intentMailModel = getIntentMailModel(application, token);
        LandlordCustomer customer = application.getProperty().getCustomer();

        PropertySearcherUserProfile userProfile = application.getUserProfile();
        PropertySearcherProfileMailBrokerContainer container = new PropertySearcherProfileMailBrokerContainer(
                userProfile.getId(),
                template,
                subject,
                intentMailModel,
                customer.getId()
        );

        sendPropertySearcherEmailMessage(container, userProfile);
    }

    private Map<String, Object> getIntentMailModel(PropertyApplication application, String token) {
        Map<String, Object> model = emailModelProvider.createUserProfilePropertyModel(application.getUserProfile(), application.getProperty());
        model.put(ModelParams.MODEL_TOKEN, token);
        model.put(ModelParams.MODEL_APPLICATION, application.getId());
        model.put(ModelParams.SUBJECT_PLACEHOLDER, application.getProperty().getData().getName());

        return model;
    }
}
