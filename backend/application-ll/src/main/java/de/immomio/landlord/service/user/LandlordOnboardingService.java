package de.immomio.landlord.service.user;

import de.immomio.beans.landlord.CustomerUserBean;
import de.immomio.beans.landlord.LandlordRegisterBean;
import de.immomio.config.DefaultBrandingThemeConfiguration;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.landlord.service.branding.BrandingService;
import de.immomio.model.repository.landlord.customer.LandlordCustomerRepository;
import de.immomio.model.repository.landlord.customer.LandlordCustomerSettingsRepository;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import de.immomio.model.repository.landlord.product.LandlordProductRepository;
import de.immomio.model.repository.landlord.product.addon.LandlordAddonProductRepository;
import de.immomio.model.repository.landlord.product.customer.LandlordCustomerProductRepository;
import de.immomio.model.repository.landlord.product.customer.addon.LandlordCustomerAddonProductRepository;
import de.immomio.security.service.KeycloakService;
import de.immomio.service.landlord.AbstractLandlordOnboardingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class LandlordOnboardingService extends AbstractLandlordOnboardingService<LandlordCustomerProductRepository, LandlordCustomerAddonProductRepository, LandlordProductRepository, LandlordAddonProductRepository> {

    private static final String REGISTER_SUBJECT = "register.subject";
    private static final String IMMOMIO = "immomio";

    private final KeycloakService keycloakService;

    private final LandlordUserRepository userRepository;

    private final LandlordCustomerRepository customerRepository;

    private final LandlordChangePasswordService changePasswordService;

    private final LandlordCustomerSettingsRepository landlordCustomerSettingsRepository;

    private final BrandingService brandingService;


    @Autowired
    public LandlordOnboardingService(
            KeycloakService keycloakService,
            LandlordUserRepository userRepository,
            LandlordCustomerRepository customerRepository,
            LandlordChangePasswordService changePasswordService,
            LandlordCustomerSettingsRepository landlordCustomerSettingsRepository,
            BrandingService brandingService,
            LandlordCustomerProductRepository customerProductRepository,
            LandlordCustomerAddonProductRepository customerAddonProductRepository,
            DefaultBrandingThemeConfiguration defaultBrandingThemeConfiguration,
            LandlordProductRepository productRepository,
            LandlordAddonProductRepository addonProductRepository
    ) {

        super(defaultBrandingThemeConfiguration, customerProductRepository, customerAddonProductRepository, productRepository, addonProductRepository);
        this.keycloakService = keycloakService;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.changePasswordService = changePasswordService;
        this.landlordCustomerSettingsRepository = landlordCustomerSettingsRepository;
        this.brandingService = brandingService;
    }

    @Override
    protected boolean createInKeycloak(LandlordRegisterBean uBean) {
        return keycloakService.createUser(
                IMMOMIO,
                uBean.getEmail(),
                uBean.getEmail(),
                uBean.getFirstName(),
                uBean.getLastName(),
                uBean.getPassword()
        );
    }

    @Override
    public boolean createInKeycloak(CustomerUserBean cBean, boolean enabled) {
        return keycloakService.createUser(IMMOMIO, cBean.getEmail(), cBean.getEmail(), cBean.getFirstname(),
                cBean.getName(), enabled);
    }

    @Override
    protected LandlordCustomer save(LandlordCustomer customer, LandlordUser user, String email) {
        LandlordCustomer landlordCustomer;
        try {
            landlordCustomer = customerRepository.save(customer);
        } catch (Exception e) {
            removeInKeycloak(email);
            throw e;
        }

        saveOrElseDelete(user, customer, email);

        String brandingUrl = generateBrandingUrl(customer);
        saveCustomerSettings(customer, brandingUrl);
        return landlordCustomer;
    }

    @Override
    protected void save(LandlordUser user) {
        userRepository.customSave(user);
    }

    @Override
    protected void save(LandlordCustomerSettings settings) {
        landlordCustomerSettingsRepository.customSave(settings);
    }

    @Override
    protected void delete(LandlordCustomer customer) {
        customerRepository.customDelete(customer);
    }

    @Override
    protected void delete(LandlordUser user) {
        userRepository.customDelete(user);
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

    private String generateBrandingUrl(LandlordCustomer customer) {
        try {
            return brandingService.generateBrandingUrl(customer.getId());
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }

        return null;
    }
}
