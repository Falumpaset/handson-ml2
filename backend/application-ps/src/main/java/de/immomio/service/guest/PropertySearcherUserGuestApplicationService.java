package de.immomio.service.guest;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.ApplicationSaveException;
import de.immomio.constants.exceptions.ImmomioRuntimeException;
import de.immomio.data.base.type.user.PropertySearcherUserType;
import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.bean.user.guest.PropertySearcherGuestUserApplyBean;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserProfileRepository;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserRepository;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.model.repository.shared.property.PropertyRepository;
import de.immomio.service.application.PropertySearcherPropertyApplicationService;
import de.immomio.service.customQuestion.PropertySearcherCustomQuestionService;
import de.immomio.service.propertySearcher.PropertySearcherUserCreationService;
import de.immomio.service.propertySearcher.customer.PropertySearcherCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertySearcherUserGuestApplicationService {

    private static final String APPLY_BEAN_MISSING_L = "APPLY_BEAN_MISSING_L";
    private static final String APPLICATION_SAVING_FAILED_L = "APPLICATION_SAVING_FAILED_L";
    private static final String USER_PROFILE_DATA_MISSING_L = "USER_PROFILE_DATA_MISSING_L";
    private static final String ADDRESS_DATA_MISSING_L = "ADDRESS_DATA_MISSING_L";
    private static final String GUEST_MODE_FOR_CUSTOMER_NOT_ALLOWED_L = "GUEST_MODE_FOR_CUSTOMER_NOT_ALLOWED_L";

    private final PropertySearcherUserRepository userRepository;
    private final PropertySearcherUserCreationService userCreationService;
    private final PropertyRepository propertyRepository;
    private final PropertyApplicationRepository applicationRepository;
    private final PropertySearcherCustomQuestionService customQuestionService;
    private final PropertySearcherCustomerService customerService;
    private final PropertySearcherUserProfileRepository userProfileRepository;
    private final PropertySearcherPropertyApplicationService applicationService;

    @Autowired
    public PropertySearcherUserGuestApplicationService(PropertySearcherUserRepository userRepository,
            PropertySearcherUserCreationService userCreationService,
            PropertyRepository propertyRepository,
            PropertyApplicationRepository applicationRepository,
            PropertySearcherCustomQuestionService customQuestionService,
            PropertySearcherCustomerService customerService,
            PropertySearcherUserProfileRepository userProfileRepository,
            PropertySearcherPropertyApplicationService applicationService) {
        this.userRepository = userRepository;
        this.userCreationService = userCreationService;
        this.propertyRepository = propertyRepository;
        this.applicationRepository = applicationRepository;
        this.customQuestionService = customQuestionService;
        this.customerService = customerService;
        this.userProfileRepository = userProfileRepository;
        this.applicationService = applicationService;
    }

    @Transactional
    public PropertyApplication applyToPropertyAsGuest(PropertySearcherUser user,
            Property property,
            PropertySearcherGuestUserApplyBean applyBean,
            String email) {
        if (applyBean == null) {
            throw new ApiValidationException(APPLY_BEAN_MISSING_L);
        }


        if (applyBean.getProfileData() == null) {
            throw new ApiValidationException(USER_PROFILE_DATA_MISSING_L);
        }
        if (applyBean.getAddress() == null) {
            throw new ApiValidationException(ADDRESS_DATA_MISSING_L);
        }


        PropertyApplication propertyApplication = applicationRepository.findByEmailAndProperty(email, property);
        if (isGuestPropertyApplicationPossible(user, property, propertyApplication)) {
            if (propertyApplication != null) {
                propertyApplication.setUserProfile(
                        changeUserProfileToGuest(propertyApplication.getUserProfile(), applyBean.getAddress(), applyBean.getProfileData()));
            } else {
                if (user == null) {
                    user = userCreationService.createCompleteUserStructure(email, PropertySearcherUserType.UNREGISTERED, applyBean.getAddress(),
                            createProfileData(applyBean.getProfileData()), null, false).getUser();
                }

                PropertySearcherUserProfile guestUserProfile = userCreationService.createSubUserProfile(PropertySearcherUserProfileType.GUEST,
                        applyBean.getAddress(), applyBean.getProfileData(), user, getInternalPoolCustomer(property));
                try {
                    propertyApplication = applicationService.createNewApplication(property, guestUserProfile, applyBean.getShortUrlToken());
                } catch (ApplicationSaveException e) {
                    throw new ImmomioRuntimeException(APPLICATION_SAVING_FAILED_L);
                }
            }

            if (applyBean.getCustomQuestionResponses() != null) {
                customQuestionService.respondToAll(applyBean.getCustomQuestionResponses(), propertyApplication.getUserProfile());
            }
        }

        return propertyApplication;
    }

    public boolean isGuestPropertyApplicationPossible(PropertySearcherUser user, Property property, PropertyApplication propertyApplication) {
        if (property == null) {
            return false;
        }

        if (!property.getCustomer().allowGuestMode()) {
            throw new ApiValidationException(GUEST_MODE_FOR_CUSTOMER_NOT_ALLOWED_L);
        }

        if (user != null && user.getType() == PropertySearcherUserType.REGISTERED) {
            return false;
        }
        return propertyApplication == null || propertyApplication.getUserProfile().getType() == PropertySearcherUserProfileType.ANONYMOUS;
    }

    public void deleteGuest(PropertyApplication propertyApplication) {
        PropertySearcherUserProfile userProfile = propertyApplication.getUserProfile();
        PropertySearcherUser user = userProfile.getUser();

        // The whole propertysearcher Customer can be delete if he has only two userProfiles
        // The main profile is always created but not used until user is registered
        // Second profile is the guest profile that should be deleted
        if (user.getProfiles().size() <= 2) {
            customerService.delete(userProfile);
        } else {
            userProfileRepository.delete(userProfile);
        }
    }

    private LandlordCustomer getInternalPoolCustomer(Property property) {
        LandlordCustomer propertyCustomer = property.getCustomer();
        return propertyCustomer.isInternalTenantPoolAllowed() ? propertyCustomer : null;
    }

    private PropertySearcherUserProfileData createProfileData(PropertySearcherUserProfileData profileData) {
        PropertySearcherUserProfileData userProfileData = new PropertySearcherUserProfileData();

        if (profileData != null) {
            userProfileData.setFirstname(profileData.getFirstname());
            userProfileData.setName(profileData.getName());
            userProfileData.setPhone(profileData.getPhone());
        }

        return userProfileData;
    }

    private PropertySearcherUserProfile changeUserProfileToGuest(PropertySearcherUserProfile userProfile,
            Address address,
            PropertySearcherUserProfileData userProfileData) {
        if (userProfile.getType() == PropertySearcherUserProfileType.MAIN) {
            throw new ApiValidationException();
        }

        userProfile.setType(PropertySearcherUserProfileType.GUEST);
        userProfile.setAddress(address);
        userProfile.setData(userProfileData);

        return userProfileRepository.save(userProfile);
    }
}
