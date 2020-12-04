package de.immomio.broker.service.property.cache;

import de.immomio.caching.service.CacheCountDataSupplier;
import de.immomio.model.repository.core.shared.application.BasePropertyApplicationRepository;
import de.immomio.model.repository.core.shared.propertyProposal.BasePropertyProposalRepository;
import de.immomio.service.shared.AbstractPropertyCountRefreshCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertyCountRefreshCacheService
        extends AbstractPropertyCountRefreshCacheService<BasePropertyApplicationRepository, BasePropertyProposalRepository> {

    @Autowired
    public PropertyCountRefreshCacheService(
            BasePropertyApplicationRepository propertyApplicationRepository,
            BasePropertyProposalRepository propertyProposalRepository,
            CacheCountDataSupplier cacheCountDataSupplier
    ) {
        super(propertyApplicationRepository, propertyProposalRepository, cacheCountDataSupplier);
    }

}
