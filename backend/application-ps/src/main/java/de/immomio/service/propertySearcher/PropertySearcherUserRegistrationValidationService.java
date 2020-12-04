package de.immomio.service.propertySearcher;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.propertysearcher.bean.user.guest.PropertySearcherGuestUserRegisterBean;
import de.immomio.propertysearcher.bean.PropertySearcherRegisterBean;
import de.immomio.utils.EmailAddressUtils;
import org.springframework.stereotype.Service;

@Service
public class PropertySearcherUserRegistrationValidationService {

    private static final String REGISTER_BEAN_IS_NULL_L = "REGISTER_BEAN_IS_NULL_L";
    private static final String PASSWORD_IS_EMPTY_L = "PASSWORD_IS_EMPTY_L";
    private static final String MAIL_IS_EMPTY_L = "MAIL_IS_EMPTY_L";
    private static final String INVALID_EMAIL_ADDRESS_L = "INVALID_EMAIL_ADDRESS_L";

    public void registerValidation(PropertySearcherRegisterBean registerBean) {
        if (registerBean == null) {
            throw new ApiValidationException(REGISTER_BEAN_IS_NULL_L);
        }
        if (registerBean.getEmail() == null || registerBean.getEmail().trim().isEmpty()) {
            throw new ApiValidationException(MAIL_IS_EMPTY_L);
        }
        if (EmailAddressUtils.isInvalid(registerBean.getEmail())) {
            throw new ApiValidationException(INVALID_EMAIL_ADDRESS_L);
        }

        validateSocialLoginPassword(registerBean.isSocialLogin(), registerBean.getPassword());
    }

    public void registerValidation(PropertySearcherGuestUserRegisterBean registerBean) {
        if (registerBean == null) {
            throw new ApiValidationException(REGISTER_BEAN_IS_NULL_L);
        }

        validateSocialLoginPassword(registerBean.isSocialLogin(), registerBean.getPassword());
    }

    private void validateSocialLoginPassword(boolean isSocialLogin, String password) {
        if (!isSocialLogin) {
            if (password == null
                    || password.isEmpty()) {
                throw new ApiValidationException(PASSWORD_IS_EMPTY_L);
            }
        }
    }
}
