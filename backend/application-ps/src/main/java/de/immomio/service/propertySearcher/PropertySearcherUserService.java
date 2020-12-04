package de.immomio.service.propertySearcher;

import de.immomio.beans.TokenBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.BadRequestException;
import de.immomio.constants.exceptions.TokenValidationException;
import de.immomio.constants.exceptions.UserNotFoundException;
import de.immomio.data.base.entity.AbstractEntity;
import de.immomio.data.propertysearcher.bean.user.PropertySearcherUserRegisterResultBean;
import de.immomio.data.propertysearcher.entity.user.ProfileCompletenessResponseBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.messaging.container.proposal.PSSearchProfileMessageContainer;
import de.immomio.messaging.container.user.PSUserProfileMessageContainer;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserProfileRepository;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserRepository;
import de.immomio.propertysearcher.bean.EditUserProfileBean;
import de.immomio.propertysearcher.bean.PropertySearcherRegisterBean;
import de.immomio.security.common.bean.ApplicationIntentToken;
import de.immomio.security.common.bean.ImpersonateResponse;
import de.immomio.security.common.bean.UserEmailToken;
import de.immomio.security.service.JWTTokenService;
import de.immomio.security.service.KeycloakService;
import de.immomio.service.indexing.PropertySearcherProfileChangedIndexingService;
import de.immomio.service.property.PropertyProposalService;
import de.immomio.service.propertySearcher.onboarding.PropertySearcherOnboardingSearchProfileService;
import de.immomio.service.propertySearcher.onboarding.PropertySearcherOnboardingService;
import de.immomio.service.propertysearcher.AbstractPropertySearcherUserService;
import de.immomio.service.propertysearcher.PropertySearcherSearchUntilCalculationService;
import de.immomio.service.propertysearcher.userprofile.PropertySearcherUserProfileConverter;
import de.immomio.service.reporting.PropertySearcherApplicationIndexingDelegate;
import de.immomio.service.security.UserSecurityService;
import de.immomio.service.sender.PropertySearcherMessageSender;
import de.immomio.service.sender.SearchProfileToProposalMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class PropertySearcherUserService extends AbstractPropertySearcherUserService {

    private static final String WRONG_USER_ID = "WRONG_USER_ID_L";
    private static final String NO_USER_AUTHORIZED = "NO_USER_AUTHORIZED_L";
    private static final String USER_NOT_FOUND = "USER_NOT_FOUND_L";
    private static final String EMAIL_ALREADY_VERIFIED_MESSAGE = "EMAIL_ALREADY_VERIFIED_L";

    private final KeycloakService keycloakService;
    private final PropertySearcherUserProfileRepository userProfileRepository;
    private final PropertySearcherUserRepository userRepository;
    private final JWTTokenService jwtTokenService;
    private final SearchProfileToProposalMessageSender profileToProposalMessageSender;
    private final PropertyProposalService propertyProposalService;
    private final PropertySearcherMessageSender messageSender;
    private final PropertySearcherApplicationIndexingDelegate applicationIndexingDelegate;
    private final PropertySearcherUserNotificationService userEmailService;
    private final UserSecurityService userSecurityService;
    private final PropertySearcherOnboardingService onboardingService;
    private final PropertySearcherProfileChangedIndexingService profileChangedIndexingService;
    private final PropertySearcherOnboardingSearchProfileService propertySearcherOnboardingSearchProfileService;
    private final PropertySearcherSearchUntilCalculationService searchUntilService;
    private final PropertySearcherUserProfileConverter userProfileConverter;

    @Autowired
    public PropertySearcherUserService(PropertySearcherUserRepository userRepository,
            KeycloakService keycloakService,
            PropertySearcherUserProfileRepository userProfileRepository,
            JWTTokenService jwtTokenService,
            SearchProfileToProposalMessageSender profileToProposalMessageSender,
            PropertyProposalService propertyProposalService,
            PropertySearcherMessageSender messageSender,
            PropertySearcherApplicationIndexingDelegate applicationIndexingDelegate,
            PropertySearcherUserNotificationService userEmailService,
            UserSecurityService userSecurityService,
            PropertySearcherOnboardingService onboardingService,
            PropertySearcherProfileChangedIndexingService profileChangedIndexingService,
            PropertySearcherOnboardingSearchProfileService propertySearcherOnboardingSearchProfileService,
            PropertySearcherSearchUntilCalculationService searchUntilService,
            PropertySearcherUserProfileConverter userProfileConverter) {
        this.userRepository = userRepository;
        this.keycloakService = keycloakService;
        this.userProfileRepository = userProfileRepository;
        this.jwtTokenService = jwtTokenService;
        this.userSecurityService = userSecurityService;
        this.profileToProposalMessageSender = profileToProposalMessageSender;
        this.propertyProposalService = propertyProposalService;
        this.messageSender = messageSender;
        this.applicationIndexingDelegate = applicationIndexingDelegate;
        this.userEmailService = userEmailService;
        this.onboardingService = onboardingService;
        this.profileChangedIndexingService = profileChangedIndexingService;
        this.propertySearcherOnboardingSearchProfileService = propertySearcherOnboardingSearchProfileService;
        this.searchUntilService = searchUntilService;
        this.userProfileConverter = userProfileConverter;
    }

    public PropertySearcherUserRegisterResultBean register(PropertySearcherRegisterBean registerBean) {
        return createRegisterResultBean(registerBase(registerBean));
    }

    public PropertySearcherUserProfile registerBase(PropertySearcherRegisterBean registerBean) {
        PropertySearcherUserProfile userProfile = onboardingService.register(registerBean);

        userEmailService.sendEmailVerifyNotification(userProfile);
        messageSender.sendUserUpdateMessage(new PSUserProfileMessageContainer(userProfile.getId()));
        profileChangedIndexingService.indexProfileChanged(userProfile.getId());

        userProfile = setSearchUntil(userProfile);

        propertySearcherOnboardingSearchProfileService.createSearchProfileForUserIfNecessary(userProfile, registerBean.getSearchProfile());

        return userProfile;
    }

    public PropertySearcherUserProfile setSearchUntil(PropertySearcherUserProfile userProfile) {
        userProfile.setSearchUntil(searchUntilService.getSearchUntil(userProfile));
        return userProfileRepository.save(userProfile);
    }

    public void unlockUser(TokenBean tokenBean) {
        UserEmailToken userEmailToken = jwtTokenService.validateEmailToken(tokenBean.getToken());
        Optional<PropertySearcherUser> foundUser = userRepository.findById(userEmailToken.getId());
        if (foundUser.isPresent()) {
            try {
                PropertySearcherUser user = foundUser.get();
                keycloakService.activateUser(userEmailToken.getEmail());
                unlockUser(user);
                userRepository.save(user);
            } catch (UserNotFoundException e) {
                log.error(e.getMessage(), e);

                throw new BadRequestException(USER_NOT_FOUND);
            }
        }
    }

    public boolean isFirstSocialLogin(String email) {
        PropertySearcherUser user = userRepository.findByEmail(email);
        return (user == null || !user.isRegistered()) && keycloakService.userExists(email);
    }

    public void resendEmailVerification(PropertySearcherUserProfile userProfile) {
        if (userProfile.getUser().getEmailVerified() != null) {
            throw new ApiValidationException(EMAIL_ALREADY_VERIFIED_MESSAGE);
        }

        userEmailService.sendEmailVerifyNotification(userProfile);
    }

    public void verifyEmail(Long userID, String token) {
        PropertySearcherUser user = userRepository.customFindOne(userID);
        if (user.getEmailVerified() != null) {
            throw new ApiValidationException(EMAIL_ALREADY_VERIFIED_MESSAGE);
        }

        UserEmailToken userEmailToken = jwtTokenService.validateEmailToken(token);
        if (!userEmailToken.getId().equals(user.getId())) {
            throw new TokenValidationException(WRONG_USER_ID);
        }

        user.setEmailVerified(new Date());
        userRepository.save(user);
        userEmailService.notifyRegisteredUser(user.getMainProfile());
    }

    public void setRentedApplicationsFetched(PropertySearcherUserProfile userProfile) {
        PropertySearcherUser user = userProfile.getUser();
        user.setRentedApplicationsFetched(new Date());
        userRepository.save(user);
    }

    public PropertySearcherUserProfile setLastLogin() {
        PropertySearcherUserProfile userProfile = userSecurityService.getPrincipalUserProfile();

        if (userProfile == null) {
            throw new ApiValidationException(NO_USER_AUTHORIZED);
        }

        if (userProfile.getUser().getLastLogin() == null) {
            return updateLastLoginAndSearch();
        }
        return userProfile;
    }

    public PropertySearcherUserProfile updateLastLoginAndSearch() {
        PropertySearcherUserProfile userProfile = userSecurityService.getPrincipalUserProfile();
        return updateLastLoginAndSearch(userProfile);
    }

    public ApplicationIntentToken extractDataFromApplicationToken(String token) {
        return jwtTokenService.validateApplicationIntentToken(token);
    }

    public void updateSearchUntil(TokenBean tokenBean, boolean confirmedSearch) {
        UserEmailToken token = jwtTokenService.validateEmailToken(tokenBean.getToken());
        PropertySearcherUserProfile mainProfile = userRepository.loadById(token.getId()).getMainProfile();

        if (confirmedSearch) {
            if (mainProfile.getSearchUntil().before(new Date())) {
                mainProfile.getSearchProfiles()
                        .stream()
                        .map(AbstractEntity::getId)
                        .map(PSSearchProfileMessageContainer::new)
                        .forEach(profileToProposalMessageSender::sendProposalUpdateMessage);
            }
            mainProfile.setSearchUntil(searchUntilService.getSearchUntil(mainProfile));
        } else {
            mainProfile.setSearchUntil(new Date());
            propertyProposalService.cleanUpProposalsAfterExpiredSearch(mainProfile);
        }
        userProfileRepository.save(mainProfile);
    }

    public void updateSearchUntil() {
        PropertySearcherUserProfile userProfile = userSecurityService.getPrincipalUserProfile();
        userProfile.setSearchUntil(searchUntilService.getSearchUntil(userProfile));
        userProfileRepository.save(userProfile);
    }

    public void editProfile(PropertySearcherUserProfile userProfile, EditUserProfileBean editUserBean) {
        userProfile.setData(editUserBean.getProfile());
        userProfile.setAddress(editUserBean.getAddress());

        userProfileRepository.save(userProfile);
        messageSender.sendUserUpdateMessage(new PSUserProfileMessageContainer(userProfile.getId()));

        userProfile.getApplications().forEach(applicationIndexingDelegate::profileDataChanged);
    }

    public void resetInternalTenantPool() {
        PropertySearcherUserProfile userProfile = userSecurityService.getPrincipalUserProfile();
        userProfile.setTenantPoolCustomer(null);
        userProfileRepository.save(userProfile);
        sendUserRefreshMessage(userProfile.getId());
    }

    public void sendUserRefreshMessage(Long userProfileId) {
        messageSender.sendUserUpdateMessage(new PSUserProfileMessageContainer(userProfileId));
    }

    public ProfileCompletenessResponseBean calculateProfileCompleteness() {
        return calculateProfileCompleteness(userSecurityService.getPrincipalUserProfile());
    }

    public void unlockUser(PropertySearcherUser user) {
        if (user.getEmailVerified() == null) {
            user.setEmailVerified(new Date());
        }
        user.setEnabled(true);
    }

    public PropertySearcherUserRegisterResultBean createRegisterResultBean(PropertySearcherUserProfile userProfile) {
        PropertySearcherUserRegisterResultBean registerResultBean = new PropertySearcherUserRegisterResultBean();
        registerResultBean.setUserProfile(userProfileConverter.convertUserProfile(userProfile));
        ImpersonateResponse impersonateResponse = keycloakService.impersonateUser(userProfile.getEmail());
        registerResultBean.setToken(impersonateResponse.getAccessToken());

        return registerResultBean;
    }

    private PropertySearcherUserProfile updateLastLoginAndSearch(PropertySearcherUserProfile userProfile) {
        if (userProfile == null) {
            throw new ApiValidationException(NO_USER_AUTHORIZED);
        }

        PropertySearcherUser user = userProfile.getUser();

        user.setLastLogin(new Date());
        user = userRepository.save(user);

        userProfile.setSearchUntil(searchUntilService.getSearchUntil(userProfile));
        userProfile = userProfileRepository.save(userProfile);
        userProfile.setUser(user);

        return userProfile;
    }

}
