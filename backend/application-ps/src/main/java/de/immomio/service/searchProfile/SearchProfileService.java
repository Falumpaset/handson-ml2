package de.immomio.service.searchProfile;

import de.immomio.cloud.service.google.GoogleMapsService;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.bean.helper.DoubleInterval;
import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.bean.searchprofile.SearchProfileBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfile;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfileType;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.entity.user.SearchProfileData;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.entity.common.District;
import de.immomio.model.repository.shared.location.DistrictRepository;
import de.immomio.model.repository.shared.searchprofile.PropertySearcherSearchProfileRepository;
import de.immomio.service.propertyProposals.PropertySearcherPropertyProposalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Maik Kingma
 */

@Service
@Slf4j
public class SearchProfileService {

    private final PropertySearcherSearchProfileRepository searchProfileRepository;

    private final PropertySearcherPropertyProposalService propertySearcherPropertyProposalService;

    private final GoogleMapsService googleMapsService;

    private final DistrictRepository districtRepository;


    private static final String SEARCH_PROFILE_NOT_COMPLETE = "SEARCH_PROFILE_NOT_COMPLETE_L";

    @Autowired
    public SearchProfileService(
            PropertySearcherSearchProfileRepository searchProfileRepository,
            PropertySearcherPropertyProposalService propertySearcherPropertyProposalService,
            GoogleMapsService googleMapsService,
            DistrictRepository districtRepository) {
        this.searchProfileRepository = searchProfileRepository;
        this.propertySearcherPropertyProposalService = propertySearcherPropertyProposalService;
        this.googleMapsService = googleMapsService;
        this.districtRepository = districtRepository;
    }

    public void create(Property property, PropertySearcherUserProfile userProfile) {
        if (property.getType() != PropertyType.FLAT) {
            return;
        }

        List<PropertySearcherSearchProfile> profiles = new ArrayList<>();
        PropertySearcherSearchProfile searchProfile;
        try {
            profiles = searchProfileRepository.customFindByUserProfileAndProperty(userProfile, property);
        } catch (Exception e) {
            log.error("error retrieving existing search profiles", e);
        }

        searchProfile = profiles == null || profiles.isEmpty() ? new PropertySearcherSearchProfile() : profiles.get(0);
        searchProfile.setUserProfile(userProfile);
        searchProfile.setProperty(property);
        searchProfile.setManuallyCreated(false);

        PropertyData data = property.getData();
        DoubleInterval rent = new DoubleInterval(0.0, data.getTotalRentGross() * 1.15);
        DoubleInterval rooms = new DoubleInterval(data.getRooms() - 1.0, Double.POSITIVE_INFINITY);
        DoubleInterval size = new DoubleInterval(data.getSize() * 0.8, Double.POSITIVE_INFINITY);

        Address address = data.getAddress();
        SearchProfileData searchProfileData = new SearchProfileData();
        populateAddress(address, searchProfileData);

        searchProfileData.setName(generateName("Auto-Generated: ", address));
        searchProfileData.setRent(rent);
        searchProfileData.setRooms(rooms);
        searchProfileData.setSize(size);
        searchProfileData.setRadius(2000L);

        searchProfile.setData(searchProfileData);

        Set<District> districts = districtRepository.findAllByZipCodesZipCodeContains(address.getZipCode());
        searchProfile.setDistricts(districts);
        searchProfile.setType(PropertySearcherSearchProfileType.DISTRICT);

        try {
            PropertySearcherSearchProfile saved = searchProfileRepository.save(searchProfile);
            propertySearcherPropertyProposalService.createProposalsFromSP(saved);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void create(SearchProfileBean searchProfileBean, PropertySearcherUserProfile userProfile) {
        if (!searchProfileComplete(searchProfileBean)) {
            throw new ApiValidationException(SEARCH_PROFILE_NOT_COMPLETE);
        }

        SearchProfileData data = new SearchProfileData();

        data.setSize(new DoubleInterval(searchProfileBean.getLowerBoundSize(), Double.POSITIVE_INFINITY));
        data.setRooms(new DoubleInterval(searchProfileBean.getLowerBoundRooms(), Double.POSITIVE_INFINITY));
        data.setRent(new DoubleInterval(0.0, searchProfileBean.getUpperBoundRent()));

        if (StringUtils.isEmpty(searchProfileBean.getName())) {
            data.setName(generateName("", searchProfileBean.getAddress()));
        } else {
            data.setName(searchProfileBean.getName());
        }

        PropertySearcherSearchProfile searchProfile = new PropertySearcherSearchProfile();
        searchProfile.setData(data);
        searchProfile.setUserProfile(userProfile);
        searchProfile.setManuallyCreated(true);
        searchProfile.setType(searchProfileBean.getType());

        if (searchProfileBean.getType() == PropertySearcherSearchProfileType.DISTRICT) {
            Set<District> districts = districtRepository.findAllByIdIn(searchProfileBean.getDistrictIds());
            searchProfile.setDistricts(districts);
            searchProfileRepository.save(searchProfile);
        } else {
            populateAddress(searchProfileBean.getAddress(), data);
            data.setRadius(searchProfileBean.getRadius());
            searchProfileRepository.customManualEntryRadius(searchProfile);
        }

        PropertySearcherSearchProfile lastSearchProfile = searchProfileRepository.customFindLastByUserProfile(userProfile);
        if (lastSearchProfile != null) {
            propertySearcherPropertyProposalService.createProposalsFromSP(lastSearchProfile);
        }
    }

    private String generateName(String prefix, Address address) {
        return prefix + address.getStreet() + " " + address.getZipCode();
    }

    private void populateAddress(Address address, SearchProfileData data) {
        address.setCoordinates(googleMapsService.getGeoCoordinates(address.toGoogleString()));
        data.setAddress(address);
    }

    public void flagSearchProfilesAsDeletedAndDeleteProposals(PropertySearcherUserProfile userProfile) {
        searchProfileRepository.customDeleteByUserProfileId(userProfile.getId());
        propertySearcherPropertyProposalService.deleteProposals(userProfile);
    }

    public void flagSearchProfileAsDeletedAndDeleteProposals(PropertySearcherSearchProfile searchProfile) {
        searchProfile.setDeleted(true);
        searchProfileRepository.save(searchProfile);
        propertySearcherPropertyProposalService.deleteProposals(searchProfile);
    }

    public boolean searchProfileComplete(SearchProfileBean searchProfileBean) {
        boolean isRadius = searchProfileBean.getType() != PropertySearcherSearchProfileType.RADIUS;
        boolean addressFilled = searchProfileBean.getAddress() != null;
        boolean lowerBoundFilled = searchProfileBean.getLowerBoundRooms() != null;
        boolean lowerBoundSizeFilled = searchProfileBean.getLowerBoundSize() != null;
        boolean nameFilled = searchProfileBean.getName() != null;
        boolean radiusFilled = searchProfileBean.getRadius() != null;
        boolean upperBoundRentFilled = searchProfileBean.getUpperBoundRent() != null;
        boolean isDistrict = searchProfileBean.getType() == PropertySearcherSearchProfileType.DISTRICT;
        boolean districtIdsFilled = searchProfileBean.getDistrictIds() != null
                && !searchProfileBean.getDistrictIds().isEmpty();

        return (isRadius || addressFilled) &&
                lowerBoundFilled &&
                lowerBoundSizeFilled &&
                nameFilled &&
                (isRadius || radiusFilled) &&
                upperBoundRentFilled &&
                (!isDistrict || districtIdsFilled);
    }

}
