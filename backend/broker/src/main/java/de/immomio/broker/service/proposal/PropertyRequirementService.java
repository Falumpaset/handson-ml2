package de.immomio.broker.service.proposal;

import de.immomio.broker.service.UserService;
import de.immomio.data.base.type.property.PropertyStatus;
import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.base.type.user.PropertySearcherUserType;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfile;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.entity.user.SearchProfileData;
import de.immomio.model.repository.core.shared.application.BasePropertyApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static de.immomio.utils.TenantUtils.isUserInOtherPrivateTenantPool;
import static de.immomio.utils.TenantUtils.isUserInPrivateTenantPool;

/**
 * @author Maik Kingma
 */

@Service
public class PropertyRequirementService {

    private final UserService userService;

    private final BasePropertyApplicationRepository applicationRepository;

    @Autowired
    public PropertyRequirementService(UserService userService, BasePropertyApplicationRepository applicationRepository) {
        this.userService = userService;
        this.applicationRepository = applicationRepository;
    }

    public boolean propertyMeetsRequirementsForCreatingProposal(
            Property property,
            PropertySearcherSearchProfile searchProfile
    ) {
        PropertySearcherUserProfile userProfile = searchProfile.getUserProfile();

        boolean userBlockedForCreatingProposals = userService.isUserBlockedForCreatingProposals(userProfile);
        boolean userInOtherPrivateTenantPool = isUserInOtherPrivateTenantPool(userProfile, property.getCustomer());

        if (userProfile.getUser().getType() != PropertySearcherUserType.REGISTERED) {
            return false;
        }

        if (userProfile.getSearchUntil() == null || userProfile.getSearchUntil().before(new Date())) {
            return false;
        }

        if (property.getStatus() == PropertyStatus.RESERVED) {
            return false;
        }

        if (searchProfile.getDeleted()) {
            return false;
        }

        if ((userBlockedForCreatingProposals && !isUserInPrivateTenantPool(userProfile, property.getCustomer())) || userInOtherPrivateTenantPool) {
            return false;
        }

        if (searchProfile.getProperty() != null && property.getId().equals(searchProfile.getProperty().getId())) {
            return false;
        }

        if (applicationRepository.existsByPropertyAndUserProfile(property, searchProfile.getUserProfile())) {
            return false;
        }

        if (property.getType() != PropertyType.FLAT) {
            return false;
        }

        return propertyFulfillsSP(property, searchProfile);
    }

    public boolean propertyFulfillsSP(Property property, PropertySearcherSearchProfile searchProfile) {
        SearchProfileData searchProfileData = searchProfile.getData();

        PropertyData propertyData = property.getData();

        if (propertyData.getTotalRentGross() == null ||
                propertyData.getSize() == null ||
                propertyData.getRooms() == null) {
            return false;
        }

        if (property.isRented()) {
            return false;
        }

        if (propertyData.getTotalRentGross() > searchProfileData.getRent().getUpperBound()) {
            return false;
        }

        if (searchProfileData.getSize().getLowerBound() > propertyData.getSize()) {
            return false;
        }

        return searchProfileData.getRooms().getLowerBound() <= propertyData.getRooms();
    }
}
