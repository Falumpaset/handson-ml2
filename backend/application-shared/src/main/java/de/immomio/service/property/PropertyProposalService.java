package de.immomio.service.property;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposalState;
import de.immomio.model.repository.shared.propertyProposal.PropertyProposalRepository;
import de.immomio.service.property.cache.PropertyCountRefreshCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyProposalService {

    private final PropertyProposalRepository proposalRepository;
    private final PropertyCountRefreshCacheService propertyCountRefreshCacheService;

    @Autowired
    public PropertyProposalService(PropertyProposalRepository proposalRepository, PropertyCountRefreshCacheService propertyCountRefreshCacheService) {
        this.proposalRepository = proposalRepository;
        this.propertyCountRefreshCacheService = propertyCountRefreshCacheService;
    }

    public Optional<PropertyProposal> findById(Long id) {
        return proposalRepository.findById(id);
    }

    public void updateState(PropertyProposal proposal, PropertyProposalState state) {
        proposal.setState(state);
        if (state == PropertyProposalState.OFFERED) {
            proposal.setOffered(new Date());
        }
        proposalRepository.save(proposal);
    }

    public void cleanUpProposalsAfterPropertyRented(Property property) {
        cleanUpProposals(proposalRepository.findAllByPropertyId(property.getId()));
    }

    public void cleanUpProposalsAfterExpiredSearch(PropertySearcherUserProfile userProfile) {
        List<PropertyProposal> proposals = proposalRepository.findAllByUserProfile(userProfile);
        cleanUpProposals(proposals);
    }

    private void cleanUpProposals(List<PropertyProposal> propertyProposals) {
        List<Long> ids = propertyProposals.stream().filter(this::proposalQualifiesForCleansing).map(PropertyProposal::getId).collect(Collectors.toList());

        if (!ids.isEmpty()) {
            List<Long> propertyIds = propertyProposals.stream()
                    .map(propertyProposal -> propertyProposal.getProperty().getId())
                    .distinct()
                    .collect(Collectors.toList());

            proposalRepository.customDelete(ids);

            for (Long propertyId : propertyIds) {
                propertyCountRefreshCacheService.refreshProposalCache(propertyId);
            }
        }
    }

    private boolean proposalQualifiesForCleansing(PropertyProposal propertyProposal) {
        return propertyProposal.getState() == PropertyProposalState.PROSPECT;
    }
}
