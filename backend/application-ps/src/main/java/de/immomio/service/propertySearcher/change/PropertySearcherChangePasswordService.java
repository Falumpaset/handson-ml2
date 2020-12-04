package de.immomio.service.propertySearcher.change;

import de.immomio.beans.ChangePasswordBean;
import de.immomio.beans.ResetPasswordConfirmBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherChangePassword;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileEmailBean;
import de.immomio.mail.ModelParams;
import de.immomio.mail.configurator.PropertySearcherMailConfigurator;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserRepository;
import de.immomio.model.repository.propertysearcher.user.change.password.PropertySearcherChangePasswordRepository;
import de.immomio.security.service.KeycloakService;
import de.immomio.service.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PropertySearcherChangePasswordService {

    public static final String ERROR_PASSWORD_RESET_EMAIL_NOT_FOUND = "error.password.reset.emailNotFound";
    private static final String ERROR_PARAMETER_IS_NULL = "errors.parameterisnull";
    private static final String ERROR_TOKEN_NOT_FOUND = "errors.tokennotfound";
    private static final String USER_NOT_FOUND = "USER_NOT_FOUND_L";
    private static final String ERROR_PASSWORD_NOT_MATCH = "errors.newpasswordnotequals";
    private static final String EMAIL_PASSWORD_RESET_SUBJECT_KEY = "password.reset.subject";
    private static final String EMAIL_PASSWORD_CHANGE_SUBJECT_KEY = "password.changed.subject";
    private static final String PASSWORD_IS_EMPTY_L = "PASSWORD_IS_EMPTY_L";
    private static final String PASSWORDS_DIFFERENT_L = "PASSWORDS_DIFFERENT_L";
    private final PropertySearcherMailSender mailSender;

    private final KeycloakService keycloakService;

    private final PropertySearcherUserRepository userRepository;

    private final PropertySearcherChangePasswordRepository changePasswordRepository;

    private final PropertySearcherMailConfigurator mailConfigurator;

    private final UserSecurityService userSecurityService;

    @Autowired
    public PropertySearcherChangePasswordService(PropertySearcherMailSender mailSender,
            KeycloakService keycloakService,
            PropertySearcherUserRepository userRepository,
            PropertySearcherChangePasswordRepository changePasswordRepository,
            PropertySearcherMailConfigurator mailConfigurator,
            UserSecurityService userSecurityService) {
        this.mailSender = mailSender;
        this.keycloakService = keycloakService;
        this.userRepository = userRepository;
        this.changePasswordRepository = changePasswordRepository;
        this.mailConfigurator = mailConfigurator;
        this.userSecurityService = userSecurityService;
    }

    public PropertySearcherChangePassword resetPassword(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }

        PropertySearcherUser user = userRepository.findByEmail(email);

        if (user == null || !user.isRegistered()) {
            throw new ApiValidationException(ERROR_PASSWORD_RESET_EMAIL_NOT_FOUND);
        }

        return resetPassword(user);
    }

    public PropertySearcherChangePassword resetPassword(PropertySearcherUser user) {
        PropertySearcherChangePassword changePassword = createChangePassword(user);
        Map<String, Object> model = getResetPasswordModel(user.getMainProfile(), changePassword.getToken());
        mailSender.send(user.getMainProfile(), MailTemplate.RESET_PASSWORD, EMAIL_PASSWORD_RESET_SUBJECT_KEY, model);

        return changePassword;
    }

    public boolean changePassword(ChangePasswordBean changePassword) {
        validateChangePasswordBean(changePassword);

        PropertySearcherUser user = userSecurityService.getPrincipalUser();
        if (user == null) {
            throw new ApiValidationException(USER_NOT_FOUND);
        }

        return updatePassword(user, changePassword.getPassword());
    }

    public boolean resetPassword(ResetPasswordConfirmBean changePassword) {
        validateChangePasswordBean(changePassword);

        PropertySearcherChangePassword cp = changePasswordRepository.findByToken(changePassword.getToken());
        if (cp == null || cp.getUser() == null) {
            throw new ApiValidationException(ERROR_TOKEN_NOT_FOUND);
        }

        return updatePassword(cp.getUser(), changePassword.getPassword());
    }

    private void validateChangePasswordBean(ChangePasswordBean changePassword) {
        if (changePassword == null) {
            throw new ApiValidationException(ERROR_PARAMETER_IS_NULL);
        }
        if (changePassword.getPassword() == null || changePassword.getPassword().isEmpty() || changePassword.getConfirmedPassword() == null || changePassword.getConfirmedPassword()
                .isEmpty()) {
            throw new ApiValidationException(PASSWORD_IS_EMPTY_L);
        }
        if (!changePassword.getPassword().equals(changePassword.getConfirmedPassword())) {
            throw new ApiValidationException(PASSWORDS_DIFFERENT_L);
        }
        if (!changePassword.checkNewPasswordConfirmed()) {
            throw new ApiValidationException(ERROR_PASSWORD_NOT_MATCH);
        }
    }

    private boolean updatePassword(PropertySearcherUser user, String password) {
        if (user == null) {
            throw new ApiValidationException(USER_NOT_FOUND);
        }
        if (password == null || password.isEmpty()) {
            throw new ApiValidationException(ERROR_PASSWORD_NOT_MATCH);
        }

        keycloakService.resetPassword(user.getEmail(), password, false);

        Map<String, Object> model = getChangePasswordModel(user.getMainProfile());
        mailSender.send(user.getMainProfile(), MailTemplate.CHANGED_PASSWORD, EMAIL_PASSWORD_CHANGE_SUBJECT_KEY, model);

        return true;
    }

    private PropertySearcherChangePassword createChangePassword(PropertySearcherUser user) {
        PropertySearcherChangePassword changePassword = new PropertySearcherChangePassword();

        changePassword.setToken(UUID.randomUUID().toString());
        changePassword.setUser(user);
        changePassword = changePasswordRepository.customSave(changePassword);

        return changePassword;
    }

    private Map<String, Object> getChangePasswordModel(PropertySearcherUserProfile userProfile) {
        Map<String, Object> model = new HashMap<>();
        model.put(ModelParams.MODEL_USER, new PropertySearcherUserProfileEmailBean(userProfile));

        return model;
    }

    private Map<String, Object> getResetPasswordModel(PropertySearcherUserProfile userProfile, String token) {
        Map<String, Object> model = new HashMap<>();
        model.put(ModelParams.MODEL_USER, new PropertySearcherUserProfileEmailBean(userProfile));
        model.put(ModelParams.MODEL_TOKEN, token);
        model.put(ModelParams.RETURN_URL, mailConfigurator.buildAppUrl());
        return model;
    }
}
