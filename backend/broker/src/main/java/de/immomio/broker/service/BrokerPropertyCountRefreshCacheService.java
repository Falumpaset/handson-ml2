package de.immomio.broker.service;

import de.immomio.caching.service.CacheCountDataSupplier;
import de.immomio.model.repository.core.shared.application.BasePropertyApplicationRepository;
import de.immomio.model.repository.core.shared.propertyProposal.BasePropertyProposalRepository;
import de.immomio.service.shared.AbstractPropertyCountRefreshCacheService;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Service
public class BrokerPropertyCountRefreshCacheService extends AbstractPropertyCountRefreshCacheService
        <BasePropertyApplicationRepository,
        BasePropertyProposalRepository> {

    public BrokerPropertyCountRefreshCacheService(
            BasePropertyApplicationRepository applicationRepository,
            BasePropertyProposalRepository propertyProposalRepository,
            CacheCountDataSupplier cacheCountDataSupplier
    ) {
        super(applicationRepository, propertyProposalRepository, cacheCountDataSupplier);
    }

}
