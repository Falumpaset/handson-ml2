package de.immomio.service.searchprofile;

import de.immomio.cloud.service.google.GoogleMapsService;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfile;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.common.GeoCoordinates;
import de.immomio.model.repository.shared.location.DistrictRepository;
import de.immomio.model.repository.shared.searchprofile.PropertySearcherSearchProfileRepository;
import de.immomio.data.propertysearcher.bean.searchprofile.SearchProfileBean;
import de.immomio.service.propertyProposals.PropertySearcherPropertyProposalService;
import de.immomio.service.searchProfile.SearchProfileService;
import de.immomio.utils.TestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Freddy Sawma
 */

@ExtendWith(MockitoExtension.class)
public class SearchProfileServiceTest {

    @Mock
    private PropertySearcherSearchProfileRepository searchProfileRepository;

    @Mock
    private PropertySearcherPropertyProposalService propertySearcherPropertyProposalService;

    @Mock
    private GoogleMapsService googleMapsService;

    @Mock
    private DistrictRepository districtRepository;

    @Spy
    @InjectMocks
    private SearchProfileService searchProfileService;

    @Test
    public void createWithProperty() {
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer());
        PropertySearcherUser user = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer());

        searchProfileService.create(property, user.getMainProfile());

        verify(searchProfileRepository, times(1)).customFindByUserProfileAndProperty(user.getMainProfile(), property);
        verify(propertySearcherPropertyProposalService, times(1))
                .createProposalsFromSP(nullable(PropertySearcherSearchProfile.class));
        verify(districtRepository, times(1)).findAllByZipCodesZipCodeContains(anyString());
    }

    @Test
    public void createWithPropertyWhenLastProfileIsNull() {
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer());
        PropertySearcherUserProfile userProfile = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer()).getMainProfile();

        searchProfileService.create(property, userProfile);

        verify(searchProfileRepository, times(1)).customFindByUserProfileAndProperty(userProfile, property);
        verify(propertySearcherPropertyProposalService, times(1))
                .createProposalsFromSP(nullable(PropertySearcherSearchProfile.class));
    }

    @Test
    public void createWithoutProperty() {
        PropertySearcherUserProfile userProfile = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer()).getMainProfile();
        SearchProfileBean bean = TestHelper.generateSearchProfileBean();

        when(googleMapsService.getGeoCoordinates(anyString())).thenReturn(new GeoCoordinates());
        PropertySearcherSearchProfile returnedProfile = new PropertySearcherSearchProfile();
        when(searchProfileRepository.customFindLastByUserProfile(any(PropertySearcherUserProfile.class)))
                .thenReturn(returnedProfile);

        searchProfileService.create(bean, userProfile);

        verify(searchProfileRepository, times(1))
                .customManualEntryRadius(any(PropertySearcherSearchProfile.class));
        verify(searchProfileRepository, times(1)).customFindLastByUserProfile(userProfile);
        verify(propertySearcherPropertyProposalService, times(1))
                .createProposalsFromSP(returnedProfile);
    }

    @Test
    public void createWithoutPropertyWhenLastProfileIsNull() {
        PropertySearcherUserProfile userProfile = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer()).getMainProfile();
        SearchProfileBean bean = TestHelper.generateSearchProfileBean();

        when(googleMapsService.getGeoCoordinates(anyString())).thenReturn(new GeoCoordinates());
        when(searchProfileRepository.customFindLastByUserProfile(any(PropertySearcherUserProfile.class)))
                .thenReturn(null);

        searchProfileService.create(bean, userProfile);

        verify(searchProfileRepository, times(1))
                .customManualEntryRadius(any(PropertySearcherSearchProfile.class));
        verify(searchProfileRepository, times(1)).customFindLastByUserProfile(userProfile);
        verify(propertySearcherPropertyProposalService, never())
                .createProposalsFromSP(any(PropertySearcherSearchProfile.class));
    }

    @Test
    public void flagSearchProfilesAsDeletedAndDeleteProposals() {
        PropertySearcherUserProfile userProfile =TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer()).getMainProfile();
        searchProfileService.flagSearchProfilesAsDeletedAndDeleteProposals(userProfile);

        verify(searchProfileRepository, times(1)).customDeleteByUserProfileId(userProfile.getId());
        verify(propertySearcherPropertyProposalService, times(1)).deleteProposals(userProfile);
    }

    @Test
    public void flagSearchProfileAsDeletedAndDeleteProposals() throws Exception {
        PropertySearcherSearchProfile profile = TestHelper.generatePropertySearcherSearchProfile();
        searchProfileService.flagSearchProfileAsDeletedAndDeleteProposals(profile);

        Assertions.assertTrue(profile.getDeleted());

        verify(searchProfileRepository, times(1)).save(profile);
        verify(propertySearcherPropertyProposalService, times(1)).deleteProposals(profile);
    }

    @Test
    public void searchProfileComplete() {
        SearchProfileBean bean = TestHelper.generateSearchProfileBean();

        Assertions.assertTrue(searchProfileService.searchProfileComplete(bean));
    }

    @Test
    public void searchProfileCompleteWhenOnePropertyIsNull() {
        SearchProfileBean bean = TestHelper.generateSearchProfileBean();
        bean.setName(null);

        Assertions.assertFalse(searchProfileService.searchProfileComplete(bean));
    }
}