package de.immomio.service.property.cache;

import de.immomio.caching.service.CacheCountDataSupplier;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.model.repository.shared.propertyProposal.PropertyProposalRepository;
import de.immomio.service.shared.AbstractPropertyCountRefreshCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Service
public class PropertyCountRefreshCacheService
        extends AbstractPropertyCountRefreshCacheService<PropertyApplicationRepository, PropertyProposalRepository> {

    @Autowired
    public PropertyCountRefreshCacheService(
            PropertyApplicationRepository propertyApplicationRepository,
            PropertyProposalRepository propertyProposalRepository,
            CacheCountDataSupplier cacheCountDataSupplier
    ) {
        super(propertyApplicationRepository, propertyProposalRepository, cacheCountDataSupplier);
    }

}
