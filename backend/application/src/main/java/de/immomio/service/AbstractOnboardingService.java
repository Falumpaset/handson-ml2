package de.immomio.service;

import de.immomio.beans.AbstractCustomerUserBean;
import de.immomio.beans.AbstractRegisterUserBean;
import de.immomio.beans.RegisterResultBean;
import de.immomio.common.ErrorCode;
import de.immomio.data.base.entity.customer.AbstractCustomer;
import de.immomio.data.base.entity.customer.user.AbstractUser;
import org.springframework.validation.ObjectError;

public abstract class AbstractOnboardingService<
        C extends AbstractCustomer<?, ?>,
        U extends AbstractUser,
        RUB extends AbstractRegisterUserBean<?>,
        CUB extends AbstractCustomerUserBean
        > {

    private static final String REGISTER_USER_ERROR_KEY = "registerUser";

    private static final String ADD_USER_ERROR_KEY = "addUser";

    public RegisterResultBean<U> register(RUB uBean) {
        RegisterResultBean<U> rrb = initResult();

        if (!isValid(uBean, rrb)) {
            return rrb;
        }

        C customer = initCustomer();
        U user = initUser();

        if (uBean.getCustomer() != null) {
            mapCustomer(customer, uBean);
        }
        mapUser(customer, user, uBean);

        if (findByEmail(uBean.getEmail()) != null || !createInKeycloak(uBean)) {
            registerError(ErrorCode.ERROR_EMAIL_ALREADY_EXISTS, rrb);
            return rrb;
        }

        save(customer, user, uBean);

        rrb.setRegisteredUser(user);

        return rrb;
    }

    private boolean isValid(RUB uBean, RegisterResultBean<U> rrb) {
        boolean valid = true;
        if (uBean == null) {
            registerError(ErrorCode.ERROR_NO_REGISTER_DATA, rrb);
            valid = false;
        } else if (uBean.getEmail() == null || uBean.getEmail().trim().isEmpty()) {
            registerError(ErrorCode.ERROR_NO_EMAIL, rrb);
            valid = false;
        } else if (uBean.getPassword() == null || uBean.getConfirmPassword() == null
                || !uBean.getPassword().equals(uBean.getConfirmPassword())) {
            registerError(ErrorCode.ERROR_PASSWORD_NOT_EQUAL, rrb);
            valid = false;
        }
        return valid;
    }


    protected abstract boolean isValid(CUB cBean, RegisterResultBean<U> rrb);

    protected abstract boolean createInKeycloak(RUB uBean);

    public abstract boolean createInKeycloak(CUB uBean, boolean enabled);

    protected abstract void removeInKeycloak(String email);

    private void registerError(String errorKey, RegisterResultBean result) {
        addError(REGISTER_USER_ERROR_KEY, errorKey, result);
    }

    protected void addUserError(String errorKey, RegisterResultBean resultBean) {
        addError(ADD_USER_ERROR_KEY, errorKey, resultBean);
    }

    private void addError(String ident, String errorKey, RegisterResultBean result) {
        result.setError(new ObjectError(ident, errorKey));
    }

    protected abstract C save(C customer, U user, RUB uBean);

    protected abstract void delete(C customer);

    protected abstract void delete(U user);

    protected abstract void notifyChangePassword(U user);

    protected abstract void mapCustomer(C customer, RUB uBean);

    protected abstract void mapUser(C customer, U user, RUB uBean);

    protected abstract void mapUser(C customer, U user, CUB cBean);

    protected abstract RegisterResultBean<U> initResult();

    protected abstract U initUser();

    protected abstract C initCustomer();

    protected abstract U findByEmail(CUB userBean);

    public abstract U findByEmail(String email);
}
