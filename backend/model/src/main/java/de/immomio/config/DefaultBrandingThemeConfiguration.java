package de.immomio.config;

import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBrandingTheme;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "default.theme")
public class DefaultBrandingThemeConfiguration extends LandlordCustomerBrandingTheme {
    private static final long serialVersionUID = 7192478344172120595L;
}
