package de.immomio.mail.configurator;

import de.immomio.config.DefaultBrandingThemeConfiguration;
import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBrandingTheme;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.mail.ModelParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Fabian Beck
 */

@Slf4j
@Service
public class MailBrandingConfigurator {

    private final DefaultBrandingThemeConfiguration defaultBrandingThemeConfiguration;


    @Autowired
    public MailBrandingConfigurator(DefaultBrandingThemeConfiguration defaultBrandingThemeConfiguration) {
        this.defaultBrandingThemeConfiguration = defaultBrandingThemeConfiguration;
    }

    public void applyBrandingThemeIfExist(LandlordCustomer customer, Map<String, Object> data) {
        LandlordCustomerBrandingTheme theme = defaultBrandingThemeConfiguration;

        if (data.containsKey(ModelParams.MODEL_ALLOW_BRANDING) && Boolean.parseBoolean(String.valueOf(data.get(
                ModelParams.MODEL_ALLOW_BRANDING))) && customer != null) {
            LandlordCustomerSettings customerSettings = customer.getCustomerSettings();
            if (customerSettings != null) {
                theme = customerSettings.getActiveTheme().orElse(theme);
            }
        }

        data.put(ModelParams.MODEL_BRANDING_THEME, theme);
    }
}
