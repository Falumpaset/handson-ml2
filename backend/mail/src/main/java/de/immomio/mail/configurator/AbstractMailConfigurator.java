package de.immomio.mail.configurator;

import de.immomio.data.landlord.bean.customer.settings.LandlordCustomerMailConfig;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.Map;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
public abstract class AbstractMailConfigurator {

    private static final String LANGUANGE_KEY_DE = "de";
    private static final String COUNTRY_KEY_DE = "DE";
    private static final String SLASH = "/";
    private static final String DOT = ".";

    @Autowired
    protected LocationService locationService;

    @Value("${base.protocol}")
    protected String protocol;

    @Value("${base.host}")
    protected String host;

    @Value("${base.subdomainhost}")
    protected String subdomainhost;

    @Value("${base.landlord.subdomain}")
    protected String subdomain;

    @Value("${base.landlord.subdomainapi}")
    protected String subdomainApi;

    @Value("${base.protocol}")
    protected String tenantProtocol;

    @Value("${base.host}")
    protected String tenantHost;

    @Value("${base.subdomainhost}")
    protected String tenantSubdomainhost;

    @Value("${base.propertysearcher.subdomain}")
    protected String tenantSubdomain;

    @Value("${base.propertysearcher.subdomainapi}")
    protected String tenantSubdomainApi;

    @Value("${base.propertysearcher.url}")
    protected String tenantUrl;

    private Locale configureLocale(LandlordCustomer customer) {
        return new Locale(LANGUANGE_KEY_DE, COUNTRY_KEY_DE);
    }

    public void emailCustomization(
            Map<String, Object> data,
            MailTemplate mailTemplate,
            LandlordCustomer customizer,
            String footerCompanyDefault
    ) {
        data.put(ModelParams.MODEL_TIMEZONE, locationService.getTimeZone());

        Locale locale = configureLocale(customizer);
        this.configureCustomer(data, customizer, locale, mailTemplate, footerCompanyDefault);
        data.put(ModelParams.MODEL_LOCALE, locale);
    }

    public Locale getLocale(LandlordCustomer customer) {
        return configureLocale(customer);
    }

    protected String buildUrl(String protocol, String subdomain, String domain, Locale locale) {
        StringBuilder sb = new StringBuilder();

        sb.append(protocol);
        if (subdomain != null && !subdomain.isEmpty()) {
            sb.append(subdomain);
            sb.append(DOT);
        }

        sb.append(domain);

        if (locale != null) {
            sb.append(SLASH);
            sb.append(locale.getLanguage());
        }

        return sb.toString();
    }

    protected String buildUrl(String url, Locale locale) {
        StringBuilder sb = new StringBuilder();

        sb.append(url);

        if (locale != null) {
            sb.append(SLASH);
            sb.append(locale.getLanguage());
        }

        return sb.toString();
    }

    protected void populateFooter(LandlordCustomer customer, Map<String, Object> data, String footerCompanyDefault) {
        if (customer != null && StringUtils.hasText(customer.getName())) {
            data.put(ModelParams.MODEL_FOOTER, customer.getName());
        } else {
            data.put(ModelParams.MODEL_FOOTER, footerCompanyDefault);
        }
    }

    protected void populateEmailSettings(LandlordCustomer customer, Map<String, Object> data) {
        if (customer != null && customer.getCustomerSettings() != null && customer.getCustomerSettings().getMailConfig() != null) {
            LandlordCustomerMailConfig mailConfig = customer.getCustomerSettings().getMailConfig();
            if (StringUtils.hasText(mailConfig.getSenderAddressPrefix())) {
                data.put(ModelParams.MODEL_SENDER_ADDRESS_PREFIX, mailConfig.getSenderAddressPrefix());
            }
            if (StringUtils.hasText(mailConfig.getSenderNamePrefix())) {
                data.put(ModelParams.MODEL_SENDER_NAME_PREFIX, mailConfig.getSenderNamePrefix());
            }
        }
    }

    protected abstract void configureCustomer(
            Map<String, Object> data,
            LandlordCustomer customer,
            Locale locale,
            MailTemplate mailTemplate,
            String footerCompanyDefault);


}
