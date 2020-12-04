package de.immomio.service.propertySearcher.onboarding;

import de.immomio.common.ErrorCode;
import de.immomio.constants.exceptions.BadRequestException;
import de.immomio.data.base.type.user.PropertySearcherUserType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.propertysearcher.bean.user.guest.PropertySearcherGuestUserRegisterBean;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserRepository;
import de.immomio.propertysearcher.bean.PropertySearcherRegisterBean;
import de.immomio.security.service.KeycloakService;
import de.immomio.service.customQuestion.PropertySearcherCustomQuestionService;
import de.immomio.service.propertySearcher.PropertySearcherUserCreationService;
import de.immomio.service.propertySearcher.PropertySearcherUserRegistrationValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Slf4j
@Service
public class PropertySearcherOnboardingService {

    private static final String IMMOMIO = "immomio";

    private final KeycloakService keycloakService;
    private final PropertySearcherUserCreationService userCreationService;
    private final PropertySearcherUserRepository userRepository;
    private final PropertySearcherCustomQuestionService customQuestionService;
    private final EntityManager entityManager;
    private final PropertySearcherUserRegistrationValidationService registrationValidationService;
    private final PropertySearcherUserProfileMergeService userProfileService;

    @Autowired
    public PropertySearcherOnboardingService(KeycloakService keycloakService,
            PropertySearcherUserCreationService userCreationService,
            PropertySearcherUserRepository userRepository,
            EntityManager entityManager,
            PropertySearcherCustomQuestionService customQuestionService,
            PropertySearcherUserRegistrationValidationService registrationValidationService,
            PropertySearcherUserProfileMergeService userProfileService) {
        this.keycloakService = keycloakService;
        this.userCreationService = userCreationService;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.customQuestionService = customQuestionService;
        this.registrationValidationService = registrationValidationService;
        this.userProfileService = userProfileService;
    }

    // Transactional because we only wont to create a user all the other entities except the SearchProfile are created as well
    @Transactional
    public PropertySearcherUserProfile register(PropertySearcherRegisterBean registerBean) {
        registrationValidationService.registerValidation(registerBean);

        try {
            PropertySearcherUserProfile userProfile = changePropertySearcherStateToRegistered(registerBean);

            if (userProfile.getTenantPoolCustomer() != null && registerBean.getCustomQuestionResponses() != null) {
                customQuestionService.respondToAll(registerBean.getCustomQuestionResponses(), userProfile);
            }

            return userProfile;
        } catch (Exception ex) {
            keycloakService.removeUser(registerBean.getEmail());
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Transactional
    public PropertySearcherUserProfile registerGuestPropertySearcher(PropertySearcherUserProfile baseProfile,
            PropertySearcherGuestUserRegisterBean registerBean) {
        registrationValidationService.registerValidation(registerBean);

        try {
            keycloakService.registerInKeycloak(IMMOMIO, registerBean.isSocialLogin(), baseProfile.getUser().getEmail(), baseProfile.getData().getFirstname(),
                    baseProfile.getData().getName(), registerBean.getPassword());

            return registerUnregisteredPropertySearcher(baseProfile.getData(), registerBean.isOptInProspect(),
                    baseProfile.getUser(), baseProfile);
        } catch (Exception ex) {
            keycloakService.removeUser(baseProfile.getEmail());
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    protected PropertySearcherUserProfile changePropertySearcherStateToRegistered(PropertySearcherRegisterBean registerBean) {
        PropertySearcherUser user = userRepository.findByEmail(registerBean.getEmail());

        if (user != null && user.isRegistered()) {
            throw new BadRequestException(ErrorCode.ERROR_EMAIL_ALREADY_EXISTS);
        }

        keycloakService.registerInKeycloak(IMMOMIO, registerBean.isSocialLogin(), registerBean.getEmail(), registerBean.getFirstName(),
                registerBean.getLastName(), registerBean.getPassword());

        if (user == null) {
            return userCreationService.createCompleteUserStructure(registerBean.getEmail(), PropertySearcherUserType.REGISTERED, registerBean.getAddress(),
                    createProfileData(registerBean), getInternalTenantPoolLandlordCustomer(registerBean), registerBean.isOptInProspect());
        }

        return registerUnregisteredPropertySearcher(createProfileData(registerBean), registerBean.isOptInProspect(), user, null);
    }

    private LandlordCustomer getInternalTenantPoolLandlordCustomer(PropertySearcherRegisterBean registerBean) {
        if (registerBean.getBrandedCustomerId() != null) {
            try {
                return entityManager.find(LandlordCustomer.class, registerBean.getBrandedCustomerId());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    private PropertySearcherUserProfileData createProfileData(PropertySearcherRegisterBean registerBean) {
        PropertySearcherUserProfileData userProfileData = registerBean.getUserProfileData();
        if (userProfileData == null) {
            userProfileData = new PropertySearcherUserProfileData();
        }

        String firstname = registerBean.getFirstName();
        if (firstname != null) {
            userProfileData.setFirstname(firstname);
        }
        String lastName = registerBean.getLastName();
        if (lastName != null) {
            userProfileData.setName(lastName);
        }
        String phone = registerBean.getPhone();
        if (phone != null) {
            userProfileData.setPhone(phone);
        }

        return userProfileData;
    }

    private PropertySearcherUserProfile registerUnregisteredPropertySearcher(PropertySearcherUserProfileData userProfileData, boolean isOptInForProspect,
            PropertySearcherUser user,
            PropertySearcherUserProfile userProfile) {
        userProfileService.merge(user, userProfileData, userProfile);

        user.setType(PropertySearcherUserType.REGISTERED);
        user.getProspectOptIn().setOptInForProspect(isOptInForProspect);

        user = userRepository.save(user);
        return user.getMainProfile();
    }
}
