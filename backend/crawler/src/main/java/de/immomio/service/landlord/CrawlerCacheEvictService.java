package de.immomio.service.landlord;

import de.immomio.caching.service.AbstractCacheEvictService;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * @author Maik Kingma
 */

@Service
public class CrawlerCacheEvictService extends AbstractCacheEvictService<LandlordCustomer> {

    public CrawlerCacheEvictService(CacheManager cacheManager) {
        super(cacheManager);
    }

    @Override
    public void evictCustomerCache(LandlordCustomer customer) {
        customer.getUsers().stream()
                .map(landlordUser -> landlordUser.getId().toString())
                .forEach(this::evictAuthoritiesCache);
    }
}
