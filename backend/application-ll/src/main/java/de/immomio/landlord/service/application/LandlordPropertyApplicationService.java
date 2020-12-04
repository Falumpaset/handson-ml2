package de.immomio.landlord.service.application;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.landlord.bean.property.PropertyMailBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileEmailBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.appointment.acceptance.AppointmentAcceptanceService;
import de.immomio.landlord.service.property.notification.PropertyApplicationNotificationService;
import de.immomio.landlord.service.reporting.indexing.delegate.LandlordApplicationIndexingDelegate;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.user.PSUserProfileMessageContainer;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.security.service.JWTTokenService;
import de.immomio.service.property.cache.PropertyCountRefreshCacheService;
import de.immomio.service.sender.PropertySearcherMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class LandlordPropertyApplicationService {

    public static final String PRE_TENANT_VIEWING_NOT_ENABLED_L = "PRE_TENANT_VIEWING_NOT_ENABLED_L";
    public static final String APPLICATION_MUST_BE_UNANSWERED_L = "APPLICATION_MUST_BE_UNANSWERED_L";
    public static final String NOT_ALLOWED_TO_READ_APPLICATION_L = "NOT_ALLOWED_TO_READ_APPLICATION_L";

    private static final String INVITATION_DIRECT_SUBJECT_KEY = "invitation.direct.subject";
    private static final String RATING = "rating";
    private static final String INTENT_REMINDER_SUBJECT = "intent.reminder.subject";
    private static final String GUEST_MODE_INTENT_REMINDER_SUBJECT = "guest_mode_intent_reminder.subject";

    private static final Set<ApplicationStatus> APPLICATION_NOT_ACCEPTABLE_STATUSES = Set.of(ApplicationStatus.NO_INTENT, ApplicationStatus.INTENT,
            ApplicationStatus.ACCEPTED);

    private final LandlordMailSender mailSender;
    private final PropertyApplicationRepository applicationRepository;
    private final PropertyApplicationNotificationService propertyApplicationNotificationService;
    private final PropertyCountRefreshCacheService refreshApplicationCountsCache;
    private final AppointmentAcceptanceService appointmentAcceptanceService;
    private final JWTTokenService jwtTokenService;
    private final PropertySearcherMessageSender propertySearcherMessageSender;
    private final LandlordApplicationIndexingDelegate applicationIndexingDelegate;
    private final UserSecurityService userSecurityService;

    @Value("${jwt.intent.ttl}")
    private int tokenTtlInDays;

    @Autowired
    public LandlordPropertyApplicationService(LandlordMailSender mailSender,
            PropertyApplicationRepository applicationRepository, PropertyApplicationNotificationService propertyApplicationNotificationService,
            PropertyCountRefreshCacheService refreshApplicationCountsCache,
            JWTTokenService jwtTokenService,
            AppointmentAcceptanceService appointmentAcceptanceService,
            PropertySearcherMessageSender propertySearcherMessageSender,
            UserSecurityService userSecurityService,
            LandlordApplicationIndexingDelegate applicationIndexingDelegate
    ) {

        this.mailSender = mailSender;
        this.applicationRepository = applicationRepository;
        this.propertyApplicationNotificationService = propertyApplicationNotificationService;
        this.refreshApplicationCountsCache = refreshApplicationCountsCache;
        this.appointmentAcceptanceService = appointmentAcceptanceService;
        this.propertySearcherMessageSender = propertySearcherMessageSender;
        this.jwtTokenService = jwtTokenService;
        this.applicationIndexingDelegate = applicationIndexingDelegate;
        this.userSecurityService = userSecurityService;
    }

    public PropertyApplication updateStatus(PropertyApplication application, ApplicationStatus status) {
        application.setStatus(status);
        if (status == ApplicationStatus.REJECTED) {
            applicationIndexingDelegate.applicationRejected(application);
        }
        setSeen(application);
        return applicationRepository.save(application);
    }

    public void askForIntent(PropertyApplication application) {
        if (application.isAskedForIntent()) {
            return;
        }
        setSeen(application);
        application.setAskedForIntent(true);
        applicationRepository.save(application);
        applicationIndexingDelegate.askedForIntent(application);
        boolean intentNotSet = application.getStatus() != ApplicationStatus.INTENT && application.getStatus() != ApplicationStatus.NO_INTENT;

        if (intentNotSet) {
            try {
                PropertySearcherUserProfileType type = application.getUserProfile().getType();
                String token = type == PropertySearcherUserProfileType.GUEST ? jwtTokenService.generateGuestUserToken(application)
                        : jwtTokenService.generateApplicationIntentToken(application, tokenTtlInDays);
                MailTemplate template = type == PropertySearcherUserProfileType.GUEST ? MailTemplate.GUEST_INTENT_REMINDER : MailTemplate.INTENT_REMINDER;
                String subject = type == PropertySearcherUserProfileType.GUEST ? GUEST_MODE_INTENT_REMINDER_SUBJECT : INTENT_REMINDER_SUBJECT;

                propertyApplicationNotificationService.askForIntentNotification(application, template, token, subject);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void acceptApplicationList(List<Long> applicationIds, MailTemplate mailTemplate) {
        List<PropertyApplication> acceptedApplications = getApplicationsByIdsAndCustomer(applicationIds)
                .stream()
                .filter(application -> !APPLICATION_NOT_ACCEPTABLE_STATUSES.contains(application.getStatus()))
                .peek(this::applicationAccept)
                .collect(Collectors.toList());

        acceptedApplications = applicationRepository.saveAll(acceptedApplications);
        acceptedApplications.forEach(application -> applicationAcceptedIndexingAndNotification(application, mailTemplate));
        refreshApplicationCacheForProperties(acceptedApplications);
    }

    public void rejectApplicationList(List<Long> applicationIds, MailTemplate mailTemplate) {
        List<PropertyApplication> rejectedApplications = getApplicationsByIdsAndCustomer(applicationIds)
                .stream()
                .filter(application -> application.getStatus() != ApplicationStatus.REJECTED)
                .peek(application -> {
                    application.setStatus(ApplicationStatus.REJECTED);
                    setSeen(application);
                }).collect(Collectors.toList());

        rejectedApplications = applicationRepository.saveAll(rejectedApplications);
        appointmentAcceptanceService.cancelAllAttendances(rejectedApplications);
        rejectedApplications.forEach(application -> afterApplicationReject(application, mailTemplate));
        refreshApplicationCacheForProperties(rejectedApplications);
    }

    public void unrejectApplicationList(List<Long> applicationIds) {
        List<PropertyApplication> unrejectedApplications = getApplicationsByIdsAndCustomer(applicationIds)
                .stream()
                .filter(application -> application.getStatus() == ApplicationStatus.REJECTED)
                .peek(application -> {
                    applicationUnReject(application);
                    setSeen(application);
                }).collect(Collectors.toList());

        unrejectedApplications = applicationRepository.saveAll(unrejectedApplications);
        unrejectedApplications.forEach(propertyApplicationNotificationService::applicationUnrejected);
        refreshApplicationCacheForProperties(unrejectedApplications);
    }

    public List<PropertyApplication> findByLandlordAndPropertySearcher(LandlordCustomer customer, PropertySearcherUserProfile user) {
        return applicationRepository.findByLandlordAndUserProfile(customer, user);
    }

    public void tagSeen(List<Long> applicationIds, Boolean seen) {
        applicationIds.forEach(id -> applicationRepository.findById(id).ifPresent(application -> {
            if (userSecurityService.allowedToReadApplication(application)) {
                applicationRepository.updateSeen(application.getId(), seen ? new Date() : null);
            }
        }));
    }

    public Optional<PropertyApplication> findById(Long id) {
        return applicationRepository.findById(id);
    }

    public void setPreTenantViewing(List<Long> applicationIds) {

        List<PropertyApplication> applications = applicationIds.stream()
                .map(applicationRepository::findById)
                .map(propertyApplication -> propertyApplication.orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        applications.stream().filter(application -> !userSecurityService.allowedToReadApplication(application)).findFirst().ifPresent(application -> {
            throw new ApiValidationException(NOT_ALLOWED_TO_READ_APPLICATION_L);
        });

        applications.stream()
                .filter(application -> !application.getProperty().getData().isPreviousTenantAppointmentEnabled())
                .findFirst()
                .ifPresent(application -> {
                    throw new ApiValidationException(PRE_TENANT_VIEWING_NOT_ENABLED_L);
                });

        applications.stream().filter(application -> application.getStatus() != ApplicationStatus.UNANSWERED).findFirst().ifPresent(application -> {
            throw new ApiValidationException(APPLICATION_MUST_BE_UNANSWERED_L);
        });

        applications.forEach(application -> {
            PropertyApplication savedApplication = updateStatus(application, ApplicationStatus.PRE_TENANT_VIEWING);
            try {
                propertyApplicationNotificationService.sendPreTenantViewingNotification(savedApplication,
                        jwtTokenService.generateApplicationIntentToken(application, tokenTtlInDays));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    public void tagSeen(PropertySearcherUserProfile userProfile, Property property, boolean seen) {
        PropertyApplication application = applicationRepository.findByUserProfileIdAndPropertyId(userProfile.getId(), property.getId());
        if (application != null) {
            tagSeen(List.of(application.getId()), seen);
        }
    }

    private void afterApplicationReject(PropertyApplication application, MailTemplate mailTemplate) {
        propertyApplicationNotificationService.sendRejectedApplicationNotification(application, mailTemplate);
        propertySearcherMessageSender.sendUserUpdateMessage(new PSUserProfileMessageContainer(application.getUserProfile().getId()));
    }

    private void applicationAccept(PropertyApplication application) {
        application.setStatus(ApplicationStatus.ACCEPTED);
        application.setAccepted(new Date());
        setSeen(application);
    }

    private void applicationAcceptedIndexingAndNotification(PropertyApplication application, MailTemplate mailTemplate) {
        propertyApplicationNotificationService.sendAcceptedApplicationNotification(application, mailTemplate);
        applicationIndexingDelegate.applicationAccepted(application);
    }

    private void setSeen(PropertyApplication application) {
        application.setSeen(new Date());
    }

    private boolean isRatingOrder(Sort.Order sortOrder) {
        return sortOrder.getProperty().equalsIgnoreCase(RATING);
    }

    private void sendMail(String body, PropertyApplication application) {
        String email = application.getUserProfile().getEmail();
        Property property = application.getProperty();
        if (email != null) {
            Map<String, Object> model = createApplicationModel(application);
            model.put("message", body);

            mailSender.send(email, property.getUser(), property.getCustomer(), MailTemplate.INVITATION_DIRECT, INVITATION_DIRECT_SUBJECT_KEY, null, model);
        }
    }

    private Map<String, Object> createApplicationModel(PropertyApplication application) {
        PropertySearcherUserProfile userProfile = application.getUserProfile();
        Property property = application.getProperty();
        LandlordCustomer customer = property.getCustomer();
        Map<String, Object> model = new HashMap<>();
        model.put(ModelParams.MODEL_USER, new PropertySearcherUserProfileEmailBean(userProfile));
        model.put(ModelParams.MODEL_USER_PROFILE, userProfile.getData());
        model.put(ModelParams.MODEL_FLAT, new PropertyMailBean(property));
        model.put(ModelParams.MODEL_ALLOW_BRANDING, customer.isBrandingAllowed());

        return model;
    }

    private void applicationUnReject(PropertyApplication application) {
        application.setStatus(ApplicationStatus.UNANSWERED);
    }

    private List<PropertyApplication> getApplicationsByIdsAndCustomer(List<Long> applicationIds) {
        LandlordCustomer landlordCustomer = userSecurityService.getPrincipalUser().getCustomer();
        return applicationRepository.findAllByIdInAndPropertyCustomer(applicationIds, landlordCustomer);
    }

    private void refreshApplicationCacheForProperties(List<PropertyApplication> propertyApplications) {
        propertyApplications.stream().map(PropertyApplication::getProperty).distinct().forEach(refreshApplicationCountsCache::refreshApplicationCache);
    }
}