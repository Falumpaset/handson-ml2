package de.immomio.caching;

import com.neovisionaries.i18n.LocaleCode;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.messagesource.MessageSource;
import de.immomio.model.repository.core.landlord.customer.messagesource.BaseLandlordMessageSourceRepository;
import de.immomio.utils.CachingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static de.immomio.constants.CacheConstants.CUSTOMER_TRANSLATIONS;

@Service
public class LandlordTranslationCacheService {

    private final BaseLandlordMessageSourceRepository messageSourceRepository;

    @Autowired
    public LandlordTranslationCacheService(BaseLandlordMessageSourceRepository messageSourceRepository) {
        this.messageSourceRepository = messageSourceRepository;
    }

    @Cacheable(value = CUSTOMER_TRANSLATIONS, key = "@landlordTranslationCacheService.getCustomerTranslationsKey(#customer, #locale)", cacheManager = "cacheManager")
    public Map<String, String> getLandlordTranslations(LandlordCustomer customer, Locale locale) {
        List<MessageSource> messageSources = messageSourceRepository.findAllByCustomerAndLocale(customer,
                LocaleCode.getByCode(locale.getCountry(), locale.getCountry()));
        return messageSources.stream().collect(Collectors.toMap(MessageSource::getMessageKey, MessageSource::getValue));
    }

    // used by @Cacheable of getLandlordTranslations
    public String getCustomerTranslationsKey(LandlordCustomer customer, Locale locale) {
        return CachingUtils.getCustomerTranslationsKey(customer, locale);
    }
}
