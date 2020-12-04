package de.immomio.landlord.service.user;

import de.immomio.beans.ChangePasswordBean;
import de.immomio.beans.ResetPasswordConfirmBean;
import de.immomio.constants.exceptions.UserNotFoundException;
import de.immomio.data.base.entity.customer.user.AbstractUser;
import de.immomio.data.landlord.bean.customer.LandlordCustomerBean;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserBean;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.landlord.entity.user.changepassword.ChangePassword;
import de.immomio.mail.ModelParams;
import de.immomio.mail.configurator.LandlordMailConfigurator;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import de.immomio.model.repository.landlord.customer.user.changepassword.LandlordChangePasswordRepository;
import de.immomio.security.service.KeycloakService;
import de.immomio.service.landlord.AbstractChangePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class LandlordChangePasswordService extends AbstractChangePasswordService {

    private static final String PASSWORD_CHANGED_SUBJECT = "password.changed.subject";
    private static final String ERROR_PASSWORD_RESET_EMAIL_NOT_FOUND = "error.password.reset.emailNotFound";
    private static final String CHANGE_PASSWORD = "changePassword";
    private static final String ERRORS_TOKEN_NOT_FOUND = "errors.tokennotfound";
    private static final String ERRORS_PARAMETER_IS_NULL = "errors.parameterisnull";
    private static final String ERRORS_NEWPASSWORD_NOT_EQUALS = "errors.newpasswordnotequals";
    private static final String PRIVATE_LANDLORD = "Privater Vermieter";

    private final LandlordMailSender mailSender;

    private final KeycloakService keycloakService;

    private final LandlordUserRepository userRepository;

    private final LandlordMailConfigurator mailConfigurator;

    private final LandlordChangePasswordRepository changePasswordRepository;

    @Autowired
    public LandlordChangePasswordService(
            LandlordMailSender mailSender,
            KeycloakService keycloakService,
            LandlordUserRepository userRepository,
            LandlordMailConfigurator mailConfigurator,
            LandlordChangePasswordRepository changePasswordRepository
    ) {
        this.mailSender = mailSender;
        this.keycloakService = keycloakService;
        this.userRepository = userRepository;
        this.mailConfigurator = mailConfigurator;
        this.changePasswordRepository = changePasswordRepository;
    }

    public ChangePassword resetPassword(String email) throws UserNotFoundException {
        LandlordUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException(ERROR_PASSWORD_RESET_EMAIL_NOT_FOUND);
        }

        return resetPassword(user);
    }

    public ChangePassword resetPassword(LandlordUser user) {
        ChangePassword changePassword = createChangePassword(user);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put(ModelParams.MODEL_USER, new LandlordUserBean(user));
        userMap.put(ModelParams.MODEL_TOKEN, changePassword.getToken());
        userMap.put(ModelParams.RETURN_URL, mailConfigurator.buildAppUrl());

        mailSender.send(user, MailTemplate.RESET_PASSWORD, getPasswordResetSubjectKey(), userMap);
        return changePassword;
    }

    public void newUser(LandlordUser user) {
        if (user == null) {
            return;
        }

        ChangePassword changePassword = createChangePassword(user);

        String defaultName = user.getCustomer().getCompanyAdmins()
                .stream()
                .findFirst()
                .map(LandlordUser::fullName)
                .orElse(PRIVATE_LANDLORD);

        Map<String, Object> model = new HashMap<>();
        model.put(ModelParams.MODEL_USER, new LandlordUserBean(user));
        model.put(ModelParams.MODEL_CUSTOMER, new LandlordCustomerBean(user.getCustomer(), defaultName));
        model.put(ModelParams.MODEL_TOKEN, changePassword.getToken());
        model.put(ModelParams.MODEL_IGNORE_DISCLAIMER, true);

        MailTemplate emailTemplate;
        if (user.getUsertype() == LandlordUsertype.COMPANYADMIN) {
            emailTemplate = MailTemplate.COMMERCIAL_NEW_ADMIN;
        } else if (user.getUsertype() == LandlordUsertype.EMPLOYEE) {
            emailTemplate = MailTemplate.COMMERCIAL_NEW_AGENT;
        } else {
            emailTemplate = MailTemplate.COMMERCIAL_NEW_PROPERTY_MANAGER;
        }

        mailSender.send(
                user.getEmail(),
                user,
                user.getCustomer(),
                emailTemplate,
                getNewAgentChangePasswordSubjectKey(),
                model);
    }

    private ChangePassword createChangePassword(LandlordUser user) {
        ChangePassword changePassword = new ChangePassword();

        changePassword.setToken(UUID.randomUUID().toString());
        changePassword.setUser(user);
        changePassword = changePasswordRepository.customSave(changePassword);

        return changePassword;
    }

    public boolean changePassword(ResetPasswordConfirmBean changePassword, BindingResult result) {
        if (changePasswordCheck(changePassword, result)) {
            return false;
        }

        ChangePassword cp = changePasswordRepository.findByToken(changePassword.getToken());

        if (cp == null) {
            result.addError(new ObjectError(CHANGE_PASSWORD, ERRORS_TOKEN_NOT_FOUND));
            return false;
        }

        return changePassword(cp.getUser(), changePassword.getPassword());
    }

    public boolean changePassword(LandlordUser user, ChangePasswordBean changePassword, BindingResult result) {
        if (result == null) {
            return false;
        }
        if (user == null || changePassword == null) {
            result.addError(new ObjectError(CHANGE_PASSWORD, ERRORS_PARAMETER_IS_NULL));
            return false;
        }
        if (!changePassword.checkNewPasswordConfirmed()) {
            result.addError(new ObjectError(CHANGE_PASSWORD, ERRORS_NEWPASSWORD_NOT_EQUALS));
            return false;
        }
        return changePassword(user, changePassword.getPassword());
    }

    private boolean changePassword(LandlordUser user, String password) {
        if (user == null || password == null || password.isEmpty()) {
            return false;
        }

        keycloakService.resetPassword(user.getEmail(), password, false);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put(ModelParams.MODEL_USER, new LandlordUserBean(user));

        mailSender.send(user, MailTemplate.CHANGED_PASSWORD, PASSWORD_CHANGED_SUBJECT, userMap);

        if (user.getEmailVerified() == null) {
            user.setEmailVerified(new Date());
            userRepository.save(user);
        }

        return true;
    }
}
