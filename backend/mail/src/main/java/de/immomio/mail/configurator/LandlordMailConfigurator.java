package de.immomio.mail.configurator;

import de.immomio.data.base.type.customer.LandlordCustomerPreference;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.mail.ModelParams;
import de.immomio.mail.configurator.AbstractMailConfigurator;
import de.immomio.mail.configurator.MailLogoConfigurator;
import de.immomio.mail.sender.templates.MailTemplate;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author Bastian Bliemeister
 */

@Slf4j
@Service
public class LandlordMailConfigurator extends AbstractMailConfigurator {

    private final MailLogoConfigurator mailLogoConfigurator;

    @Autowired
    public LandlordMailConfigurator(MailLogoConfigurator mailLogoConfigurator) {
        this.mailLogoConfigurator = mailLogoConfigurator;
    }

    public String buildAppUrl() {
        return this.buildUrl(this.protocol, subdomain, host, locationService.getLocale());
    }

    public String buildPsAppUrl() {
        return this.buildUrl(this.protocol, tenantSubdomain, tenantHost, locationService.getLocale());
    }

    @Override
    protected void configureCustomer(Map<String, Object> data,
                                     LandlordCustomer customer,
                                     Locale locale,
                                     MailTemplate mailTemplate,
                                     String footerCompanyDefault) {
        String host = this.host;

        if (customer != null) {
            log.info("Configuring E-Mail template for customer: " + customer.getId());

            data.put(ModelParams.MODEL_CUSTOMER, customer);

            String tmp = customer.getPreferences().get(LandlordCustomerPreference.DOMAIN.getKey());
            if (! StringUtils.isEmpty(tmp)) {
                host = this.subdomainhost;
            }

        }

        data.put(ModelParams.MODEL_URL, buildAppUrl());
        data.put(ModelParams.MODEL_API_URL, this.buildUrl(this.protocol, subdomainApi, host, null));
        data.put(ModelParams.MODEL_TENANT_URL, this.buildUrl(this.tenantUrl, locale));

        populateFooter(customer, data, footerCompanyDefault);
        populateEmailSettings(customer, data);

        mailLogoConfigurator.applyDefaultLogo(data);
    }
}
