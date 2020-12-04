package de.immomio.service.property.cache;

import de.immomio.constants.CacheConstants;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.model.repository.shared.propertyProposal.PropertyProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */
@Service
public class PropertyCacheCountService {

    private PropertyProposalRepository propertyProposalRepository;

    private PropertyApplicationRepository propertyApplicationRepository;

    @Autowired
    public PropertyCacheCountService(
            PropertyProposalRepository propertyProposalRepository,
            PropertyApplicationRepository propertyApplicationRepository
    ) {
        this.propertyProposalRepository = propertyProposalRepository;
        this.propertyApplicationRepository = propertyApplicationRepository;
    }

    @Cacheable(value = CacheConstants.PROPOSAL_COUNT_CACHE, key = "#propertyId.toString()", cacheManager = "cacheManager")
    public Long getProposalCount(Long propertyId) {
        return propertyProposalRepository.countByProperty(propertyId);
    }

    @Cacheable(value = CacheConstants.APPLICATION_COUNT_CACHE, key = "#propertyId.toString()", cacheManager = "cacheManager")
    public Long getApplicationCount(Long propertyId) {
        return propertyApplicationRepository.getSizeOfApplications(propertyId);
    }

    @Cacheable(value = CacheConstants.INVITEE_COUNT_CACHE, key = "#propertyId.toString()", cacheManager = "cacheManager")
    public Long getInviteeCount(Long propertyId) {
        return propertyApplicationRepository.getSizeOfInvitees(propertyId);
    }
}
