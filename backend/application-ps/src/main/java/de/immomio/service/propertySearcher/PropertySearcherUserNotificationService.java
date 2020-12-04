package de.immomio.service.propertySearcher;

import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBranding;
import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomerBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileEmailBean;
import de.immomio.mail.ModelParams;
import de.immomio.mail.configurator.PropertySearcherMailConfigurator;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.security.service.JWTTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class PropertySearcherUserNotificationService {

    private static final String EMAIL_REGISTER_SUBJECT_KEY = "register.subject";
    private static final String EMAIL_VERIFICATION_SUBJECT_KEY = "email.verification.subject";

    private final JWTTokenService jwtTokenService;

    private final PropertySearcherMailConfigurator mailConfigurator;

    private final PropertySearcherMailSender propertySearcherMailSender;

    @Value("${email.period.verificationInDays}")
    private int emailVerificationPeriod;

    @Autowired
    public PropertySearcherUserNotificationService(JWTTokenService jwtTokenService, PropertySearcherMailConfigurator mailConfigurator, PropertySearcherMailSender propertySearcherMailSender) {
        this.jwtTokenService = jwtTokenService;
        this.mailConfigurator = mailConfigurator;
        this.propertySearcherMailSender = propertySearcherMailSender;
    }

    public void sendEmailVerifyNotification(PropertySearcherUserProfile userProfile) {
        try {
            String token = jwtTokenService.generateEmailToken(userProfile.getUser(), emailVerificationPeriod);

            Map<String, Object> model = new HashMap<>();
            model.put(ModelParams.MODEL_TOKEN, token);
            model.put(ModelParams.MODEL_USER, new PropertySearcherUserProfileEmailBean(userProfile));
            model.put(ModelParams.RETURN_URL, mailConfigurator.buildAppUrl());

            propertySearcherMailSender.send(userProfile, MailTemplate.EMAIL_VERIFICATION, EMAIL_VERIFICATION_SUBJECT_KEY, model);
        } catch (IOException error) {
            log.error(error.getMessage(), error);
        }
    }

    public void notifyRegisteredUser(PropertySearcherUserProfile userProfile) {
        if (isRegisterMailAllowedForUser(userProfile)) {
            Map<String, Object> model = createRegisterModel(userProfile);
            propertySearcherMailSender.send(userProfile, MailTemplate.REGISTER_USER, EMAIL_REGISTER_SUBJECT_KEY, null, model, null, null);
        }
    }

    private boolean isRegisterMailAllowedForUser(PropertySearcherUserProfile userProfile) {
        if (userProfile.isInInternalPool() == Boolean.TRUE) {
            LandlordCustomerBranding landlordCustomerBranding = userProfile.getCustomerBranding();
            if (landlordCustomerBranding != null && landlordCustomerBranding.getDisplayITPFlags() != null) {
                return landlordCustomerBranding.getDisplayITPFlags().isSendRegisterMail();
            }
        }
        return true;
    }

    private Map<String, Object> createRegisterModel(PropertySearcherUserProfile userProfile) {
        Map<String, Object> model = new HashMap<>();
        model.put(ModelParams.MODEL_USER, new PropertySearcherUserProfileEmailBean(userProfile));
        model.put(ModelParams.MODEL_USER_PROFILE, userProfile.getData());
        model.put(ModelParams.MODEL_CUSTOMER, new PropertySearcherCustomerBean(userProfile.getUser().getCustomer()));
        model.put(ModelParams.MODEL_IGNORE_DISCLAIMER, true);

        return model;
    }
}
