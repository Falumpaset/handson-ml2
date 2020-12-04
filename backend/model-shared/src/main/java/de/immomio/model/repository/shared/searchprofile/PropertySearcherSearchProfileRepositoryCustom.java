package de.immomio.model.repository.shared.searchprofile;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfile;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;

import java.util.List;

public interface PropertySearcherSearchProfileRepositoryCustom {

    void customSave(PropertySearcherSearchProfile profile);

    void customManualEntryRadius(PropertySearcherSearchProfile profile);

    List<PropertySearcherSearchProfile> customFindByUserProfileAndProperty(PropertySearcherUserProfile userProfile, Property property);

    PropertySearcherSearchProfile customFindLastByUserProfileAndProperty(PropertySearcherUserProfile userProfile, Property property);

    List<PropertySearcherSearchProfile> customFindByUserProfile(PropertySearcherUserProfile userProfile);

    PropertySearcherSearchProfile customFindLastByUserProfile(PropertySearcherUserProfile userProfile);

    void customDeleteAllByUserProfile(PropertySearcherUserProfile userProfile);
}
