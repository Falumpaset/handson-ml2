package de.immomio.landlord.listener.messageSource;

import de.immomio.data.landlord.entity.messagesource.MessageSource;
import de.immomio.landlord.service.product.LandlordCacheEvictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class MessageSourceListener {

    private final LandlordCacheEvictService cacheEvictService;

    @Autowired
    public MessageSourceListener(LandlordCacheEvictService cacheEvictService) {
        this.cacheEvictService = cacheEvictService;
    }

    @HandleAfterSave
    public void evictCache(MessageSource messageSource) {
        cacheEvictService.evictMessageSourceBasedCaches(messageSource);
    }
}
