package de.immomio.broker.service;

import de.immomio.broker.service.application.PropertyApplicationService;
import de.immomio.broker.service.proposal.PropertyProposalService;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.messaging.container.proposal.PSSearchProfileMessageContainer;
import de.immomio.messaging.container.score.LandlordPropertyScoreMessageContainer;
import de.immomio.messaging.container.user.PSUserProfileMessageContainer;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import de.immomio.model.repository.core.propertysearcher.searchprofile.BasePropertySearcherSearchProfileRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Niklas Lindemann
 */
@Slf4j
@Service
public class OnMessageService {

    private final BasePropertySearcherSearchProfileRepository searchProfileRepository;

    private final BasePropertyRepository propertyRepository;

    private final PropertyProposalService propertyProposalService;

    private final PropertyApplicationService propertyApplicationService;

    private final BrokerGeoCodingService brokerGeoCodingService;

    private final BasePropertySearcherUserProfileRepository userProfileRepository;

    @Autowired
    public OnMessageService(
            BasePropertySearcherSearchProfileRepository searchProfileRepository,
            BasePropertyRepository propertyRepository,
            PropertyProposalService propertyProposalService,
            PropertyApplicationService propertyApplicationService,
            BrokerGeoCodingService brokerGeoCodingService,
            BasePropertySearcherUserProfileRepository userProfileRepository
    ) {
        this.searchProfileRepository = searchProfileRepository;
        this.propertyRepository = propertyRepository;
        this.propertyProposalService = propertyProposalService;
        this.propertyApplicationService = propertyApplicationService;
        this.brokerGeoCodingService = brokerGeoCodingService;
        this.userProfileRepository = userProfileRepository;
    }

    public void onMessage(PSSearchProfileMessageContainer message) {
        Long searchProfileId = message.getSearchProfileId();
        searchProfileRepository.findById(searchProfileId)
                .ifPresentOrElse(propertyProposalService::generateForSearchProfile,
                        () -> log.info("searchprofile {} has been removed", searchProfileId)
                );
    }

    public void onMessage(de.immomio.messaging.container.proposal.LandlordPropertyMessageContainer message) {
        try {
            List<Property> properties = getProperties(message.getPropertyIds());
            brokerGeoCodingService.generateAndSaveCoordinates(properties, message.isCalculateCoordinates());

            properties.stream().filter(Objects::nonNull).forEach(property -> {
                propertyApplicationService.updateScoreWhenPropertyUpdated(property.getId());
                propertyProposalService.calculateProposals(property);
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void onMessage(LandlordPropertyScoreMessageContainer container) {
        container.getPropertyIds().forEach(propertyApplicationService::updateScoreWhenPropertyUpdated);
        container.getPropertyIds().forEach(propertyProposalService::updateScoreWhenPropertyUpdated);
    }

    public void onMessage(PSUserProfileMessageContainer message) {
        try {
            userProfileRepository.findById(message.getUserProfileId()).ifPresentOrElse(user -> {
                        propertyProposalService.generateForUser(user);
                        propertyApplicationService.updateScoreWhenUserUpdated(user);
                    },
                    () -> log.error("user " + message.getUserProfileId() + " not found")
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private List<Property> getProperties(List<Long> propertyIds) {
        return propertyRepository.findAllByIdIn(propertyIds);
    }

}
