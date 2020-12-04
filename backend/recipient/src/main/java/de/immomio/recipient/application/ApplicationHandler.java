package de.immomio.recipient.application;

import de.immomio.data.base.bean.score.ScoreBean;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.base.type.user.PropertySearcherUserType;
import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.entity.user.profile.details.AdditionalInformation;
import de.immomio.data.propertysearcher.entity.user.profile.details.CreditScreening;
import de.immomio.data.propertysearcher.entity.user.profile.details.Profession;
import de.immomio.data.shared.entity.email.ListingDetail;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.messaging.container.PropertyApplicationBrokerContainer;
import de.immomio.messaging.container.user.PSUserProfileMessageContainer;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserProfileRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserRepository;
import de.immomio.model.repository.core.shared.application.BasePropertyApplicationRepository;
import de.immomio.recipient.reporting.RecipientElasticsearchIndexingSenderService;
import de.immomio.recipient.service.RecipientPropertyCountRefreshCacheService;
import de.immomio.recipient.service.calculator.RecipientCalculatorDelegate;
import de.immomio.utils.TenantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static de.immomio.messaging.config.QueueConfigUtils.PropertyApplicationConfig;
import static de.immomio.messaging.config.QueueConfigUtils.PropertySearcherConfig;

@Slf4j
@Component
public class ApplicationHandler {

    private static final String EXCHANGE_NAME = PropertyApplicationConfig.EXCHANGE_NAME;
    private static final String ROUTING_KEY = PropertyApplicationConfig.ROUTING_KEY;

    private final PropertySearcherUserCreationService userCreationService;

    private final BasePropertySearcherUserProfileRepository userProfileRepository;

    private final BasePropertyApplicationRepository propertyApplicationRepository;

    private final RabbitTemplate rabbitTemplate;

    private final RecipientCalculatorDelegate recipientCalculatorDelegate;

    private final RecipientPropertyCountRefreshCacheService refreshCacheService;

    private final RecipientElasticsearchIndexingSenderService indexingService;

    private final ApplicationNotificationService applicationNotificationService;

    private final BasePropertySearcherUserRepository propertySearcherUserRepository;

    @Autowired
    public ApplicationHandler(PropertySearcherUserCreationService userCreationService,
            BasePropertySearcherUserProfileRepository userProfileRepository,
            BasePropertyApplicationRepository propertyApplicationRepository,
            RabbitTemplate rabbitTemplate,
            RecipientCalculatorDelegate recipientCalculatorDelegate,
            RecipientPropertyCountRefreshCacheService refreshCacheService,
            RecipientElasticsearchIndexingSenderService indexingService,
            ApplicationNotificationService applicationNotificationService,
            BasePropertySearcherUserRepository propertySearcherUserRepository) {
        this.userCreationService = userCreationService;
        this.userProfileRepository = userProfileRepository;
        this.propertyApplicationRepository = propertyApplicationRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.recipientCalculatorDelegate = recipientCalculatorDelegate;
        this.refreshCacheService = refreshCacheService;
        this.indexingService = indexingService;
        this.applicationNotificationService = applicationNotificationService;
        this.propertySearcherUserRepository = propertySearcherUserRepository;
    }

    public void applyUser(Property property, ListingDetail listingDetail, Portal portal) {
        if (propertyApplicationRepository.existsByUserProfileUserEmailAndProperty(listingDetail.getEmail(), property)) {
            log.error("an application for this email and porperty is already existing");
            return;
        }

        try {
            boolean newUser = false;
            PropertySearcherUser user = propertySearcherUserRepository.findByEmail(listingDetail.getEmail());

            if (user == null) {
                newUser = true;
                user = userCreationService.createEmptyUserStructure(listingDetail.getEmail(), PropertySearcherUserType.UNREGISTERED).getUser();
            }

            PropertySearcherUserProfile userProfile = getUserProfile(user, property, listingDetail);

            PropertyApplication application = createApplication(property, userProfile, listingDetail, portal);
            sendApplicationCreationEvent(application);
            applicationNotificationService.sendApplicationEmail(newUser, userProfile, property, listingDetail);
        } catch (Exception ex) {
            log.error("userProfile and application were not created due to an error during userProfile " +
                    "creation on SSO / DB level");
            log.error(ex.getMessage());
        }
    }

    public void removeInternalTenantPoolIfNecessary(PropertyApplication application) {
        PropertySearcherUserProfile userProfile = application.getUserProfile();
        if (TenantUtils.isUserInOtherPrivateTenantPool(userProfile, application.getProperty().getCustomer())) {
            removeInternalTenantPool(userProfile);
            sendPropertySearcherRefreshMessage(userProfile);
        }
    }

    private PropertySearcherUserProfile getUserProfile(PropertySearcherUser user, Property property, ListingDetail listingDetail) {
        if (user.getType() == PropertySearcherUserType.REGISTERED) {
            return user.getMainProfile();
        }

        return userCreationService.createSubUserProfile(
                PropertySearcherUserProfileType.ANONYMOUS,
                null,
                createUserProfileData(listingDetail),
                user,
                getInternalTenantPoolCustomer(property)
        );
    }

    private PropertyApplication createApplication(Property property, PropertySearcherUserProfile userProfile, ListingDetail details, Portal portal) {
        PropertyApplication application = new PropertyApplication(portal);
        application.setUserProfile(userProfile);
        application.setProperty(property);
        application.setText(details.getText());
        application.setStatus(ApplicationStatus.UNANSWERED);

        ScoreBean scoreBean = recipientCalculatorDelegate.calculateScore(property, userProfile);
        application.setScore(scoreBean.getScore().doubleValue());
        application.setCustomQuestionScore(scoreBean.getCustomQuestionScore());

        PropertyApplication save = propertyApplicationRepository.save(application);

        indexingService.applicationCreated(save, application.getProperty().getCustomer());
        refreshCacheService.refreshApplicationCache(property);
        removeInternalTenantPoolIfNecessary(save);
        return save;
    }

    private void sendApplicationCreationEvent(PropertyApplication application) {
        PropertyApplicationBrokerContainer container = new PropertyApplicationBrokerContainer();
        container.setId(application.getId());
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, container);
    }

    private PropertySearcherUserProfileData createUserProfileData(ListingDetail listingDetails) {
        PropertySearcherUserProfileData userProfile = new PropertySearcherUserProfileData();
        userProfile.setName(listingDetails.getName());
        userProfile.setFirstname(listingDetails.getFirstName());
        userProfile.setPhone(listingDetails.getTelephone());
        userProfile.setAdditionalInformation(new AdditionalInformation());
        userProfile.setProfession(new Profession());
        userProfile.setCreditScreening(new CreditScreening());

        return userProfile;
    }

    private void removeInternalTenantPool(PropertySearcherUserProfile userProfile) {
        userProfile.setTenantPoolCustomer(null);
        userProfileRepository.save(userProfile);
    }

    private void sendPropertySearcherRefreshMessage(PropertySearcherUserProfile userProfile) {
        PSUserProfileMessageContainer container = new PSUserProfileMessageContainer(userProfile);

        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(PropertySearcherConfig.EXCHANGE_NAME, PropertySearcherConfig.ROUTING_KEY, container);
    }

    private LandlordCustomer getInternalTenantPoolCustomer(Property property) {
        LandlordCustomer customer = property.getCustomer();

        if (customer.isInternalTenantPoolAllowed()) {
            return customer;
        }

        return null;
    }
}
