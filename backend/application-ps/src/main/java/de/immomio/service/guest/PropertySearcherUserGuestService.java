package de.immomio.service.guest;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.ApplicationSaveException;
import de.immomio.constants.exceptions.ImmomioRuntimeException;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.base.type.user.PropertySearcherUserType;
import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBranding;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.bean.user.PropertySearcherUserRegisterResultBean;
import de.immomio.data.propertysearcher.bean.user.guest.PropertySearcherGuestUserApplicationPossibleBean;
import de.immomio.data.propertysearcher.bean.user.guest.PropertySearcherGuestUserApplyBean;
import de.immomio.data.propertysearcher.bean.user.guest.PropertySearcherGuestUserRegisterBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.application.PropertySearcherGuestPropertyApplicationBean;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionResponseQuestionBean;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserRepository;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.model.repository.shared.property.PropertyRepository;
import de.immomio.propertysearcher.bean.AppointmentAcceptanceBean;
import de.immomio.propertysearcher.bean.AppointmentBundleBean;
import de.immomio.propertysearcher.bean.PropertySearcherRegisterApplyBean;
import de.immomio.security.common.bean.ApplyGuestUserToken;
import de.immomio.security.common.bean.GuestUserToken;
import de.immomio.security.service.JWTTokenService;
import de.immomio.service.application.PropertyApplicationCollectionService;
import de.immomio.service.application.PropertyApplicationConverter;
import de.immomio.service.application.PropertySearcherPropertyApplicationService;
import de.immomio.service.appointment.AppointmentAcceptanceService;
import de.immomio.service.appointment.AppointmentService;
import de.immomio.service.customQuestion.CustomQuestionResponseService;
import de.immomio.service.indexing.PropertySearcherProfileChangedIndexingService;
import de.immomio.service.property.cache.PropertyCountRefreshCacheService;
import de.immomio.service.propertySearcher.PropertySearcherUserService;
import de.immomio.service.propertySearcher.onboarding.PropertySearcherOnboardingSearchProfileService;
import de.immomio.service.propertySearcher.onboarding.PropertySearcherOnboardingService;
import de.immomio.utils.EmailAddressUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PropertySearcherUserGuestService {

    private static final String APPLICATION_NOT_FOUND_L = "APPLICATION_NOT_FOUND_L";
    private static final String USER_PROFILE_NOT_GUEST_L = "USER_PROFILE_NOT_GUEST_L";
    private static final String CANCELING_NOT_ALLOWED_L = "CANCELING_NOT_ALLOWED_L";
    private static final String GUEST_MODE_FOR_CUSTOMER_NOT_ALLOWED_L = "GUEST_MODE_FOR_CUSTOMER_NOT_ALLOWED_L";
    private static final String PROPERTY_NOT_FOUND_L = "PROPERTY_NOT_FOUND_L";
    private static final String EMAIL_MISSING_L = "EMAIL_MISSING_L";

    private static final String EMAIL_ADDRESS_INVALID_L = "EMAIL_ADDRESS_INVALID_L";
    private static final String USER_ALREADY_REGISTERED_L = "USER_ALREADY_REGISTERED_L";

    private final JWTTokenService jwtTokenService;
    private final PropertySearcherUserRepository userRepository;
    private final PropertySearcherPropertyApplicationService applicationService;
    private final PropertyApplicationRepository applicationRepository;
    private final AppointmentService appointmentService;
    private final AppointmentAcceptanceService appointmentAcceptanceService;
    private final PropertySearcherUserGuestNotificationService userGuestNotificationService;
    private final PropertySearcherUserGuestApplicationService guestApplicationService;
    private final PropertySearcherOnboardingService onboardingService;
    private final CustomQuestionResponseService customQuestionResponseService;
    private final PropertyRepository propertyRepository;
    private final PropertySearcherUserService userService;
    private final PropertySearcherProfileChangedIndexingService profileChangedIndexingService;
    private final PropertyCountRefreshCacheService propertyCacheCountService;
    private final PropertySearcherOnboardingSearchProfileService propertySearcherOnboardingSearchProfileService;
    private final PropertyApplicationCollectionService applicationCollectionService;

    public PropertySearcherUserGuestService(JWTTokenService jwtTokenService,
            PropertySearcherUserRepository userRepository,
            PropertySearcherPropertyApplicationService applicationService,
            PropertyApplicationRepository applicationRepository,
            AppointmentService appointmentService,
            AppointmentAcceptanceService appointmentAcceptanceService,
            PropertySearcherUserGuestNotificationService userGuestNotificationService,
            PropertySearcherUserGuestApplicationService guestApplicationService,
            PropertySearcherOnboardingService onboardingService,
            CustomQuestionResponseService customQuestionResponseService,
            PropertyRepository propertyRepository,
            PropertySearcherUserService userService,
            PropertySearcherProfileChangedIndexingService profileChangedIndexingService,
            PropertyCountRefreshCacheService propertyCacheCountService,
            PropertySearcherOnboardingSearchProfileService propertySearcherOnboardingSearchProfileService,
            PropertyApplicationCollectionService applicationCollectionService) {
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
        this.applicationService = applicationService;
        this.applicationRepository = applicationRepository;
        this.appointmentService = appointmentService;
        this.appointmentAcceptanceService = appointmentAcceptanceService;
        this.userGuestNotificationService = userGuestNotificationService;
        this.guestApplicationService = guestApplicationService;
        this.onboardingService = onboardingService;
        this.customQuestionResponseService = customQuestionResponseService;
        this.propertyRepository = propertyRepository;
        this.userService = userService;
        this.profileChangedIndexingService = profileChangedIndexingService;
        this.propertyCacheCountService = propertyCacheCountService;
        this.propertySearcherOnboardingSearchProfileService = propertySearcherOnboardingSearchProfileService;
        this.applicationCollectionService = applicationCollectionService;
    }

    public PropertySearcherUserRegisterResultBean register(String token, PropertySearcherGuestUserRegisterBean registerBean) {
        PropertyApplication propertyApplication = getApplicationFromToken(token);

        PropertySearcherUserProfile userProfile = onboardingService.registerGuestPropertySearcher(propertyApplication.getUserProfile(), registerBean);
        userGuestNotificationService.sendEmailVerifyNotification(userProfile);

        profileChangedIndexingService.indexProfileChanged(propertyApplication.getUserProfile().getId());
        propertySearcherOnboardingSearchProfileService.createSearchProfileForUserIfNecessary(userProfile, registerBean.getSearchProfile());

        return userService.createRegisterResultBean(userProfile);
    }

    public PropertySearcherGuestUserApplicationPossibleBean isGuestPropertyApplicationPossible(String email, Long propertyId) {
        Property property = propertyRepository.customFindById(propertyId).orElseThrow(() -> new ApiValidationException(PROPERTY_NOT_FOUND_L));
        if (!property.getCustomer().allowGuestMode()) {
            throw new ApiValidationException(GUEST_MODE_FOR_CUSTOMER_NOT_ALLOWED_L);
        }

        if (email == null) {
            throw new ApiValidationException(EMAIL_MISSING_L);
        }
        email = email.toLowerCase();

        PropertySearcherUser user = userRepository.findByEmail(email);
        if (user != null && user.getType() == PropertySearcherUserType.REGISTERED) {
            userGuestNotificationService.sendRegisterReminder(user.getMainProfile(), property);
            return PropertySearcherGuestUserApplicationPossibleBean.builder().alreadyRegistered(true).build();
        }

        PropertyApplication propertyApplication = applicationRepository.findByEmailAndProperty(email, property);
        if (propertyApplication != null && propertyApplication.getUserProfile().getType() == PropertySearcherUserProfileType.GUEST) {
            return PropertySearcherGuestUserApplicationPossibleBean.builder().alreadyGuest(true).build();
        }

        if (!guestApplicationService.isGuestPropertyApplicationPossible(user, property, propertyApplication)) {
            return PropertySearcherGuestUserApplicationPossibleBean.builder().build();
        }

        return PropertySearcherGuestUserApplicationPossibleBean.builder()
                .applicationPossible(true)
                .token(getApplicationPossibleToken(email, propertyId))
                .build();
    }

    public PropertySearcherUserRegisterResultBean registerAndApplyToProperty(String token, PropertySearcherRegisterApplyBean registerBean) {
        ApplyGuestUserToken applyToken = jwtTokenService.validateApplyGuestUserToken(token);
        Property property = propertyRepository.customFindById(applyToken.getPropertyId()).orElseThrow(() -> new ApiValidationException(PROPERTY_NOT_FOUND_L));

        registerBean.setEmail(applyToken.getEmail());

        PropertySearcherUserProfile userProfile = userService.registerBase(registerBean);

        try {
            applicationService.createNewApplication(property, userProfile, registerBean.getShortUrlToken());
        } catch (ApplicationSaveException e) {
            throw new ImmomioRuntimeException();
        }

        return userService.createRegisterResultBean(userProfile);
    }

    public void applyToPropertyAsGuest(String token, PropertySearcherGuestUserApplyBean applyBean) {
        ApplyGuestUserToken applyToken = jwtTokenService.validateApplyGuestUserToken(token);

        String email = applyToken.getEmail();
        if (email == null || EmailAddressUtils.isInvalid(email)) {
            throw new ApiValidationException(EMAIL_ADDRESS_INVALID_L);
        }

        PropertySearcherUser user = userRepository.findByEmail(applyToken.getEmail());

        if (user != null && user.getType() == PropertySearcherUserType.REGISTERED) {
            throw new ApiValidationException(USER_ALREADY_REGISTERED_L);
        }

        Property property = propertyRepository.customFindById(applyToken.getPropertyId()).orElseThrow(() -> new ApiValidationException(PROPERTY_NOT_FOUND_L));

        PropertyApplication existingApplication = applicationRepository.findByEmailAndProperty(email, property);

        boolean shouldIndexProfileChangeEvent = existingApplication != null &&
                existingApplication.getUserProfile().getType() == PropertySearcherUserProfileType.ANONYMOUS;

        PropertyApplication propertyApplication = guestApplicationService.applyToPropertyAsGuest(user, property, applyBean, email);

        userGuestNotificationService.sendApplicationConfirmed(propertyApplication, getGuestUserToken(propertyApplication));
        userService.sendUserRefreshMessage(propertyApplication.getUserProfile().getId());
        if (shouldIndexProfileChangeEvent) {
            profileChangedIndexingService.indexProfileChanged(propertyApplication.getUserProfile().getId());
        }
        propertyCacheCountService.refreshApplicationCache(propertyApplication.getProperty());

    }

    public void deleteGuest(String token) {
        PropertyApplication application = getApplicationFromToken(token);

        guestApplicationService.deleteGuest(application);
    }

    public List<AppointmentBundleBean> getAppointmentBundleBeans(String token) {
        PropertyApplication application = getApplicationFromToken(token);
        return appointmentService.getAppointmentBundleBeans(application.getUserProfile());
    }

    public AppointmentAcceptanceBean acceptViewing(Appointment appointment, String token) {
        PropertyApplication application = getApplicationFromToken(token);
        AppointmentAcceptance appointmentAcceptance = appointmentService.acceptViewing(appointment, application);
        return appointmentService.collectAppointmentAcceptanceBean(appointmentAcceptance);
    }

    public AppointmentAcceptanceBean cancel(AppointmentAcceptance appointmentAcceptance, String token) {
        PropertyApplication application = getApplicationFromToken(token);

        if (!application.equals(appointmentAcceptance.getApplication())) {
            throw new ApiValidationException(CANCELING_NOT_ALLOWED_L);
        }

        appointmentAcceptance = appointmentAcceptanceService.cancel(appointmentAcceptance);
        return appointmentService.collectAppointmentAcceptanceBean(appointmentAcceptance);
    }

    public PropertySearcherGuestPropertyApplicationBean getApplication(String token) {
        return applicationCollectionService.collectGuestApplicationBean(getApplicationFromToken(token));
    }

    public PropertyApplication getApplicationFromToken(String token) {
        GuestUserToken guestUserToken = jwtTokenService.validateGuestUserToken(token);
        PropertyApplication application = applicationRepository.findById(guestUserToken.getApplicationId())
                .orElseThrow(() -> new ApiValidationException(APPLICATION_NOT_FOUND_L));

        if (application.getUserProfile().getType() != PropertySearcherUserProfileType.GUEST) {
            throw new ApiValidationException(USER_PROFILE_NOT_GUEST_L);
        }

        return application;
    }

    public void updateIntentOfApplication(boolean intent, String token) {
        PropertyApplication application = getApplicationFromToken(token);
        applicationService.updateIntentOfApplication(application, intent ? ApplicationStatus.INTENT : ApplicationStatus.NO_INTENT);
    }

    public List<CustomQuestionResponseQuestionBean> getCustomQuestionResponses(String token) {
        PropertyApplication application = getApplicationFromToken(token);

        return customQuestionResponseService.getCustomQuestionResponses(application.getUserProfile());
    }

    public LandlordCustomerBranding getUserProfileBranding(String token) {
        PropertyApplication application = getApplicationFromToken(token);

        LandlordCustomerBranding customerBranding = application.getProperty().getCustomer().getCustomerBranding();
        return customerBranding != null ? customerBranding : new LandlordCustomerBranding();
    }

    private String getGuestUserToken(PropertyApplication application) {
        try {
            return jwtTokenService.generateGuestUserToken(application);
        } catch (IOException e) {
            throw new ImmomioRuntimeException("TOKEN_GENERATION_FAILED_L");
        }
    }

    private String getApplicationPossibleToken(String email, Long propertyId) {
        try {
            return jwtTokenService.generateApplyGuestUserToken(UUID.randomUUID(), email, propertyId);
        } catch (IOException e) {
            throw new ImmomioRuntimeException("TOKEN_GENERATION_FAILED_L");
        }
    }
}
