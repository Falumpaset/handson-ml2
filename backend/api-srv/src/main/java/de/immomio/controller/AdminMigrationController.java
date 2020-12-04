package de.immomio.controller;

import de.immomio.config.DefaultBrandingThemeConfiguration;
import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBrandingThemes;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.model.repository.service.landlord.customer.LandlordCustomerSettingsRepository;
import de.immomio.service.AppointmentMigrationService;
import de.immomio.service.landlord.branding.BrandingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/migration")
public class AdminMigrationController {

    private final LandlordCustomerSettingsRepository landlordCustomerSettingsRepository;

    private final BrandingService brandingService;

    private final AppointmentMigrationService appointmentMigrationService;

    @Autowired
    private DefaultBrandingThemeConfiguration defaultBrandingThemeConfiguration;

    @Autowired
    public AdminMigrationController(
            LandlordCustomerSettingsRepository landlordCustomerSettingsRepository,
            BrandingService brandingService,
            AppointmentMigrationService appointmentMigrationService) {
        this.landlordCustomerSettingsRepository = landlordCustomerSettingsRepository;
        this.brandingService = brandingService;
        this.appointmentMigrationService = appointmentMigrationService;
    }

    @PostMapping("/set-default-brand-theme")
    public ResponseEntity setDefaultBrandTheme() {
        List<LandlordCustomerSettings> customerSettings = landlordCustomerSettingsRepository.findAllByThemeUrlIsNull();
        customerSettings.forEach(customerSetting -> {
            try {
                String themeUrl = brandingService.generateBrandingUrl(customerSetting.getCustomer().getId());
                customerSetting.setThemeUrl(themeUrl);

                LandlordCustomerBrandingThemes brandingThemes = customerSetting.getBrandingThemes();
                if (brandingThemes == null || brandingThemes.isEmpty()) {
                    brandingThemes = new LandlordCustomerBrandingThemes();
                    brandingThemes.add(defaultBrandingThemeConfiguration);
                    customerSetting.setBrandingThemes(brandingThemes);
                }
            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
            }
        });

        landlordCustomerSettingsRepository.saveAll(customerSettings);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/appointments")
    public void migrateAppointments() {
        appointmentMigrationService.migrateAppointmentCreators();
    }
}
