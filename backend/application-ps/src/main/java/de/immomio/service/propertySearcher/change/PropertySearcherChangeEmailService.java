package de.immomio.service.propertySearcher.change;

import de.immomio.beans.ChangeEmailBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.ImmomioRuntimeException;
import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomerBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherChangeEmail;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileEmailBean;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserProfileRepository;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserRepository;
import de.immomio.model.repository.propertysearcher.user.change.email.PropertySearcherChangeEmailRepository;
import de.immomio.security.service.KeycloakService;
import de.immomio.service.security.UserSecurityService;
import de.immomio.utils.EmailAddressUtils;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class PropertySearcherChangeEmailService {

    public static final String EMAIL_CHANGED_SUBJECT_KEY = "email.changed.subject";
    public static final String TOKEN_MISSING_L = "TOKEN_MISSING_L";
    public static final String CHANGE_EMAIL_MISSING_L = "CHANGE_EMAIL_MISSING_L";
    public static final String USER_MISSING_L = "USER_MISSING_L";
    public static final String MORE_THAN_ONE_KEYCLOAK_USER_L = "MORE_THAN_ONE_KEYCLOAK_USER_L";
    private static final String EMAIL_CHANGE_SUBJECT_KEY = "email.change.subject";
    private static final String USER_NOT_FOUND_L = "USER_NOT_FOUND_L";
    private static final String EMAIL_ALREADY_TAKEN_L = "EMAIL_ALREADY_TAKEN_L";
    private static final String INVALID_EMAIL_ADDRESS_L = "INVALID_EMAIL_ADDRESS_L";
    private static final String CHANGE_EMAIL_BEAN_NULL_L = "CHANGE_EMAIL_BEAN_NULL_L";

    private final PropertySearcherChangeEmailRepository changeEmailRepository;

    private final PropertySearcherMailSender mailSender;

    private final PropertySearcherUserRepository userRepository;

    private final PropertySearcherUserProfileRepository userProfileRepository;

    private final UserSecurityService userSecurityService;

    private final KeycloakService keycloakService;

    @Autowired
    public PropertySearcherChangeEmailService(PropertySearcherChangeEmailRepository changeEmailRepository,
            PropertySearcherMailSender mailSender,
            PropertySearcherUserRepository userRepository,
            PropertySearcherUserProfileRepository userProfileRepository,
            UserSecurityService userSecurityService,
            KeycloakService keycloakService) {
        this.changeEmailRepository = changeEmailRepository;
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.userSecurityService = userSecurityService;
        this.keycloakService = keycloakService;
    }

    public PropertySearcherChangeEmail changeEmail(ChangeEmailBean changeEmailBean) {
        PropertySearcherUser user = userSecurityService.getPrincipalUser();
        if (user == null) {
            throw new ApiValidationException(USER_NOT_FOUND_L);
        }
        if (changeEmailBean == null) {
            throw new ApiValidationException(CHANGE_EMAIL_BEAN_NULL_L);
        }
        if (EmailAddressUtils.isInvalid(changeEmailBean.getEmail())) {
            throw new ApiValidationException(INVALID_EMAIL_ADDRESS_L);
        }
        if (userRepository.existsByEmail(changeEmailBean.getEmail())) {
            throw new ApiValidationException(EMAIL_ALREADY_TAKEN_L);
        }

        List<PropertySearcherChangeEmail> existing = changeEmailRepository.findByUser(user);
        PropertySearcherChangeEmail changeEmail = createPropertySearcherChangeEmail(user, changeEmailBean);

        changeEmail = changeEmailRepository.save(changeEmail);
        changeEmailRepository.deleteAll(existing);

        sendEmailChangeNotification(changeEmail, user.getMainProfile());

        return changeEmail;
    }

    public void confirmEmail(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new ApiValidationException(TOKEN_MISSING_L);
        }

        PropertySearcherChangeEmail changeEmail = changeEmailRepository.findByToken(token);

        if (changeEmail == null) {
            throw new ApiValidationException(CHANGE_EMAIL_MISSING_L);
        }

        PropertySearcherUser user = changeEmail.getUser();
        if (user == null) {
            throw new ApiValidationException(USER_MISSING_L);
        }

        List<UserRepresentation> keyCloakUsers = keycloakService.searchUser(user.getEmail());
        if (keyCloakUsers.size() != 1) {
            log.error("Found multiple/none user(s) for " + user.getEmail() + " [" + keyCloakUsers.size() + "]");
            throw new ImmomioRuntimeException(MORE_THAN_ONE_KEYCLOAK_USER_L);
        }

        String oldEmail = user.getEmail();
        String email = changeEmail.getEmail();

        user.setEmail(email);
        List<PropertySearcherUserProfile> userProfiles = user.getProfiles();
        userProfiles.forEach(userProfile -> userProfile.setEmail(email));

        keycloakService.changeEmail(keyCloakUsers.get(0), email);
        userProfileRepository.saveAll(userProfiles);
        user = userRepository.save(user);

        sendEmailChangedNotification(user.getMainProfile(), oldEmail);
    }

    public void sendEmailChangeNotification(PropertySearcherChangeEmail changeEmail, PropertySearcherUserProfile userProfile) {
        Map<String, Object> model = createChangeEmailModel(changeEmail, userProfile);
        mailSender.send(userProfile.getEmail(), userProfile, MailTemplate.NEW_EMAIL, EMAIL_CHANGE_SUBJECT_KEY, model);
    }

    public void sendEmailChangedNotification(PropertySearcherUserProfile userProfile, String email) {
        Map<String, Object> model = createUserModel(userProfile);
        mailSender.send(email, userProfile, MailTemplate.NEW_EMAIL_CHANGED, EMAIL_CHANGED_SUBJECT_KEY, model);
    }

    private PropertySearcherChangeEmail createPropertySearcherChangeEmail(PropertySearcherUser user, ChangeEmailBean email) {
        PropertySearcherChangeEmail changeEmail = new PropertySearcherChangeEmail();

        changeEmail.setUser(user);
        changeEmail.setEmail(email.getEmail());
        changeEmail.setToken(UUID.randomUUID().toString());

        return changeEmail;
    }

    private Map<String, Object> createChangeEmailModel(PropertySearcherChangeEmail changeEmail, PropertySearcherUserProfile userProfile) {
        Map<String, Object> model = createUserModel(userProfile);
        model.put(ModelParams.MODEL_TOKEN, changeEmail.getToken());
        model.put(ModelParams.MODEL_IGNORE_DISCLAIMER, true);
        model.put(ModelParams.MODEL_NEW_EMAIL, changeEmail.getEmail());

        return model;
    }

    private Map<String, Object> createUserModel(PropertySearcherUserProfile userProfile) {
        Map<String, Object> model = new HashMap<>();
        model.put(ModelParams.MODEL_USER, new PropertySearcherUserProfileEmailBean(userProfile));
        model.put(ModelParams.MODEL_CUSTOMER, new PropertySearcherCustomerBean(userProfile.getUser().getCustomer()));
        model.put(ModelParams.MODEL_USER_PROFILE, userProfile.getData());

        return model;
    }
}
