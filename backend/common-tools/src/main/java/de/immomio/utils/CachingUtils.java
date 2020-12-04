package de.immomio.utils;

import com.neovisionaries.i18n.LocaleCode;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.mail.sender.templates.MailTemplate;

import java.util.Locale;

public class CachingUtils {

    public static final String CACHE_KEY_SEPERATOR = "_";

    private CachingUtils() {
    }

    public static String getCustomerTranslationsKey(LandlordCustomer customer, Locale locale) {
        return getCustomerTranslationsKey(customer, LocaleCode.getByCode(locale.getCountry(), locale.getCountry()));
    }

    public static String getCustomerTranslationsKey(LandlordCustomer customer, LocaleCode locale) {
        return customer.getId() + CACHE_KEY_SEPERATOR + locale.name();
    }

    public static String getCustomerTranslatedEmailTemplateKey(LandlordCustomer customer, Locale locale, MailTemplate mailTemplate) {
        return getCustomerTranslatedEmailTemplateKey(customer, LocaleCode.getByCode(locale.getCountry(), locale.getCountry()), mailTemplate);
    }

    public static String getCustomerTranslatedEmailTemplateKey(LandlordCustomer customer, LocaleCode locale, MailTemplate mailTemplate) {
        return (customer != null ? customer.getId() + CACHE_KEY_SEPERATOR : "") + locale.name() + CACHE_KEY_SEPERATOR + mailTemplate.getSendGridTemplate();
    }
}
