package de.immomio.service.guest;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.service.propertySearcher.PropertySearcherUserNotificationService;
import de.immomio.service.shared.EmailModelProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class PropertySearcherUserGuestNotificationService {

    private static final String GUEST_MODE_ALREADY_REGISTERED_REMINDER_SUBJECT = "guest_mode_already_registered_reminder.subject";
    private static final String GUEST_MODE_APPLICATION_CONFIRMATION_SUBJECT = "guest_mode_application_confirmation.subject";

    private final PropertySearcherMailSender mailSender;
    private final EmailModelProvider emailModelProvider;
    private final PropertySearcherUserNotificationService propertySearcherUserNotificationService;

    @Autowired
    public PropertySearcherUserGuestNotificationService(PropertySearcherMailSender mailSender,
            EmailModelProvider emailModelProvider,
            PropertySearcherUserNotificationService propertySearcherUserNotificationService) {
        this.mailSender = mailSender;
        this.emailModelProvider = emailModelProvider;
        this.propertySearcherUserNotificationService = propertySearcherUserNotificationService;
    }

    public void sendRegisterReminder(PropertySearcherUserProfile userProfile, Property property) {
        Map<String, Object> model = emailModelProvider.createUserProfilePropertyModel(userProfile, property);
        emailModelProvider.appendPSAppUrl(model);

        mailSender.send(userProfile, MailTemplate.GUEST_ALREADY_REGISTERED_REMINDER, GUEST_MODE_ALREADY_REGISTERED_REMINDER_SUBJECT, model,
                property.getCustomer());
    }

    public void sendApplicationConfirmed(PropertyApplication propertyApplication, String token) {
        Map<String, Object> model = emailModelProvider.createUserProfilePropertyModel(propertyApplication.getUserProfile(),
                propertyApplication.getProperty());
        emailModelProvider.appendToken(model, token);
        emailModelProvider.appendPSAppUrl(model);
        emailModelProvider.appendSubjectPlaceholder(model, propertyApplication.getProperty().getData().getName());

        mailSender.send(propertyApplication.getUserProfile(), MailTemplate.GUEST_APPLICATION_CONFIRMATION, GUEST_MODE_APPLICATION_CONFIRMATION_SUBJECT, model,
                propertyApplication.getProperty().getCustomer());
    }

    public void sendEmailVerifyNotification(PropertySearcherUserProfile userProfile) {
        propertySearcherUserNotificationService.sendEmailVerifyNotification(userProfile);
    }
}
