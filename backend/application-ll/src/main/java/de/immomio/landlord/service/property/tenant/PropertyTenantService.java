package de.immomio.landlord.service.property.tenant;

import de.immomio.constants.exceptions.ImmomioRuntimeException;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.base.type.property.PropertyStatus;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.tenant.PropertyTenantBean;
import de.immomio.data.shared.entity.property.TenantInfo;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import de.immomio.landlord.service.appointment.AppointmentService;
import de.immomio.landlord.service.property.PropertyService;
import de.immomio.landlord.service.reporting.indexing.delegate.LandlordApplicationIndexingDelegate;
import de.immomio.landlord.service.reporting.indexing.delegate.LandlordPropertyIndexingDelegate;
import de.immomio.messaging.container.property.PropertyMakeTenantContainer;
import de.immomio.messaging.container.property.PropertyMakeTenantType;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import de.immomio.model.repository.shared.tenant.PropertyTenantRepository;
import de.immomio.service.landlord.property.tenant.PropertyTenantConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static de.immomio.messaging.config.QueueConfigUtils.PropertyMakeTenantConfig;
import static de.immomio.messaging.container.property.PropertyMakeTenantType.APPLICATION;
import static de.immomio.messaging.container.property.PropertyMakeTenantType.PROPERTY;

@Slf4j
@Service
public class PropertyTenantService {

    private static final String ONLY_PROPERTY_OR_APPLICATION_SHOULD_BE_SET = "ONLY_PROPERTY_OR_APPLICATION_SHOULD_BE_SET_L";
    private static final String PROPERTY_OR_APPLICATION_MUST_BE_SET = "PROPERTY_OR_APPLICATION_MUST_BE_SET_L";
    private static final String APPLICANT_HAS_NO_INTENT_TO_RENT = "APPLICANT_HAS_NO_INTENT_TO_RENT_L";
    private static final String ERROR_PROPERTY_ALREADY_HAS_TENANT = "ERROR_PROPERTY_ALREADY_HAS_TENANT_L";
    private final PropertyTenantRepository propertyTenantRepository;
    private final LandlordPropertyIndexingDelegate propertyIndexingDelegate;
    private final LandlordApplicationIndexingDelegate applicationIndexingDelegate;
    private final AppointmentService appointmentService;
    private final RabbitTemplate rabbitTemplate;
    private final PropertyService propertyService;
    private final PropertyTenantConverter propertyTenantConverter;

    @Autowired
    public PropertyTenantService(
            PropertyTenantRepository propertyTenantRepository,
            LandlordPropertyIndexingDelegate propertyIndexingDelegate,
            LandlordApplicationIndexingDelegate applicationIndexingDelegate,
            AppointmentService appointmentService,
            RabbitTemplate rabbitTemplate,
            PropertyService propertyService,
            PropertyTenantConverter propertyTenantConverter) {
        this.propertyTenantRepository = propertyTenantRepository;
        this.propertyIndexingDelegate = propertyIndexingDelegate;
        this.applicationIndexingDelegate = applicationIndexingDelegate;
        this.appointmentService = appointmentService;
        this.rabbitTemplate = rabbitTemplate;
        this.propertyService = propertyService;
        this.propertyTenantConverter = propertyTenantConverter;

    }

    public PropertyTenantBean makeTenant(Property property, PropertyApplication application, Date start, Boolean rejectAll, Boolean deactivate) {
        validateRentedParams(property, application);

        if (application != null) {
            return propertyTenantConverter.convertToTenantBean(makeTenantWithApplication(application, start, rejectAll, deactivate));
        }

        return propertyTenantConverter.convertToTenantBean(makeTenantWithProperty(property, start, rejectAll, deactivate));
    }

    private PropertyTenant makeTenantWithProperty(Property property, Date start, Boolean rejectAll, Boolean deactivate) {
        validateForRent(property);

        PropertyTenant propertyTenant = processTenant(property, start, null);
        sendPropertyMakeTenantMessage(property.getId(), null, rejectAll, deactivate, PROPERTY);

        return propertyTenant;
    }

    private PropertyTenant makeTenantWithApplication(PropertyApplication application, Date start, Boolean rejectAll, Boolean deactivate) {
        validateForRent(application);

        PropertyTenant propertyTenant = processTenant(application.getProperty(), start, application.getUserProfile());
        applicationIndexingDelegate.acceptedAsTenant(application);
        sendPropertyMakeTenantMessage(application.getId(), propertyTenant.getId(), rejectAll, deactivate, APPLICATION);

        return propertyTenant;
    }

    private PropertyTenant processTenant(Property property, Date start, PropertySearcherUserProfile user) {
        PropertyTenant propertyTenant = savePropertyTenant(property, start, user);

        propertyService.updatePropertyState(property, PropertyStatus.DEFAULT);
        appointmentService.resetAppointmentAcceptances(property.getAppointments());
        propertyIndexingDelegate.propertyRented(property);

        return propertyTenant;
    }

    private void validateForRent(Property property) {
        if (property.getTenant() != null) {
            log.error("unable to save propertyTenant for property: " + property.getId() + " " + ERROR_PROPERTY_ALREADY_HAS_TENANT);
            throw new ImmomioRuntimeException(ERROR_PROPERTY_ALREADY_HAS_TENANT);

        }
    }

    private void validateForRent(PropertyApplication application) {
        Property property = application.getProperty();
        validateForRent(property);
        if (application.getStatus() == ApplicationStatus.NO_INTENT) {
            log.error("unable to save propertyTenant for property: " + property.getId() + " " + APPLICANT_HAS_NO_INTENT_TO_RENT);
            throw new ImmomioRuntimeException(APPLICANT_HAS_NO_INTENT_TO_RENT);
        }
    }

    private PropertyTenant savePropertyTenant(Property property, Date start, PropertySearcherUserProfile user) {
        PropertyTenant propertyTenant = createPropertyTenant(property, start, user);

        return propertyTenantRepository.save(propertyTenant);
    }

    private void validateRentedParams(Property property, PropertyApplication application) {
        if (property == null && application == null) {
            throw new ImmomioRuntimeException(PROPERTY_OR_APPLICATION_MUST_BE_SET);
        }
        if (property != null && application != null) {
            throw new ImmomioRuntimeException(ONLY_PROPERTY_OR_APPLICATION_SHOULD_BE_SET);
        }
    }

    private PropertyTenant createPropertyTenant(Property property, Date start, PropertySearcherUserProfile userProfile) {
        Optional<PropertySearcherUserProfile> optionalPropertySearcherUserProfile = Optional.ofNullable(userProfile);
        PropertyTenant propertyTenant = new PropertyTenant();
        propertyTenant.setContractStart(start);
        propertyTenant.setProperty(property);
        propertyTenant.setUserProfile(optionalPropertySearcherUserProfile.orElse(null));

        optionalPropertySearcherUserProfile.ifPresent(foundUser -> {
            TenantInfo tenantInfo = new TenantInfo();
            PropertySearcherUserProfileData profile = foundUser.getData();
            tenantInfo.setFirstName(profile.getFirstname());
            tenantInfo.setName(profile.getName());

            propertyTenant.setTenantInfo(tenantInfo);
        });

        return propertyTenant;
    }

    private void sendPropertyMakeTenantMessage(Long id, Long tenantId, Boolean rejectAll, Boolean deactivate, PropertyMakeTenantType type) {
        PropertyMakeTenantContainer message = new PropertyMakeTenantContainer(id, tenantId, rejectAll, type, deactivate);

        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(PropertyMakeTenantConfig.EXCHANGE_NAME,
                PropertyMakeTenantConfig.ROUTING_KEY,
                message);
    }

}
