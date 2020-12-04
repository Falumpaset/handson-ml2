package de.immomio.service.shared;

import de.immomio.caching.service.CacheCountDataSupplier;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.model.repository.core.shared.application.BasePropertyApplicationRepository;
import de.immomio.model.repository.core.shared.propertyProposal.BasePropertyProposalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * @author Niklas Lindemann
 */
@Slf4j
public class AbstractPropertyCountRefreshCacheService
        <PAR extends BasePropertyApplicationRepository,
        PPR extends BasePropertyProposalRepository> {

    private PAR applicationRepository;

    private PPR propertyProposalRepository;

    private CacheCountDataSupplier cacheCountDataSupplier;

    public AbstractPropertyCountRefreshCacheService(
            PAR applicationRepository,
            PPR propertyProposalRepository,
            CacheCountDataSupplier cacheCountDataSupplier
    ) {
        this.applicationRepository = applicationRepository;
        this.propertyProposalRepository = propertyProposalRepository;
        this.cacheCountDataSupplier = cacheCountDataSupplier;
    }

    @Async
    public void refreshApplicationCache(Property property) {
        log.info("refresh application cache for property " + property.getId() );

        Long sizeOfInvitees = applicationRepository.getSizeOfInvitees(property.getId());
        Long sizeOfApplications = applicationRepository.getSizeOfApplications(property.getId());

        cacheCountDataSupplier.setApplicationCount(property.getId(), sizeOfApplications);
        cacheCountDataSupplier.setInviteeCount(property.getId(), sizeOfInvitees);
    }

    @Async
    public void refreshProposalCache(Property property) {
        refreshProposalCache(property.getId());
    }

    @Async
    public void refreshProposalCache(Long propertyId) {
        Long sizeOfProposals = propertyProposalRepository.countByProperty(propertyId);
        cacheCountDataSupplier.setProposalCount(propertyId, sizeOfProposals);
    }

    public void refreshPropertyCaches(List<Property> properties) {
        properties.stream().distinct().forEach(property -> {
            refreshApplicationCache(property);
            refreshProposalCache(property);
        });
    }
}
