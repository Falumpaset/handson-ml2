package de.immomio.landlord.service.email;

import de.immomio.beans.landlord.email.TemplateRequestBean;
import de.immomio.beans.landlord.email.TemplateResponseBean;
import de.immomio.config.ApplicationMessageSource;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.landlord.service.velocity.VelocityEmailTemplateService;
import de.immomio.mail.configurator.LandlordMailConfigurator;
import de.immomio.mail.configurator.PropertySearcherMailConfigurator;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.mail.sender.templates.MailType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

/**
 * @author Fabian Beck
 */

@Service
public class EmailTemplateService {

    public static final String TEMPLATE_NOT_CONFIGURABLE_L = "TEMPLATE_NOT_CONFIGURABLE_L";
    private final LandlordMailConfigurator landlordMailConfigurator;

    private final SendgridEmailTemplateService sendgridEmailTemplateService;

    private final VelocityEmailTemplateService velocityEmailTemplateService;

    private final PropertySearcherMailConfigurator propertySearcherMailConfigurator;

    private final EmailTemplateDataProviderService emailTemplateDataProviderService;

    private final UserSecurityService userSecurityService;

    private final ApplicationMessageSource messageSource;

    @Value("${email.footer.customer.default}")
    private String footerPlaceholder;

    @Autowired
    public EmailTemplateService(LandlordMailConfigurator landlordMailConfigurator,
            SendgridEmailTemplateService sendgridEmailTemplateService,
            VelocityEmailTemplateService velocityEmailTemplateService,
            PropertySearcherMailConfigurator propertySearcherMailConfigurator,
            EmailTemplateDataProviderService emailTemplateDataProviderService,
            UserSecurityService userSecurityService,
            ApplicationMessageSource messageSource) {
        this.landlordMailConfigurator = landlordMailConfigurator;
        this.sendgridEmailTemplateService = sendgridEmailTemplateService;
        this.velocityEmailTemplateService = velocityEmailTemplateService;
        this.propertySearcherMailConfigurator = propertySearcherMailConfigurator;
        this.emailTemplateDataProviderService = emailTemplateDataProviderService;
        this.userSecurityService = userSecurityService;
        this.messageSource = messageSource;
    }

    public TemplateResponseBean template(MailTemplate template, TemplateRequestBean requestBean) {
        if (!template.isConfigurable()) {
            throw new ApiValidationException(TEMPLATE_NOT_CONFIGURABLE_L);
        }

        LandlordCustomer customer = userSecurityService.getPrincipalUser().getCustomer();

        Map<String, Object> model = emailTemplateDataProviderService.getTemplateEmailModel(requestBean, customer);
        Locale locale = landlordMailConfigurator.getLocale(customer);

        if (template.getMailType() == MailType.PropertysearcherUser) {
            propertySearcherMailConfigurator.emailCustomization(model, template, customer, messageSource.resolveCodeString(footerPlaceholder, locale));
        } else {
            landlordMailConfigurator.emailCustomization(model, template, customer, messageSource.resolveCodeString(footerPlaceholder, locale));
        }

        return readAndMerge(template, model, requestBean.isTranslateKeys(), locale, customer);
    }

    private TemplateResponseBean readAndMerge(MailTemplate template, Map<String, Object> data, boolean filled,
            Locale locale, LandlordCustomer customer) {
        if (StringUtils.isBlank(template.getSendGridTemplate())) {
            return velocityEmailTemplateService.readAndMergeTemplate(template, data);
        } else {
            return sendgridEmailTemplateService.getTemplate(template, customer, data, filled, locale);
        }
    }
}
