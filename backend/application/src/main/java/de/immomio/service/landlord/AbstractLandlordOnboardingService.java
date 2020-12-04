package de.immomio.service.landlord;

import de.immomio.beans.RegisterResultBean;
import de.immomio.beans.landlord.CustomerUserBean;
import de.immomio.beans.landlord.FtpAccessBean;
import de.immomio.beans.landlord.LandlordCustomerRegisterBean;
import de.immomio.beans.landlord.LandlordRegisterBean;
import de.immomio.common.ErrorCode;
import de.immomio.config.DefaultBrandingThemeConfiguration;
import de.immomio.data.base.bean.customer.PaymentMethod;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.base.type.customer.PaymentMethodType;
import de.immomio.data.landlord.bean.customer.settings.DigitalContractCustomerSettings;
import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBrandingThemes;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.ftpaccess.FtpAccess;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.addonproduct.LandlordCustomerAddonProduct;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserBean;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.landlord.entity.user.profile.LandlordUserProfile;
import de.immomio.model.abstractrepository.product.BaseAbstractCustomerAddonProductRepository;
import de.immomio.model.abstractrepository.product.BaseAbstractCustomerProductRepository;
import de.immomio.model.repository.core.landlord.product.BaseLandlordProductRepository;
import de.immomio.model.repository.core.landlord.product.addon.BaseAddonProductRepository;
import de.immomio.service.AbstractOnboardingService;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractLandlordOnboardingService<
        CPR extends BaseAbstractCustomerProductRepository<LandlordCustomerProduct, LandlordCustomer>,
        CAPR extends BaseAbstractCustomerAddonProductRepository<LandlordCustomerAddonProduct>,
        PR extends BaseLandlordProductRepository,
        APR extends BaseAddonProductRepository>
        extends AbstractOnboardingService<LandlordCustomer, LandlordUser, LandlordRegisterBean, CustomerUserBean> {

    private DefaultBrandingThemeConfiguration defaultBrandingThemeConfiguration;
    private CPR customerProductRepository;
    private CAPR customerAddonProductRepository;
    private PR productRepository;
    private APR addonProductRepository;


    public AbstractLandlordOnboardingService(DefaultBrandingThemeConfiguration defaultBrandingThemeConfiguration,
            CPR customerProductRepository,
            CAPR customerAddonProductRepository,
            PR productRepository,
            APR addonProductRepository) {
        this.defaultBrandingThemeConfiguration = defaultBrandingThemeConfiguration;
        this.customerProductRepository = customerProductRepository;
        this.customerAddonProductRepository = customerAddonProductRepository;
        this.productRepository = productRepository;
        this.addonProductRepository = addonProductRepository;
    }

    @Override
    protected void mapCustomer(LandlordCustomer customer, LandlordRegisterBean uBean) {
        final LandlordCustomerRegisterBean cBean = uBean.getCustomer();

        customer.setAddress(cBean.getAddress());
        customer.setCustomerType(cBean.getCustomerType());
        customer.setLocation(cBean.getLocation());
        customer.setManagementUnits(cBean.getManagementUnits());
        customer.setDescription(cBean.getDescription());
        customer.setName(cBean.getName());

        customer.setPaymentMethods(Collections.singletonList(
                new PaymentMethod(
                        cBean.getPaymentMethod() != null ? cBean.getPaymentMethod() : PaymentMethodType.DEFAULT,
                        true
                )));
        if (StringUtils.isEmpty(cBean.getInvoiceEmail())) {
            customer.setInvoiceEmail(uBean.getEmail().toLowerCase());
        } else {
            customer.setInvoiceEmail(cBean.getInvoiceEmail().toLowerCase());
        }

        if (cBean.getPreferences() != null) {
            customer.setPreferences(cBean.getPreferences());
        }

        if (cBean.getFtpAccess() != null) {
            FtpAccessBean cFtpAccess = cBean.getFtpAccess();
            if (StringUtils.isNotEmpty(cFtpAccess.getUserPassword()) &&
                    StringUtils.isNotEmpty(cFtpAccess.getHomeDirectory())) {
                FtpAccess ftpAccess = new FtpAccess();
                ftpAccess.setUserPassword(cFtpAccess.getUserPassword());
                ftpAccess.setHomeDirectory(cFtpAccess.getHomeDirectory());
                customer.setFtpAccesses(Collections.singletonList(ftpAccess));
            }
        }
        customer.setCustomerSize(cBean.getCustomerSize());
        if (cBean.getPriceMultiplier() != null) {
            customer.setPriceMultiplier(cBean.getPriceMultiplier());
        } else {
            customer.setPriceMultiplier(cBean.getCustomerSize().getPriceMultiplier());
        }
    }

    @Override
    protected void mapUser(LandlordCustomer customer, LandlordUser user, LandlordRegisterBean uBean) {
        user.setCustomer(customer);
        user.setEmail(uBean.getEmail().toLowerCase());
        user.setUsertype(LandlordUsertype.COMPANYADMIN);
        user.setEnabled(true);

        LandlordUserProfile profile = new LandlordUserProfile();

        profile.setFirstname(uBean.getFirstName());
        profile.setName(uBean.getLastName());
        profile.setPhone(uBean.getPhone());

        user.setProfile(profile);
    }

    @Override
    protected void mapUser(LandlordCustomer customer, LandlordUser user, CustomerUserBean cBean) {
        user.setCustomer(customer);
        user.setEmail(cBean.getEmail().toLowerCase());
        user.setUsertype(cBean.getUsertype());

        LandlordUserProfile profile = new LandlordUserProfile();

        profile.setFirstname(cBean.getFirstname());
        profile.setName(cBean.getName());
        profile.setGender(cBean.getGender());
        profile.setTitle(cBean.getTitle());
        profile.setPhone(cBean.getPhone());
        profile.setPortrait(cBean.getPortrait());

        user.setProfile(profile);
    }

    @Override
    protected boolean isValid(CustomerUserBean cBean, RegisterResultBean<LandlordUser> rrb) {
        boolean valid = true;
        if (cBean == null) {
            addUserError(ErrorCode.ERROR_NO_USER_BEAN, rrb);
            valid = false;
        } else if (cBean.getEmail() == null || cBean.getEmail().trim().isEmpty()) {
            addUserError(ErrorCode.ERROR_NO_EMAIL, rrb);
            valid = false;
        } else if (cBean.getUsertype() == null) {
            addUserError(ErrorCode.ERROR_NO_USER_TYPE, rrb);
            valid = false;
        }
        return valid;
    }

    protected void saveOrElseDelete(LandlordUser user, LandlordCustomer customer, String email) {
        try {
            save(user);
        } catch (Exception e) {
            removeInKeycloak(email);
            delete(customer);
            throw e;
        }
    }

    protected void saveCustomerSettings(LandlordCustomer customer, String themeUrl) {
        LandlordCustomerSettings customerSettings = new LandlordCustomerSettings();

        customerSettings.setThemeUrl(themeUrl);
        customerSettings.setCustomer(customer);
        customerSettings.setDeleteDkLevelAfterRenting(true);
        populateDefaultBrandingTheme(customerSettings);
        DigitalContractCustomerSettings contractCustomerSettings = DigitalContractCustomerSettings.builder()
                .continueContractWhenNotVisitedFlat(true)
                .contractDefaultSignerType(DigitalContractSignerType.TENANT)
                .build();
        contractCustomerSettings.setContinueContractWhenNotVisitedFlat(true);
        contractCustomerSettings.setContractDefaultSignerType(DigitalContractSignerType.TENANT);
        customerSettings.setContractCustomerSettings(contractCustomerSettings);
        save(customerSettings);
    }

    private void populateDefaultBrandingTheme(LandlordCustomerSettings customerSettings) {
        LandlordCustomerBrandingThemes brandingThemes = new LandlordCustomerBrandingThemes();
        brandingThemes.add(defaultBrandingThemeConfiguration);
        customerSettings.setBrandingThemes(brandingThemes);
    }

    protected Map<String, Object> toMap(LandlordUser user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user", new LandlordUserBean(user));
        userMap.put("userprofile", user.getProfile());
        userMap.put("customer", user.getCustomer());
        userMap.put("ignoredisclaimer", true);
        return userMap;
    }

    @Override
    protected final RegisterResultBean<LandlordUser> initResult() {
        return new RegisterResultBean<>();
    }

    @Override
    protected LandlordUser initUser() {
        return new LandlordUser();
    }

    @Override
    protected LandlordCustomer initCustomer() {
        return new LandlordCustomer();
    }

    @Override
    protected LandlordUser findByEmail(CustomerUserBean userBean) {
        return findByEmail(userBean.getEmail().toLowerCase());
    }

    @Override
    protected LandlordCustomer save(LandlordCustomer customer, LandlordUser user, LandlordRegisterBean uBean) {
        return save(customer, user, uBean.getEmail());
    }

    protected abstract LandlordCustomer save(LandlordCustomer customer, LandlordUser user, String email);

    protected abstract void save(LandlordUser user);

    protected abstract void save(LandlordCustomerSettings settings);

    public abstract LandlordUser findByEmail(String email);

}
