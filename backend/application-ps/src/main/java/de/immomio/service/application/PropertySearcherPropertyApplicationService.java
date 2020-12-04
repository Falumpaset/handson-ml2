package de.immomio.service.application;

import de.immomio.beans.IdBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.ApplicationSaveException;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.landlord.bean.property.dk.DkApprovalLevel;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.dk.DkApproval;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.application.PropertyApplicationBean;
import de.immomio.data.shared.bean.application.PropertySearcherPropertyApplicationBean;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.messaging.container.user.PSUserProfileMessageContainer;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserProfileRepository;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.service.conversation.ConversationConfigService;
import de.immomio.service.property.PropertyService;
import de.immomio.service.property.cache.PropertyCountRefreshCacheService;
import de.immomio.service.reporting.PropertySearcherApplicationIndexingDelegate;
import de.immomio.service.searchProfile.SearchProfileService;
import de.immomio.service.sender.PropertySearcherMessageSender;
import de.immomio.service.shortUrl.ShortUrlService;
import de.immomio.utils.TenantUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PropertySearcherPropertyApplicationService {

    public static final String APPLICATION_NOT_FOUND = "APPLICATION_NOT_FOUND";
    private static final String INTENT_STATUS_NOT_ACCEPTABLE = "INTENT_STATUS_NOT_ACCEPTABLE_L";
    private static final String APPLICATION_NOT_IN_ACCEPTED_STATUS = "APPLICATION_NOT_IN_ACCEPTED_STATUS_L";
    private static final String APPLICATION_ONLY_ALLOWED_FOR_FLAT_L = "APPLICATION_ONLY_ALLOWED_FOR_FLAT_L";
    private final PropertySearcherApplicationScoreService applicationScoreService;

    private final PropertyApplicationRepository propertyApplicationRepository;

    private final PropertyCountRefreshCacheService propertyCacheCountService;

    private final PropertySearcherMessageSender propertySearcherMessageSender;

    private final ShortUrlService shortUrlService;

    private final PropertySearcherApplicationIndexingDelegate applicationIndexingDelegate;

    private final PropertySearcherUserProfileRepository propertySearcherUserProfileRepository;

    private final ConversationConfigService conversationConfigService;

    private final PropertyService propertyService;

    private final SearchProfileService searchProfileService;

    private final PropertyApplicationNotificationService propertyApplicationNotificationService;

    private final PropertyApplicationCollectionService applicationCollectionService;

    @Autowired
    public PropertySearcherPropertyApplicationService(PropertySearcherApplicationScoreService applicationScoreService,
            PropertyApplicationRepository propertyApplicationRepository,
            PropertyCountRefreshCacheService propertyCacheCountService,
            PropertySearcherMessageSender propertySearcherMessageSender,
            ShortUrlService shortUrlService,
            PropertySearcherApplicationIndexingDelegate applicationIndexingDelegate,
            PropertySearcherUserProfileRepository propertySearcherUserProfileRepository,
            ConversationConfigService conversationConfigService,
            PropertyService propertyService,
            SearchProfileService searchProfileService,
            PropertyApplicationNotificationService propertyApplicationNotificationService,
            PropertyApplicationCollectionService applicationCollectionService) {
        this.applicationScoreService = applicationScoreService;
        this.propertyApplicationRepository = propertyApplicationRepository;
        this.propertyCacheCountService = propertyCacheCountService;
        this.propertySearcherMessageSender = propertySearcherMessageSender;
        this.shortUrlService = shortUrlService;
        this.applicationIndexingDelegate = applicationIndexingDelegate;
        this.propertySearcherUserProfileRepository = propertySearcherUserProfileRepository;
        this.conversationConfigService = conversationConfigService;
        this.propertyService = propertyService;
        this.searchProfileService = searchProfileService;
        this.propertyApplicationNotificationService = propertyApplicationNotificationService;
        this.applicationCollectionService = applicationCollectionService;
    }

    public PropertySearcherPropertyApplicationBean createNewApplication(Long propertyId, PropertySearcherUserProfile userProfile, String shortUrlToken) throws
            ApplicationSaveException {
        Property property = propertyService.customFindById(propertyId);
        PropertyApplication application = createNewApplication(property, userProfile, shortUrlToken);
        return applicationCollectionService.collectApplicationBean(application);
    }

    public PropertyApplication createNewApplication(Property property, PropertySearcherUserProfile userProfile, String shortUrlToken) throws
            ApplicationSaveException {

        if (property.getType() != PropertyType.FLAT) {
            throw new ApiValidationException(APPLICATION_ONLY_ALLOWED_FOR_FLAT_L);
        }

        PropertyApplication existingApplication = propertyApplicationRepository.findFirstByUserProfileAndProperty(userProfile, property);
        if (existingApplication != null) {
            throw new DataIntegrityViolationException("application already exists");
        }

        Portal portal = StringUtils.isNotBlank(shortUrlToken) ? shortUrlService.getPortalFromShortUrl(shortUrlToken) : null;
        PropertyApplication newApplication = new PropertyApplication(portal);

        newApplication.setUserProfile(userProfile);
        newApplication.setProperty(property);
        newApplication.setStatus(ApplicationStatus.UNANSWERED);
        newApplication.getDkApprovals().add(new DkApproval(newApplication, DkApprovalLevel.DK1));

        applicationScoreService.setScoreForApplication(newApplication);
        try {
            PropertyApplication saved = propertyApplicationRepository.save(newApplication);
            removeInternalTenantPoolIfNecessary(saved);

            applicationIndexingDelegate.appliedExternal(saved);
            propertyCacheCountService.refreshApplicationCache(property);

            if (userProfile.getUser().getProspectOptIn().isOptInForProspect() && userProfile.getUser().isRegistered()) {
                searchProfileService.create(property, userProfile);
            }

            if (userProfile.getUser().isRegistered()) {
                propertyApplicationNotificationService.applicationConfirmed(userProfile, property);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationSaveException(e.getMessage(), e);
        }
        return newApplication;
    }

    public void removeInternalTenantPoolIfNecessary(PropertyApplication application) {
        PropertySearcherUserProfile userProfile = application.getUserProfile();
        if (TenantUtils.isUserInOtherPrivateTenantPool(userProfile, application.getProperty().getCustomer())) {
            userProfile.setTenantPoolCustomer(null);
            propertySearcherUserProfileRepository.save(userProfile);
            propertySearcherMessageSender.sendUserUpdateMessage(new PSUserProfileMessageContainer(userProfile));
        }
    }

    public PropertyApplication save(PropertyApplication application) {
        return propertyApplicationRepository.save(application);
    }

    public PropertySearcherPropertyApplicationBean findApplicationBeanById(Long id) {
        return applicationCollectionService.collectApplicationBean(findById(id));
    }

    public PropertyApplication findById(Long id) {
        return propertyApplicationRepository.findById(id)
                .orElseThrow(() -> new ApiValidationException(APPLICATION_NOT_FOUND));
    }

    public void delete(PropertyApplication application) {
        propertyApplicationRepository.delete(application);
        propertyCacheCountService.refreshApplicationCache(application.getProperty());
        propertySearcherMessageSender.sendUserUpdateMessage(new PSUserProfileMessageContainer(application.getUserProfile()));
        applicationIndexingDelegate.applicationDeleted(application);
    }

    public void updateIntentOfApplication(Long applicationId, ApplicationStatus intent) {
        updateIntentOfApplication(findById(applicationId), intent);
    }

    public void updateIntentOfApplication(PropertyApplication application, ApplicationStatus intent) {
        List<ApplicationStatus> acceptedStatuses = Arrays.asList(ApplicationStatus.INTENT, ApplicationStatus.NO_INTENT);
        if (!acceptedStatuses.contains(intent)) {
            throw new ApiValidationException(INTENT_STATUS_NOT_ACCEPTABLE);
        }

        if (application.getStatus() == intent) {
            return;
        }

        List<ApplicationStatus> applicationStatuses = Arrays.asList(ApplicationStatus.NO_INTENT, ApplicationStatus.INTENT, ApplicationStatus.PRE_TENANT_VIEWING,
                ApplicationStatus.ACCEPTED);

        if (!applicationStatuses.contains(application.getStatus()) && !application.isAskedForIntent()) {
            throw new ApiValidationException(APPLICATION_NOT_IN_ACCEPTED_STATUS);
        }

        application.setStatus(intent);

        if (intent == ApplicationStatus.INTENT) {
            boolean dk2NotSetYet = application.getDkApprovals().stream().noneMatch(dkApproval -> dkApproval.getLevel() == DkApprovalLevel.DK2);

            if (dk2NotSetYet) {
                application.getDkApprovals().add(new DkApproval(application, DkApprovalLevel.DK2));
            }
            applicationIndexingDelegate.intentGiven(application);
        } else if (intent == ApplicationStatus.NO_INTENT) {
            applicationIndexingDelegate.noIntentGiven(application);

            if (application.getProperty().getCustomer().getCustomerSettings().getDkLevelCustomerSettings().getIntentNeededBeforeDk2()) {
                application.getDkApprovals().add(new DkApproval(application, DkApprovalLevel.DK1));
            }
        }

        save(application);

        propertyCacheCountService.refreshApplicationCache(application.getProperty());
    }

    public Page<PropertySearcherPropertyApplicationBean> findByUserProfile(PropertySearcherUserProfile userProfile, Pageable pageable) {
        Page<PropertyApplication> applications = propertyApplicationRepository.findFlatApplicationByUserProfile(userProfile, pageable);

        return new PageImpl<>(applications.stream().parallel().map(
                applicationCollectionService::collectApplicationBean).collect(Collectors.toList()), pageable,
                applications.getTotalElements());
    }

    public Page<PropertySearcherPropertyApplicationBean> findAvailableForConversation(PropertySearcherUserProfile userProfile) {
        List<PropertyApplication> applications = propertyApplicationRepository.findByUserProfileAndConversationIsNull(userProfile);
        applications.removeIf(application -> {
            boolean allowedToSendMessage = conversationConfigService.isAllowedToSendMessage(application.getId());
            return !allowedToSendMessage;
        });

        return new PageImpl<>(applications.stream().parallel().map(
                applicationCollectionService::collectApplicationBean).collect(Collectors.toList()));
    }

    public Page<PropertySearcherPropertyApplicationBean> findApplicationsWithRentedFlatsById() {
        List<PropertyApplication> applications = propertyApplicationRepository.findApplicationsWithRentedFlatsById();

        return new PageImpl<>(applications.stream().parallel().map(
                applicationCollectionService::collectApplicationBean).collect(Collectors.toList()));
    }

    public IdBean exists(PropertySearcherUserProfile userProfile, Long propertyId) {
        return propertyApplicationRepository.getByUserProfileAndProperty(userProfile, propertyId)
                .map(propertyApplication -> new IdBean(propertyApplication.getId()))
                .orElse(new IdBean());
    }
}
