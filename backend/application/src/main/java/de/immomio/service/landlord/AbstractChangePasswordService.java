package de.immomio.service.landlord;

import de.immomio.beans.ChangePasswordBean;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Objects;

/**
 * @author Maik Kingma
 */

public abstract class AbstractChangePasswordService {

    private static final String NEW_AGENT_CHANGE_PASSWORD_SUBJECT_KEY = "commercial.new.agent.subject";
    private static final String PASSWORD_RESET_SUBJECT_KEY = "password.reset.subject";

    private static final String ERROR_PARAMETER_NULL = "errors.parameterisnull";
    private static final String ERROR_PASSWORDS_NOT_ALIKE = "errors.newpasswordnotequals";
    private static final String ERROR_OBJECT = "changePassword";


    protected boolean changePasswordCheck(ChangePasswordBean changePassword, BindingResult result) {
        if (changePassword == null) {
            Objects.requireNonNull(result)
                    .addError(new ObjectError(ERROR_OBJECT, ERROR_PARAMETER_NULL));
            return true;
        }
        if (!changePassword.checkNewPasswordConfirmed()) {
            result.addError(new ObjectError(ERROR_OBJECT, ERROR_PASSWORDS_NOT_ALIKE));
            return true;
        }
        return false;
    }

    public static String getNewAgentChangePasswordSubjectKey() {
        return NEW_AGENT_CHANGE_PASSWORD_SUBJECT_KEY;
    }

    public static String getPasswordResetSubjectKey() {
        return PASSWORD_RESET_SUBJECT_KEY;
    }
}
