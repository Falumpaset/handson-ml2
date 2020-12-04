package de.immomio.service.proposal;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.model.repository.core.shared.propertyProposal.BasePropertyProposalRepository;
import de.immomio.service.CrawlerPropertyCountRefreshCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Service
public class ProposalService {

    private BasePropertyProposalRepository propertyProposalRepository;

    private CrawlerPropertyCountRefreshCacheService propertyCountRefreshCacheService;

    @Autowired
    public ProposalService(
            BasePropertyProposalRepository propertyProposalRepository,
            CrawlerPropertyCountRefreshCacheService propertyCountRefreshCacheService
    ) {
        this.propertyProposalRepository = propertyProposalRepository;
        this.propertyCountRefreshCacheService = propertyCountRefreshCacheService;
    }

    public Long getCount(Date dateFrom, Date dateEnd) {
        return propertyProposalRepository.countByCreatedBetween(dateFrom, dateEnd);
    }

    public void deleteProposals(PropertySearcherUserProfile userProfile) {
        List<PropertyProposal> propertyProposals = propertyProposalRepository.findAllByUserProfile(userProfile);

        if (!propertyProposals.isEmpty()) {
            List<Long> ids = propertyProposals.stream()
                    .map(PropertyProposal::getId)
                    .collect(Collectors.toList());

            List<Long> properties = propertyProposalRepository.findAllPropertyIdsForProposals(ids);
            propertyProposalRepository.customDelete(ids);
            properties.forEach(propertyCountRefreshCacheService::refreshProposalCache);
        }
    }
}
