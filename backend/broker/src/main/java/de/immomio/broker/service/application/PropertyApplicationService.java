package de.immomio.broker.service.application;

import de.immomio.broker.service.CustomQuestionResponseService;
import de.immomio.broker.service.calculator.BrokerCalculatorDelegate;
import de.immomio.broker.service.proposal.PropertyProposalService;
import de.immomio.broker.service.reporting.BrokerElasticsearchIndexingSenderService;
import de.immomio.constants.exceptions.RejectAllException;
import de.immomio.data.base.bean.score.ScoreBean;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.common.customQuestion.CustomQuestionResponse;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserProfileRepository;
import de.immomio.model.repository.core.shared.application.BasePropertyApplicationRepository;
import de.immomio.model.repository.core.shared.priosetCustomQuestion.BasePriosetCustomQuestionAssociationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class PropertyApplicationService {


    private final BasePropertyApplicationRepository propertyApplicationRepository;

    private final BasePriosetCustomQuestionAssociationRepository customQuestionAssociationRepository;

    private final CustomQuestionResponseService customQuestionResponseService;

    private final PropertyApplicationNotificationService propertyApplicationNotificationService;

    private final BrokerElasticsearchIndexingSenderService brokerElasticsearchIndexingService;

    private final PropertyProposalService propertyProposalService;

    private final BasePropertySearcherUserProfileRepository userProfileRepository;

    private final BrokerCalculatorDelegate calculatorDelegate;

    @Autowired
    public PropertyApplicationService(
            BasePropertyApplicationRepository propertyApplicationRepository,
            BasePriosetCustomQuestionAssociationRepository customQuestionAssociationRepository,
            CustomQuestionResponseService customQuestionResponseService,
            PropertyApplicationNotificationService propertyApplicationNotificationService,
            BrokerElasticsearchIndexingSenderService brokerElasticsearchIndexingService,
            PropertyProposalService propertyProposalService,
            BasePropertySearcherUserProfileRepository userProfileRepository,
            BrokerCalculatorDelegate calculatorDelegate) {
        this.propertyApplicationRepository = propertyApplicationRepository;
        this.customQuestionAssociationRepository = customQuestionAssociationRepository;
        this.customQuestionResponseService = customQuestionResponseService;
        this.propertyApplicationNotificationService = propertyApplicationNotificationService;
        this.brokerElasticsearchIndexingService = brokerElasticsearchIndexingService;
        this.propertyProposalService = propertyProposalService;
        this.userProfileRepository = userProfileRepository;
        this.calculatorDelegate = calculatorDelegate;
    }

    public void updateScoreWhenPropertyUpdated(Long propertyId) {
        List<PropertyApplication> applications = propertyApplicationRepository.findAllByPropertyId(propertyId);
        updateScoreWhenPropertyUpdated(applications);
    }

    public void updateScoreWhenUserUpdated(PropertySearcherUserProfile userProfile) {
        List<PropertyApplication> propertyApplications = propertyApplicationRepository.findAllByUserProfile(userProfile);
        updateScoreWhenPropertyUpdated(propertyApplications);
    }

    public PropertyApplication findById(Long id) {
        return propertyApplicationRepository.findById(id).orElse(null);
    }

    private void updateScoreWhenPropertyUpdated(List<PropertyApplication> propertyApplications) {
        propertyApplications.forEach(this::setScoreForApplication);
        propertyApplicationRepository.saveAll(propertyApplications);
    }

    private void setScoreForApplication(PropertyApplication application) {
        Property property = application.getProperty();
        PropertySearcherUserProfile userProfile = application.getUserProfile();
        ScoreBean scoreBean = calculatorDelegate.calculateScore(property, userProfile);
        application.setScore(scoreBean.getScore().doubleValue());
        application.setCustomQuestionScore(scoreBean.getCustomQuestionScore());
    }

    private void setScoreForCustomQuestionResponse(CustomQuestionResponse response) {
        List<PropertyApplication> propertyApplications = propertyApplicationRepository
                .findByUserProfileAndPropertyCustomer(response.getUserProfile(), response.getCustomQuestion().getCustomer());

        propertyApplications.forEach(this::setScoreForApplication);

        propertyApplicationRepository.saveAll(propertyApplications);
    }

    public void setScoreForCustomQuestionResponse(Long responseId) {
        CustomQuestionResponse customQuestionResponse = customQuestionResponseService.findById(responseId);
        if (customQuestionResponse != null) {
            setScoreForCustomQuestionResponse(customQuestionResponse);
        } else {
            log.error("custom question response with id {} not found", responseId);
        }
    }

    public void rejectRemainingApplicants(
            Property property,
            PropertySearcherUserProfile user
    ) throws RejectAllException {
        try {
            List<PropertyApplication> applications = propertyApplicationRepository.findAllByPropertyId(property.getId());
            List<PropertyApplication> rejectedApplications = applications.stream()
                            .filter(app -> applicationIsRejectedAndNotTenant(user, app))
                            .map(this::updateStatusToRejected)
                            .collect(Collectors.toList());

            propertyApplicationRepository.saveAll(rejectedApplications);
            propertyApplicationNotificationService.sendRejectedApplicationNotification(rejectedApplications);
            rejectedApplications.forEach(application -> brokerElasticsearchIndexingService.applicationRejected(
                    application,
                    application.getProperty().getUser()));
            rejectedApplications.stream()
                    .map(item -> item.getUserProfile().getId())
                    .forEach(this::generateProposalForUserAndUpdateScores);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);

            throw new RejectAllException(ex.getMessage(), ex);
        }
    }

    public void removeDkLevels(
            Property property,
            PropertySearcherUserProfile user
    )  {
        List<PropertyApplication> applications = propertyApplicationRepository.findAllByPropertyId(property.getId());
        List<PropertyApplication> dkLevelRemoved = applications.stream()
                .filter(app -> applicationIsNotTenant(user, app))
                .map(this::removeDkLevels)
                .collect(Collectors.toList());

        propertyApplicationRepository.saveAll(dkLevelRemoved);
    }

    private void generateProposalForUserAndUpdateScores(Long userId) {
        try {
            userProfileRepository.findById(userId).ifPresentOrElse(user -> {
                        propertyProposalService.generateForUser(user);
                        updateScoreWhenUserUpdated(user);
                    },
                    () -> log.error("user " + userId + " not found")
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private PropertyApplication updateStatusToRejected(PropertyApplication application) {
        application.setStatus(ApplicationStatus.REJECTED);

        return application;
    }

    private PropertyApplication removeDkLevels(PropertyApplication application) {
        LandlordCustomerSettings customerSettings = application.getProperty().getCustomer().getCustomerSettings();
        if (customerSettings != null && BooleanUtils.isTrue(customerSettings.getDeleteDkLevelAfterRenting())) {
            application.setDkApprovals(Collections.emptyList());
        }

        return application;
    }

    private boolean applicationIsNotTenant(PropertySearcherUserProfile userProfile, PropertyApplication app) {
        return app != null && !app.getUserProfile().equals(userProfile);
    }

    private boolean applicationIsRejectedAndNotTenant(PropertySearcherUserProfile userProfile, PropertyApplication app) {
        boolean applicationNotRejected = app.getStatus() != ApplicationStatus.REJECTED;

        return applicationIsNotTenant(userProfile, app) && applicationNotRejected;
    }

}
