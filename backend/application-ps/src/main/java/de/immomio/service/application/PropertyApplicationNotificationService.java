package de.immomio.service.application;

import de.immomio.data.landlord.bean.property.PropertyMailBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileEmailBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class PropertyApplicationNotificationService {

    private static final String APPLICATION_CONFIRMED_SUBJECT_KEY = "application.confirmed.subject";

    private static final String APPLICATION_ACCEPTED_SUBJECT_KEY = "application.accepted.subject";

    private final PropertySearcherMailSender mailSender;

    @Value("${appointment.durationInMinutes}")
    private int durationInMinutes;

    @Autowired
    public PropertyApplicationNotificationService(PropertySearcherMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void applicationConfirmed(PropertySearcherUserProfile userProfile, Property property) {
        Map<String, Object> model = createApplicationModel(userProfile, property);
        mailSender.send(
                userProfile,
                MailTemplate.APPLICATION_CONFIRMED,
                APPLICATION_CONFIRMED_SUBJECT_KEY,
                model,
                property.getCustomer().getId());
    }

    public void applicationAccepted(PropertySearcherUserProfile userProfile, Property property) {
        Map<String, Object> model = createApplicationModel(userProfile, property);
        mailSender.send(userProfile, MailTemplate.APPLICATION_ACCEPTED_V1, APPLICATION_ACCEPTED_SUBJECT_KEY, model);
    }

    private Map<String, Object> createApplicationModel(PropertySearcherUserProfile userProfile, Property property) {
        Map<String, Object> model = new HashMap<>();
        model.put(ModelParams.MODEL_USER, new PropertySearcherUserProfileEmailBean(userProfile));
        model.put(ModelParams.MODEL_USER_PROFILE, userProfile.getData());
        model.put(ModelParams.MODEL_FLAT, new PropertyMailBean(property));
        populateCustomerPreferencesAndAccessing(model, property.getCustomer());

        return model;
    }

    private void populateCustomerPreferencesAndAccessing(Map<String, Object> model, LandlordCustomer customer) {
        model.put(ModelParams.MODEL_PREFERENCES, customer.getPreferences());
        model.put(ModelParams.MODEL_ALLOW_BRANDING, customer.isBrandingAllowed());
        model.put(ModelParams.MODEL_BRANDING_LOGO, customer.getBrandingLogo());
    }

}
