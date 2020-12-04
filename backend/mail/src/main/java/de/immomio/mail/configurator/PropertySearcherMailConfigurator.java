package de.immomio.mail.configurator;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.mail.ModelParams;
import de.immomio.mail.configurator.AbstractMailConfigurator;
import de.immomio.mail.configurator.MailBrandingConfigurator;
import de.immomio.mail.configurator.MailLogoConfigurator;
import de.immomio.mail.sender.templates.MailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
public class PropertySearcherMailConfigurator extends AbstractMailConfigurator {

    private final MailBrandingConfigurator mailBrandingConfigurator;

    private final MailLogoConfigurator mailLogoConfigurator;

    @Autowired
    public PropertySearcherMailConfigurator(MailBrandingConfigurator mailBrandingConfigurator,
                                            MailLogoConfigurator mailLogoConfigurator) {
        this.mailBrandingConfigurator = mailBrandingConfigurator;
        this.mailLogoConfigurator = mailLogoConfigurator;
    }

    public String buildAppUrl() {
        return this.buildUrl(this.tenantUrl, locationService.getLocale());
    }

    protected void configureCustomer(Map<String, Object> data,
                                     LandlordCustomer customer,
                                     Locale locale,
                                     MailTemplate mailTemplate,
                                     String footerCompanyDefault) {
        if (customer != null) {
            log.info("Configuring E-Mail template for customer: " + customer.getId());

            data.put(ModelParams.MODEL_CUSTOMER, customer);
        }

        data.put(ModelParams.MODEL_URL, buildAppUrl());
        data.put(ModelParams.MODEL_API_URL, this.buildUrl(this.protocol, tenantSubdomainApi, this.tenantHost, null));
        data.put(ModelParams.MODEL_TENANT_URL, buildAppUrl());

        populateFooter(customer, data, footerCompanyDefault);
        populateEmailSettings(customer, data);

        mailLogoConfigurator.applyBrandingLogoIfExist(data);
        mailBrandingConfigurator.applyBrandingThemeIfExist(customer, data);
    }
}
