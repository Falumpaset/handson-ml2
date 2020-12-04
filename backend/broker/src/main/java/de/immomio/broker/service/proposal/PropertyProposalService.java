package de.immomio.broker.service.proposal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import de.immomio.broker.service.BrokerPropertyCountRefreshCacheService;
import de.immomio.broker.service.CustomQuestionResponseService;
import de.immomio.broker.service.UserService;
import de.immomio.broker.service.calculator.BrokerCalculatorDelegate;
import de.immomio.broker.service.property.cache.PropertyCountRefreshCacheService;
import de.immomio.broker.service.reporting.BrokerElasticsearchIndexingSenderService;
import de.immomio.data.base.bean.score.ScoreBean;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.reporting.enums.ProposalEventType;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfile;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfileType;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.GeoCoordinates;
import de.immomio.data.shared.entity.common.District;
import de.immomio.data.shared.entity.common.ZipCode;
import de.immomio.data.shared.entity.common.customQuestion.CustomQuestionResponse;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposalState;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import de.immomio.model.repository.core.propertysearcher.searchprofile.BasePropertySearcherSearchProfileRepository;
import de.immomio.model.repository.core.shared.location.BaseDistrictRepository;
import de.immomio.model.repository.core.shared.propertyProposal.BasePropertyProposalRepository;
import de.immomio.utils.ListUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static de.immomio.utils.TenantUtils.isUserInOtherPrivateTenantPool;
import static de.immomio.utils.TenantUtils.isUserInPrivateTenantPool;

/**
 * @author Niklas Lindemann, Maik Kingma
 */

@Slf4j
@Service
public class PropertyProposalService {

    private final BasePropertyProposalRepository proposalRepository;

    private final BrokerCalculatorDelegate calculatorDelegate;

    private final BasePropertySearcherSearchProfileRepository searchProfileRepository;

    private BaseDistrictRepository districtRepository;

    private final CustomQuestionResponseService customQuestionResponseService;

    private final PropertyRequirementService requirementService;

    private final ProposalOfferService proposalOfferService;

    private final BrokerPropertyCountRefreshCacheService propertyCacheCountService;

    private BasePropertyRepository propertyRepository;

    private final BasePropertySearcherSearchProfileRepository searcherSearchProfileRepository;

    private final UserService userService;

    private final BrokerElasticsearchIndexingSenderService elasticsearchIndexingService;

    private final PropertyCountRefreshCacheService propertyCountRefreshCacheService;

    private static final int SRID = 4326;

    @Autowired
    public PropertyProposalService(
            BasePropertyProposalRepository proposalRepository,
            BrokerCalculatorDelegate calculatorDelegate,
            BasePropertySearcherSearchProfileRepository searchProfileRepository,
            BaseDistrictRepository districtRepository,
            CustomQuestionResponseService customQuestionResponseService,
            PropertyRequirementService requirementService,
            ProposalOfferService proposalOfferService,
            BrokerPropertyCountRefreshCacheService propertyCacheCountService,
            BasePropertyRepository propertyRepository,
            BasePropertySearcherSearchProfileRepository searcherSearchProfileRepository,
            UserService userService,
            BrokerElasticsearchIndexingSenderService elasticsearchIndexingService,
            PropertyCountRefreshCacheService propertyCountRefreshCacheService
    ) {
        this.proposalRepository = proposalRepository;
        this.calculatorDelegate = calculatorDelegate;
        this.searchProfileRepository = searchProfileRepository;
        this.districtRepository = districtRepository;
        this.customQuestionResponseService = customQuestionResponseService;
        this.requirementService = requirementService;
        this.proposalOfferService = proposalOfferService;
        this.propertyCacheCountService = propertyCacheCountService;
        this.propertyRepository = propertyRepository;
        this.searcherSearchProfileRepository = searcherSearchProfileRepository;
        this.userService = userService;
        this.elasticsearchIndexingService = elasticsearchIndexingService;
        this.propertyCountRefreshCacheService = propertyCountRefreshCacheService;
    }

    public PropertyProposal generateOrUpdateProposal(Property property, PropertySearcherSearchProfile searchProfile) {
        PropertySearcherUserProfile userProfile = searchProfile.getUserProfile();
        PropertyProposal proposal = getPropertyProposal(property, userProfile);
        proposal.setSearchProfile(searchProfile);
        setScoreForProposal(proposal);
        return proposal;
    }

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 10000))
    public void matchForProposals(Property property) {
        try {
            cleanupProposals(property);
            if (property.isRented()) {
                return;
            }

            Address address = property.getData().getAddress();
            Point point = createGeometryData(address);
            if (point != null) {
                List<PropertySearcherSearchProfile> searchProfiles = getSearchProfiles(point);
                searchProfiles.addAll(getSearchProfilesByZip(address.getZipCode()));
                List<PropertyProposal> generatedProposals = new ArrayList<>();
                searchProfiles.forEach(profile -> {
                    if (requirementService.propertyMeetsRequirementsForCreatingProposal(property, profile)) {
                        PropertyProposal propertyProposal = generateOrUpdateProposal(property, profile);
                        if (propertyProposal != null) {
                            generatedProposals.add(propertyProposal);
                        }
                    }
                });
                List<PropertyProposal> uniqueProposals = generatedProposals.stream()
                        .filter(ListUtils.distinctByKey(proposal -> proposal.getUserProfile().getId()))
                        .collect(Collectors.toList());
                saveAllAndOffer(uniqueProposals);
                refreshProposalCache(Collections.singletonList(property));

            }
        } catch (Exception e) {
            log.error(e.getMessage() + " INSIDE RETRY BLOCK", e);
            throw e;
        }
    }

    public void generateForUser(PropertySearcherUserProfile userProfile) {
        searcherSearchProfileRepository.findAllByUserProfile(userProfile).forEach(this::generateForSearchProfile);
    }

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 10000))
    public void generateForSearchProfile(PropertySearcherSearchProfile searchProfile) {
        try {
            if (searchProfile == null) {
                log.info("Search profile has been deleted");
                return;
            }
            if (BooleanUtils.isTrue(searchProfile.getDeleted())) {
                log.info("searchprofile {} is deleted", searchProfile.getId());
                return;
            }
            PropertySearcherUserProfile userProfile = searchProfile.getUserProfile();
            log.info("Update scores for searchprofile id = {}", searchProfile.getId());

            List<Property> properties = getProperties(searchProfile);
            List<PropertyProposal> generatedProposals = new ArrayList<>();
            properties.stream()
                    .filter(property -> {
                        boolean userBlockedForCreatingProposals = userService.isUserBlockedForCreatingProposals(userProfile);
                        if (userBlockedForCreatingProposals) {
                            return isUserInPrivateTenantPool(userProfile, property.getCustomer());
                        }
                        return !isUserInOtherPrivateTenantPool(userProfile, property.getCustomer());
                    })
                    .forEach(property -> {
                        PropertyProposal propertyProposal = calculateProposal(property, searchProfile);
                        if (propertyProposal != null) {
                            generatedProposals.add(propertyProposal);
                        }
                    });
            saveAllAndOffer(generatedProposals);
            refreshProposalCache(properties);
        } catch (Exception e) {
            log.error(e.getMessage() + " INSIDE RETRY BLOCK", e);
            throw e;
        }
    }

    public void calculateProposals(Property property) {
        log.info("Update scores and proposals after property is changed. Property id = {}", property.getId());
        try {
            LocalDateTime now = LocalDateTime.now();
            matchForProposals(property);
            long millis = now.until(LocalDateTime.now(), ChronoUnit.MILLIS);

            log.info(String.format("PROPOSAL CALCULATION OF PROPERTY %s TOOK %s milliseconds", property.getId(), millis));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void cleanUpProposalsAfterPropertyRented(Property property) {
        cleanUpProposals(proposalRepository.findAllByPropertyId(property.getId()));
    }

    public void setScoreForCustomQuestionResponse(Long responseId) {
        CustomQuestionResponse customQuestionResponse = customQuestionResponseService.findById(responseId);
        if (customQuestionResponse != null) {
            setScoreForCustomQuestionResponse(customQuestionResponse);
        } else {
            log.error("custom question response with id {} not found", responseId);
        }
    }

    public void updateScoreWhenPropertyUpdated(List<PropertyProposal> propertyProposals) {
        propertyProposals.forEach(this::setScoreForProposal);
        saveAllAndOffer(propertyProposals);
    }

    public void updateScoreWhenPropertyUpdated(Long propertyId) {
        List<PropertyProposal> proposals = proposalRepository.findAllByPropertyId(propertyId);
        proposals.forEach(this::setScoreForProposal);
        saveAllAndOffer(proposals);
    }

    private void setScoreForProposal(PropertyProposal proposal) {
        ScoreBean scoreBean = calculatorDelegate.calculateScore(proposal.getProperty(), proposal.getUserProfile());
        proposal.setCustomQuestionScore(scoreBean.getCustomQuestionScore());
        proposal.setScore(scoreBean.getScore().doubleValue());
    }

    private void setScoreForCustomQuestionResponse(CustomQuestionResponse response) {
        List<PropertyProposal> propertyProposals = proposalRepository
                .findByUserProfileAndPropertyCustomer(response.getUserProfile(), response.getCustomQuestion().getCustomer());

        propertyProposals.forEach(this::setScoreForProposal);

        proposalRepository.saveAll(propertyProposals);
    }

    private void cleanUpProposals(List<PropertyProposal> propertyProposals) {
        List<Long> ids = propertyProposals.stream()
                .filter(this::proposalQualifiesForCleansing)
                .map(PropertyProposal::getId)
                .collect(Collectors.toList());

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

    private List<PropertySearcherSearchProfile> getSearchProfilesByZip(String zip) {
        return searchProfileRepository.findAllByZip(zip);
    }


    private List<Property> getProperties(PropertySearcherSearchProfile profile) {
        if (profile.getType() == PropertySearcherSearchProfileType.RADIUS) {
            Point point = createGeometryData(profile);

            return propertyRepository.customFindNearestToPoint(point.getY(), point.getX(), profile.getData().getRadius());
        } else {
            List<District> districts = districtRepository.findAllBySearchProfile(profile.getId());
            List<String> zipCodes = districts.stream()
                    .map(District::getZipCodes)
                    .flatMap(Collection::stream)
                    .map(ZipCode::getZipCode)
                    .distinct()
                    .collect(Collectors.toList());

            return !zipCodes.isEmpty() ? propertyRepository.findAllByZipCodeIn(zipCodes) : Collections.emptyList();
        }
    }


    private List<PropertySearcherSearchProfile> getSearchProfiles(Point point) {
        return searchProfileRepository.customFindNearestToPoint(point.getY(), point.getX());
    }

    private PropertyProposal getPropertyProposal(Property property, PropertySearcherUserProfile userProfile) {
        PropertyProposal proposal = proposalRepository.findByUserProfileAndProperty(userProfile, property);
        if (proposal == null) {
            proposal = new PropertyProposal();
            proposal.setState(PropertyProposalState.PROSPECT);
            proposal.setProperty(property);
            proposal.setUserProfile(userProfile);
        }

        return proposal;
    }

    private void cleanupProposals(Property property) {
        List<PropertyProposal> proposalsForDeletion = proposalRepository.findAllByPropertyId(property.getId())
                .stream()
                .parallel()
                .filter(this::proposalShouldBeDeleted)
                .collect(Collectors.toList());

        delete(proposalsForDeletion);
    }

    private boolean proposalShouldBeDeleted(PropertyProposal propertyProposal) {
        if (propertyProposal.getState() != PropertyProposalState.PROSPECT) {
            return false;
        }
        PropertySearcherSearchProfile searchProfile = propertyProposal.getSearchProfile();
        Property property = propertyProposal.getProperty();

        if (property.isRented()) {
            return true;
        }

        PropertySearcherUserProfile userProfile = searchProfile.getUserProfile();
        if (userProfile.getSearchUntil() == null || userProfile.getSearchUntil().before(new Date())) {
            return true;
        }

        boolean match = requirementService.propertyFulfillsSP(property, searchProfile);
        Point proposalPoint = createGeometryData(property.getData().getAddress());

        if (proposalPoint == null) {
            return false;
        }
        boolean withinDistance;
        if (searchProfile.getType() == PropertySearcherSearchProfileType.RADIUS) {
            Point searchProfilePoint = createGeometryData(searchProfile.getData().getAddress());
            if (searchProfilePoint == null) {
                return false;
            }
            withinDistance = searchProfileRepository
                    .isWithin(
                            proposalPoint.getY(),
                            proposalPoint.getX(),
                            searchProfilePoint.getY(),
                            searchProfilePoint.getX(),
                            searchProfile.getData().getRadius());
        } else {
            List<District> districts = districtRepository.findAllBySearchProfile(searchProfile.getId());
            withinDistance = districts
                    .stream()
                    .map(District::getZipCodes)
                    .flatMap(Collection::stream)
                    .anyMatch(zipCode -> zipCode.getZipCode().equals(property.getData().getAddress().getZipCode()));
        }

        return !(withinDistance && match);
    }

    private Point createGeometryData(PropertySearcherSearchProfile searchProfile) {
        Address address = searchProfile.getData().getAddress();
        if (address != null) {
            return createGeometryData(address);
        } else {
            log.error("Address is NULL for search profile id='{}'", searchProfile.getId());
        }

        return null;
    }

    private Point createGeometryData(Address address) {
        if (address != null) {
            GeoCoordinates coordinates = address.getCoordinates();
            if (coordinates != null) {
                Coordinate coordinate = new Coordinate(coordinates.getLongitude(), coordinates.getLatitude());
                return new GeometryFactory(new PrecisionModel(), SRID).createPoint(coordinate);
            }
        } else {
            log.warn("Address is NULL");
        }

        return null;
    }

    private void delete(List<PropertyProposal> proposalsForDeletion) {
        if (!proposalsForDeletion.isEmpty()) {
            List<Long> ids = proposalsForDeletion.stream().map(PropertyProposal::getId).collect(Collectors.toList());
            proposalRepository.customDelete(ids);
            proposalsForDeletion.stream()
                    .map(PropertyProposal::getProperty)
                    .distinct()
                    .forEach(propertyCacheCountService::refreshProposalCache);
        }
    }

    public void saveAllAndOffer(List<PropertyProposal> proposals) {
        List<PropertyProposal> proposalsToOffer = new ArrayList<>();
        List<PropertyProposal> newProposals = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        proposals.forEach(propertyProposal -> {
            if (propertyProposal.isNew()) {
                newProposals.add(propertyProposal);
            } else {
                proposalsToOffer.add(propertyProposal);
                try {
                    String customQuestionScore = objectMapper.writeValueAsString(propertyProposal.getCustomQuestionScore());
                    proposalRepository.customUpdateScore(propertyProposal.getId(), propertyProposal.getScore(), customQuestionScore);
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage(), e);
                }
            }
        });

        List<PropertyProposal> savedNewProposals = proposalRepository.saveAll(newProposals);
        elasticsearchIndexingService.indexProposal(ProposalEventType.PROPOSAL_CREATED, savedNewProposals.toArray(new PropertyProposal[0]));
        proposalsToOffer.addAll(savedNewProposals);

        proposalsToOffer.forEach(proposalOfferService::offerProposalIfNecessary);
    }

    public void refreshProposalCache(List<Property> properties) {
        properties.forEach(propertyCacheCountService::refreshProposalCache);
    }

    private PropertyProposal calculateProposal(Property property, PropertySearcherSearchProfile searchProfile) {
        if (requirementService.propertyMeetsRequirementsForCreatingProposal(property, searchProfile)) {
            return generateOrUpdateProposal(property, searchProfile);
        }

        return null;
    }

}
