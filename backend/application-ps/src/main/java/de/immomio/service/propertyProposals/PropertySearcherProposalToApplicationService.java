package de.immomio.service.propertyProposals;

import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.service.application.PropertyApplicationNotificationService;
import de.immomio.service.application.PropertySearcherApplicationScoreService;
import de.immomio.service.property.cache.PropertyCountRefreshCacheService;
import de.immomio.service.reporting.PropertySearcherApplicationIndexingDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */
@Service
public class PropertySearcherProposalToApplicationService {
    private PropertyApplicationRepository applicationRepository;
    private PropertyApplicationNotificationService propertyApplicationNotificationService;
    private PropertyCountRefreshCacheService countRefreshCacheService;
    private PropertySearcherApplicationIndexingDelegate applicationIndexingDelegate;
    private PropertySearcherApplicationScoreService applicationScoreService;

    @Autowired
    public PropertySearcherProposalToApplicationService(PropertyApplicationRepository applicationRepository,
            PropertyApplicationNotificationService propertyApplicationNotificationService,
            PropertyCountRefreshCacheService countRefreshCacheService,
            PropertySearcherApplicationIndexingDelegate applicationIndexingDelegate,
            PropertySearcherApplicationScoreService applicationScoreService) {
        this.applicationRepository = applicationRepository;
        this.propertyApplicationNotificationService = propertyApplicationNotificationService;
        this.countRefreshCacheService = countRefreshCacheService;
        this.applicationIndexingDelegate = applicationIndexingDelegate;
        this.applicationScoreService = applicationScoreService;
    }

    public PropertyApplication convertProposalToApplication(PropertyProposal proposal) {
        PropertySearcherUserProfile userProfile = proposal.getUserProfile();
        Property property = proposal.getProperty();

        PropertyApplication application = new PropertyApplication();
        application.setStatus(ApplicationStatus.UNANSWERED);
        application.setProperty(property);
        application.setUserProfile(userProfile);

        applicationScoreService.setScoreForApplication(application);

        application = applicationRepository.save(application);

        propertyApplicationNotificationService.applicationConfirmed(userProfile, property);
        countRefreshCacheService.refreshApplicationCache(application.getProperty());
        applicationIndexingDelegate.appliedPool(application);

        return application;
    }
}
