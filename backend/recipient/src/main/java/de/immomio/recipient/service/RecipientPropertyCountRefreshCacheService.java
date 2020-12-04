package de.immomio.recipient.service;

import de.immomio.caching.service.CacheCountDataSupplier;
import de.immomio.model.repository.core.shared.application.BasePropertyApplicationRepository;
import de.immomio.model.repository.core.shared.propertyProposal.BasePropertyProposalRepository;
import de.immomio.service.shared.AbstractPropertyCountRefreshCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Service
public class RecipientPropertyCountRefreshCacheService extends AbstractPropertyCountRefreshCacheService<
        BasePropertyApplicationRepository,
        BasePropertyProposalRepository> {

    @Autowired
    public RecipientPropertyCountRefreshCacheService(
            BasePropertyApplicationRepository applicationRepository,
            BasePropertyProposalRepository propertyProposalRepository,
            CacheCountDataSupplier cacheCountDataSupplier
    ) {
        super(applicationRepository, propertyProposalRepository, cacheCountDataSupplier);
    }
}
