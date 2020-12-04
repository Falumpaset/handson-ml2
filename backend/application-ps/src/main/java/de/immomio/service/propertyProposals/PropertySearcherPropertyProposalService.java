package de.immomio.service.propertyProposals;

import de.immomio.data.base.entity.AbstractEntity;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfile;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.proposal.PropertySearcherPropertyProposalBean;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposalState;
import de.immomio.messaging.container.proposal.PSSearchProfileMessageContainer;
import de.immomio.model.repository.shared.propertyProposal.PropertyProposalRepository;
import de.immomio.service.property.cache.PropertyCountRefreshCacheService;
import de.immomio.service.proposal.PropertyProposalConverter;
import de.immomio.service.reporting.PropertySearcherUserIndexingDelegate;
import de.immomio.service.sender.SearchProfileToProposalMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maik Kingma
 */
@Service
@Slf4j
public class PropertySearcherPropertyProposalService {

    private final SearchProfileToProposalMessageSender proposalMessageSender;

    private final PropertyProposalRepository propertyProposalRepository;

    private final PropertyCountRefreshCacheService refreshPropertyCountCache;

    private final PropertySearcherUserIndexingDelegate userIndexingDelegate;

    private final PropertySearcherProposalToApplicationService proposalToApplicationService;

    private final PropertyProposalConverter propertyProposalConverter;

    @Autowired
    public PropertySearcherPropertyProposalService(
            SearchProfileToProposalMessageSender proposalMessageSender,
            PropertyProposalRepository propertyProposalRepository,
            PropertyCountRefreshCacheService refreshPropertyCountCache,
            PropertySearcherUserIndexingDelegate userIndexingDelegate,
            PropertySearcherProposalToApplicationService proposalToApplicationService,
            PropertyProposalConverter propertyProposalConverter) {
        this.proposalMessageSender = proposalMessageSender;
        this.propertyProposalRepository = propertyProposalRepository;
        this.refreshPropertyCountCache = refreshPropertyCountCache;
        this.userIndexingDelegate = userIndexingDelegate;
        this.proposalToApplicationService = proposalToApplicationService;
        this.propertyProposalConverter = propertyProposalConverter;
    }

    public void createProposalsFromSP(PropertySearcherSearchProfile searchProfile) {
        PSSearchProfileMessageContainer container = new PSSearchProfileMessageContainer();
        container.setSearchProfileId(searchProfile.getId());
        proposalMessageSender.sendProposalUpdateMessage(container);
    }

    public void deleteProposals(PropertySearcherSearchProfile searchProfile) {
        List<Long> propertyIds = searchProfile.getProposals()
                .stream()
                .map(PropertyProposal::getProperty)
                .map(AbstractEntity::getId)
                .collect(Collectors.toList());
        propertyProposalRepository.customDeleteBySearchProfile(Collections.singletonList(searchProfile.getId()));
        propertyIds.forEach(refreshPropertyCountCache::refreshProposalCache);
    }

    public void deleteProposals(PropertySearcherUserProfile userProfile) {
        propertyProposalRepository.customDeleteByUser(userProfile.getId());
        List<PropertyProposal> proposals = userProfile.getPropertyProposals();
        List<Property> properties = proposals.stream().map(PropertyProposal::getProperty).collect(Collectors.toList());
        propertyProposalRepository.deleteAll(proposals);
        properties.forEach(refreshPropertyCountCache::refreshProposalCache);
    }

    public void acceptProposal(PropertyProposal proposal) {

        proposal.setState(PropertyProposalState.ACCEPTED);
        proposal.setAccepted(new Date());

        proposalToApplicationService.convertProposalToApplication(proposal);

        PropertyProposal savedProposal = propertyProposalRepository.save(proposal);
        refreshPropertyCountCache.refreshProposalCache(savedProposal.getProperty());
        userIndexingDelegate.proposalAccepted(savedProposal);
    }

    public PropertyProposal deny(PropertyProposal propertyProposal) {
        propertyProposal.setState(PropertyProposalState.DENIEDBYPS);
        PropertyProposal saved = propertyProposalRepository.save(propertyProposal);
        userIndexingDelegate.proposalDenied(propertyProposal);
        return saved;
    }

    public Page<PropertySearcherPropertyProposalBean> findAllForPs(PropertySearcherUser user, Pageable pageable) {
        Page<PropertyProposal> proposalPage = propertyProposalRepository.findOfferedForPs(user, pageable);
        return new PageImpl<>(proposalPage.stream().map(propertyProposalConverter::convertToPropertySearcherProposalBean).collect(Collectors.toList()), pageable, proposalPage.getTotalElements());
    }
}
