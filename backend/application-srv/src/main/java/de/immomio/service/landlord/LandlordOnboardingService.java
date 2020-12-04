package de.immomio.service.landlord;

import de.immomio.beans.landlord.CustomerUserBean;
import de.immomio.beans.landlord.LandlordRegisterBean;
import de.immomio.config.DefaultBrandingThemeConfiguration;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.model.repository.service.landlord.customer.LandlordCustomerRepository;
import de.immomio.model.repository.service.landlord.customer.LandlordCustomerSettingsRepository;
import de.immomio.model.repository.service.landlord.customer.user.LandlordUserRepository;
import de.immomio.model.repository.service.landlord.product.LandlordProductRepository;
import de.immomio.model.repository.service.landlord.product.addon.AddonProductRepository;
import de.immomio.model.repository.service.landlord.product.customer.LandlordCustomerProductRepository;
import de.immomio.model.repository.service.landlord.product.customer.addon.LandlordCustomerAddonProductRepository;
import de.immomio.security.service.KeycloakService;
import de.immomio.service.landlord.branding.BrandingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Slf4j
@Service
public final class LandlordOnboardingService extends AbstractLandlordOnboardingService<LandlordCustomerProductRepository, LandlordCustomerAddonProductRepository, LandlordProductRepository, AddonProductRepository> {

    private static final String REGISTER_SUBJECT = "register.subject";
    private static final String CUSTOMER_HAS_NO_ACTIVE_PRODUCT = "CUSTOMER_HAS_NO_ACTIVE_PRODUCT_L";

    private final KeycloakService keycloakService;

    private final LandlordUserRepository userRepository;

    private final LandlordCustomerRepository customerRepository;

    private final LandlordChangePasswordService changePasswordService;

    private final LandlordCustomerSettingsRepository customerSettingsRepository;

    private LandlordCustomerProductRepository customerProductRepository;

    private final BrandingService brandingService;

    @Autowired
    public LandlordOnboardingService(
            KeycloakService keycloakService,
            LandlordUserRepository userRepository,
            LandlordCustomerRepository customerRepository,
            LandlordChangePasswordService changePasswordService,
            LandlordCustomerSettingsRepository customerSettingsRepository,
            LandlordCustomerProductRepository customerProductRepository,
            BrandingService brandingService,
            LandlordCustomerAddonProductRepository customerAddonProductRepository,
            DefaultBrandingThemeConfiguration defaultBrandingThemeConfiguration,
            LandlordProductRepository productRepository,
            AddonProductRepository addonProductRepository
    ) {

        super(defaultBrandingThemeConfiguration, customerProductRepository, customerAddonProductRepository, productRepository, addonProductRepository);
        this.keycloakService = keycloakService;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.changePasswordService = changePasswordService;
        this.customerSettingsRepository = customerSettingsRepository;
        this.customerProductRepository = customerProductRepository;
        this.brandingService = brandingService;
    }

    @Override
    protected boolean createInKeycloak(LandlordRegisterBean uBean) {
        return keycloakService.createUser("immomio", uBean.getEmail(), uBean.getEmail(),
                uBean.getFirstName(), uBean.getLastName(), uBean.getPassword());
    }

    @Override
    public boolean createInKeycloak(CustomerUserBean cBean, boolean enabled) {
        return keycloakService.createUser("immomio", cBean.getEmail(), cBean.getEmail(),
                cBean.getFirstname(), cBean.getName());
    }

    @Override
    protected LandlordCustomer save(LandlordCustomer customer, LandlordUser user, String email) {
        LandlordCustomer landlordCustomer;
        try {
            landlordCustomer = customerRepository.save(customer);
        } catch (Exception e) {
            keycloakService.removeUser(email);
            throw e;
        }

        saveOrElseDelete(user, customer, email);

        String brandingUrl = generateBrandingUrl(customer);
        saveCustomerSettings(customer, brandingUrl);
        return landlordCustomer;
    }

    @Override
    protected void save(LandlordUser user) {
        userRepository.save(user);
    }

    @Override
    protected void save(LandlordCustomerSettings settings) {
        customerSettingsRepository.save(settings);
    }

    @Override
    protected void delete(LandlordCustomer customer) {
        customerRepository.delete(customer);
    }

    @Override
    protected void delete(LandlordUser user) {
        userRepository.delete(user);
    }

    @Override
    protected void removeInKeycloak(String email) {
        keycloakService.removeUser(email);
    }


    @Override
    protected void notifyChangePassword(LandlordUser user) {
        changePasswordService.newUser(user);
    }

    @Override
    public LandlordUser findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    public LandlordCustomerProduct deactivateActiveProduct(LandlordCustomer customer, Date dueDate) {

        LandlordCustomerProduct activeProduct = customer.getActiveProduct();

        if (activeProduct == null) {
            throw new ApiValidationException(CUSTOMER_HAS_NO_ACTIVE_PRODUCT);
        }
        if (dueDate == null) {
            dueDate = activeProduct.getDueDate();
        }
        activeProduct.setRenew(false);
        activeProduct.setDueDate(dueDate);
        return customerProductRepository.save(activeProduct);
    }

    private String generateBrandingUrl(LandlordCustomer customer) {
        try {
            return brandingService.generateBrandingUrl(customer.getId());
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }

        return null;
    }
}
