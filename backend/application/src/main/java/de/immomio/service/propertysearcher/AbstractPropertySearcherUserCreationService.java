package de.immomio.service.propertysearcher;

import de.immomio.data.base.bean.customer.PaymentMethod;
import de.immomio.data.base.type.customer.CustomerLocation;
import de.immomio.data.base.type.customer.PaymentMethodType;
import de.immomio.data.base.type.user.PropertySearcherUserType;
import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherProspectOptIn;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.model.abstractrepository.customer.user.BaseAbstractUserRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserProfileRepository;
import de.immomio.utils.ReflectionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;

public abstract class AbstractPropertySearcherUserCreationService {

    private final BaseAbstractUserRepository<PropertySearcherUser> userRepository;
    private final BasePropertySearcherUserProfileRepository userProfileRepository;
    private final PropertySearcherSearchUntilCalculationService searchUntilCalculationService;

    public AbstractPropertySearcherUserCreationService(BaseAbstractUserRepository<PropertySearcherUser> userRepository,
            BasePropertySearcherUserProfileRepository userProfileRepository,
            PropertySearcherSearchUntilCalculationService searchUntilCalculationService) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.searchUntilCalculationService = searchUntilCalculationService;
    }

    @Transactional
    public PropertySearcherUserProfile createCompleteUserStructure(String email,
            PropertySearcherUserType type,
            Address address,
            PropertySearcherUserProfileData userProfileData,
            LandlordCustomer internalTenantPoolCustomer, boolean optInProspect) {
        PropertySearcherUser user = createUser(email, type, optInProspect);
        return createMainUserProfile(address, userProfileData, user, internalTenantPoolCustomer);
    }

    @Transactional
    public PropertySearcherUserProfile createEmptyUserStructure(String email,
            PropertySearcherUserType type) {
        PropertySearcherUser user = createUser(email, type, false);
        return createMainUserProfile(null, createEmptyUserProfileData(), user, null);
    }

    @Transactional
    public PropertySearcherUserProfile createSubUserProfile(PropertySearcherUserProfileType profileType,
            Address address,
            PropertySearcherUserProfileData userProfileData,
            PropertySearcherUser user,
            LandlordCustomer internalTenentPoolCustomer) {

        if (profileType == PropertySearcherUserProfileType.MAIN) {
            throw new IllegalArgumentException("PROFILE_TYPE_MAIN_NOT_ALLOWED_FOR_SUB_PROFILE_L");
        }

        PropertySearcherUserProfile userProfile = new PropertySearcherUserProfile();

        return createUserProfile(userProfile, profileType, address, userProfileData, user, internalTenentPoolCustomer);
    }

    public PropertySearcherUserProfileData createEmptyUserProfileData() {
        return new PropertySearcherUserProfileData();
    }

    private PropertySearcherUserProfile createMainUserProfile(Address address,
            PropertySearcherUserProfileData userProfileData,
            PropertySearcherUser user,
            LandlordCustomer internalTenantPoolCustomer) {

        PropertySearcherUserProfile userProfile = new PropertySearcherUserProfile();
        userProfile.setId(user.getId());

        PropertySearcherUserProfile filledUserProfile = createUserProfile(userProfile, PropertySearcherUserProfileType.MAIN, address, userProfileData, user,
                internalTenantPoolCustomer);
        filledUserProfile.setSearchUntil(searchUntilCalculationService.getSearchUntil(filledUserProfile));
        return filledUserProfile;
    }

    private PropertySearcherUserProfile createUserProfile(PropertySearcherUserProfile userProfile,
            PropertySearcherUserProfileType profileType,
            Address address,
            PropertySearcherUserProfileData userProfileData,
            PropertySearcherUser user,
            LandlordCustomer internalTenentPoolCustomer) {
        userProfile.setUser(user);
        userProfile.setType(profileType);
        userProfile.setEmail(user.getEmail());
        userProfile.setAddress(address);
        userProfile.setData(userProfileData);

        if (internalTenentPoolCustomer != null && internalTenentPoolCustomer.isInternalTenantPoolAllowed()) {
            userProfile.setTenantPoolCustomer(internalTenentPoolCustomer);
        }
        ReflectionUtils.trimFields(userProfile.getAddress());
        ReflectionUtils.trimFields(userProfile.getData());

        userProfile = userProfileRepository.save(userProfile);

        user.getProfiles().add(userProfile);

        return userProfile;
    }

    private PropertySearcherCustomer createCustomer() {
        PropertySearcherCustomer customer = new PropertySearcherCustomer();

        customer.setLocation(CustomerLocation.DE);
        customer.setPaymentMethods(new ArrayList<>(Collections.singletonList(new PaymentMethod(PaymentMethodType.PAYPAL, true))));

        return saveCustomer(customer);
    }

    private PropertySearcherUser createUser(String email, PropertySearcherUserType type, boolean optInProspect) {
        PropertySearcherCustomer customer = createCustomer();

        PropertySearcherUser user = new PropertySearcherUser();
        user.setCustomer(customer);
        user.setEmail(email);
        user.setEnabled(true);
        user.setType(type);
        user.setProspectOptIn(new PropertySearcherProspectOptIn(user, optInProspect));

        user = userRepository.save(user);

        customer.setUser(user);

        return user;
    }

    protected abstract PropertySearcherCustomer saveCustomer(PropertySearcherCustomer customer);
}
