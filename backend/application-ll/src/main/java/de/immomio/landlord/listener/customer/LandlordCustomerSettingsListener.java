package de.immomio.landlord.listener.customer;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.landlord.service.branding.BrandingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RepositoryEventHandler
public class LandlordCustomerSettingsListener {

    private final BrandingService brandingService;

    @Autowired
    public LandlordCustomerSettingsListener(BrandingService brandingService) {
        this.brandingService = brandingService;
    }

    @HandleBeforeCreate
    @HandleBeforeSave
    public void beforeCreateOrSave(LandlordCustomerSettings landlordCustomerSettings) {
        try {
            LandlordCustomer customer = landlordCustomerSettings.getCustomer();
            if (customer.isBrandingAllowed() && landlordCustomerSettings.getThemeUrl() == null) {
                landlordCustomerSettings.setThemeUrl(brandingService.validateAndGenerateBrandingUrl());
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
