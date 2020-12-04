package de.immomio.broker.service;

import de.immomio.broker.service.application.PropertyApplicationService;
import de.immomio.broker.service.calculator.BrokerCalculatorDelegate;
import de.immomio.broker.service.property.portal.PropertyPortalPublishService;
import de.immomio.broker.service.publishlog.PublishLogService;
import de.immomio.constants.exceptions.RejectAllException;
import de.immomio.data.base.bean.score.CustomQuestionScoreBean;
import de.immomio.data.base.bean.score.ScoreBean;
import de.immomio.data.base.type.property.PropertyTask;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.publish.PublishLog;
import de.immomio.data.landlord.entity.property.publish.PublishState;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import de.immomio.messaging.container.PropertyApplicationBrokerContainer;
import de.immomio.messaging.container.property.PropertyMakeTenantContainer;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import de.immomio.model.repository.core.shared.application.BasePropertyApplicationRepository;
import de.immomio.model.repository.core.shared.tenant.BasePropertyTenantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class ApplicationMessageService {

    private final BasePropertyApplicationRepository basePropertyApplicationRepository;

    private final BasePropertyTenantRepository basePropertyTenantRepository;

    private final PropertyTenantProcessService propertyTenantProcessService;

    private final BasePropertyRepository propertyRepository;

    private final PropertyApplicationService propertyApplicationService;

    private final PropertyPortalPublishService publishService;

    private final PublishLogService publishLogService;

    private final BrokerCalculatorDelegate calculatorDelegate;

    @Autowired
    public ApplicationMessageService(
            BasePropertyApplicationRepository basePropertyApplicationRepository,
            BasePropertyTenantRepository basePropertyTenantRepository,
            PropertyTenantProcessService propertyTenantProcessService,
            BasePropertyRepository propertyRepository,
            PropertyApplicationService propertyApplicationService,
            PropertyPortalPublishService publishService,
            PublishLogService publishLogService,
            BrokerCalculatorDelegate calculatorDelegate) {
        this.basePropertyApplicationRepository = basePropertyApplicationRepository;
        this.basePropertyTenantRepository = basePropertyTenantRepository;
        this.propertyTenantProcessService = propertyTenantProcessService;
        this.propertyRepository = propertyRepository;
        this.propertyApplicationService = propertyApplicationService;
        this.publishService = publishService;
        this.publishLogService = publishLogService;
        this.calculatorDelegate = calculatorDelegate;
    }

    public void onMessage(PropertyApplicationBrokerContainer message) {
        basePropertyApplicationRepository.findById(message.getId()).ifPresentOrElse(propertyApplication -> {
                    //TODO add error monitoring

                    PropertySearcherUserProfile userProfile = propertyApplication.getUserProfile();

                    // TODO check "anonymous user"

                    if (userProfile == null) {
                        propertyApplication.setScore(Double.NEGATIVE_INFINITY);
                        propertyApplication.setCustomQuestionScore(CustomQuestionScoreBean.builder()
                                .scoreIncludingRange(BigDecimal.valueOf(Double.NEGATIVE_INFINITY))
                                .scoreExcludingRange(BigDecimal.valueOf(Double.NEGATIVE_INFINITY))
                                .build());

                        basePropertyApplicationRepository.save(propertyApplication);
                    } else {
                        try {
                            ScoreBean scoreBean = calculatorDelegate.calculateScore(propertyApplication.getProperty(), userProfile);
                            propertyApplication.setCustomQuestionScore(scoreBean.getCustomQuestionScore());
                            propertyApplication.setScore(scoreBean.getScore().doubleValue());
                            basePropertyApplicationRepository.save(propertyApplication);
                        } catch (Exception e) {
                            log.error("ERROR: preCalculate: propertyApplication -> " + propertyApplication.getId().toString(),
                                    e);
                        }
                    }
                },
                () -> log.info("PropertyApplication not found, ID = " + message.getId())
        );
    }

    public void onMessage(PropertyMakeTenantContainer container) {
        switch (container.getType()) {
            case APPLICATION:
                processPropertyApplicationRenting(container);
                break;
            case PROPERTY:
                processPropertyRenting(container);
                break;
            default:
                log.warn("Property renting type does not support");

        }
        deactivateFlatIfNecessary(container);
    }

    private void processPropertyRenting(PropertyMakeTenantContainer container) {
        propertyRepository.findById(container.getId())
                .ifPresentOrElse(
                        property -> propertyTenantProcessRenting(property, container),
                        () -> log.error("Property Tenant with ID {} not found", container.getId())
                );
    }

    private void propertyTenantProcessRenting(Property property, PropertyMakeTenantContainer container) {
        basePropertyTenantRepository
                .findById(container.getPropertyTenantId())
                .ifPresentOrElse(propertyTenant -> {
                            try {
                                propertyTenantProcessService.processRenting(property, propertyTenant, container.getRejectAll());
                            } catch (RejectAllException e) {
                                log.error(e.getMessage(), e);
                            }
                        },
                        () -> log.error("Property Tenant with ID {} not found", container.getPropertyTenantId())
                );
    }

    private void deactivateFlatIfNecessary(PropertyMakeTenantContainer container) {
        Optional<PropertyTenant> propertyTenantOpt = basePropertyTenantRepository.findById(container.getPropertyTenantId());
        propertyTenantOpt.ifPresent(propertyTenant -> {
            if (container.getDeactivate()) {
                Property property = propertyTenant.getProperty();
                property.setTask(PropertyTask.DEACTIVATE);
                PublishLog publishLog = publishLogService.createPublishLog(property);
                try {
                    publishService.deactivateFlat(property, publishLog);
                    publishLog.setPublishState(PublishState.SUCCESS);
                } catch (Exception e) {
                    publishService.handleErrorState(property, publishLog, e);
                }
                publishLogService.savePublishLog(publishLog);
            }
        });
    }

    private void processPropertyApplicationRenting(PropertyMakeTenantContainer container) {
        PropertyApplication application = propertyApplicationService.findById(container.getId());

        basePropertyTenantRepository.findById(container.getPropertyTenantId())
                .ifPresentOrElse(
                        propertyTenant -> propertyTenantProcessApplicationRenting(application, propertyTenant, container),
                        () -> log.error("Property Tenant with ID {} not found", container.getPropertyTenantId())
                );
    }

    private void propertyTenantProcessApplicationRenting(
            PropertyApplication application,
            PropertyTenant propertyTenant,
            PropertyMakeTenantContainer container
    ) {
        try {
            propertyTenantProcessService.processRenting(application, propertyTenant, container.getRejectAll());
        } catch (RejectAllException e) {
            log.error(e.getMessage(), e);
        }
    }
}
