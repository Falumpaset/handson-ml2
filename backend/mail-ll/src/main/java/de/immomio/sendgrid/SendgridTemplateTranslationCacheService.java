package de.immomio.sendgrid;

import de.immomio.caching.LandlordTranslationCacheService;
import de.immomio.config.ApplicationMessageSource;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.sendgrid.model.SendgridTemplateVersion;
import de.immomio.utils.CachingUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.immomio.constants.CacheConstants.CUSTOMER_MAIL_TEMPLATE_TRANSLATIONS;

@Service
public class SendgridTemplateTranslationCacheService {

    private final LandlordTranslationCacheService translationCacheService;
    private final ApplicationMessageSource messageSource;
    private final SendgridTemplateClient sendgridTemplateClient;

    @Autowired
    public SendgridTemplateTranslationCacheService(LandlordTranslationCacheService translationCacheService,
            ApplicationMessageSource messageSource, SendgridTemplateClient sendgridTemplateClient) {
        this.translationCacheService = translationCacheService;
        this.messageSource = messageSource;
        this.sendgridTemplateClient = sendgridTemplateClient;
    }

    @Cacheable(value = CUSTOMER_MAIL_TEMPLATE_TRANSLATIONS, key = "@sendgridTemplateTranslationCacheService.getCustomerTranslatedEmailTemplateKey(#customer, #locale, #template)", cacheManager = "cacheManager")
    public SendgridTemplateVersion getTranslatedSendgridHtml(MailTemplate template, LandlordCustomer customer, Locale locale) {
        SendgridTemplateVersion sendgridTemplate = sendgridTemplateClient.getTemplate(template.getSendGridTemplate());
        sendgridTemplate.setHtmlContent(replaceTranslations(template, sendgridTemplate.getHtmlContent(), customer, locale));
        sendgridTemplate.setSubject(replaceTranslations(template, sendgridTemplate.getSubject(), customer, locale));
        return sendgridTemplate;
    }

    // used by @Cacheable of getTranslatedSendgridHtml
    public String getCustomerTranslatedEmailTemplateKey(LandlordCustomer customer, Locale locale, MailTemplate mailTemplate) {
        return CachingUtils.getCustomerTranslatedEmailTemplateKey(customer, locale, mailTemplate);
    }

    private String replaceTranslations(MailTemplate template, String content, LandlordCustomer customer, Locale locale) {
        Map<String, String> landlordTranslations = customer != null ? translationCacheService.getLandlordTranslations(customer, locale) : new HashMap<>();

        Pattern pattern = Pattern.compile("\\{\\{(.*?)}}");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String group = matcher.group();
            String messageKey = matcher.group(1);

            String replacement = getTranslation(template, messageKey, landlordTranslations, locale);
            if (StringUtils.isNotBlank(replacement)) {
                content = content.replace(group, replacement);
            }
        }

        return content;
    }

    private String getTranslation(MailTemplate template, String key, Map<String, String> landlordTranslations, Locale locale) {
        if (template.isConfigurable() && landlordTranslations.containsKey(key)) {
            return landlordTranslations.get(key);
        }

        return messageSource.resolveCodeString(key, locale);
    }
}
