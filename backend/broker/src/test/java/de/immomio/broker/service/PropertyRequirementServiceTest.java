package de.immomio.broker.service;

import de.immomio.broker.AbstractTest;
import de.immomio.broker.service.proposal.PropertyRequirementService;
import de.immomio.broker.utils.TestHelper;
import de.immomio.data.base.bean.helper.DoubleInterval;
import de.immomio.data.base.type.property.PropertyStatus;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfile;
import de.immomio.data.propertysearcher.entity.user.SearchProfileData;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Freddy Sawma.
 */

public class PropertyRequirementServiceTest extends AbstractTest {

    @Mock
    private UserService userService;

    @Spy
    @InjectMocks
    private PropertyRequirementService propertyRequirementService;

    private PropertySearcherSearchProfile profile;
    private Property property;
    private SearchProfileData data;

    @Before
    public void generatePropertyAndProfile() {
        profile = TestHelper.generatePropertySearcherSearchProfile();
        property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer());
        profile.setProperty(property);
        profile.setData(new SearchProfileData());
        property.setData(PropertyData.builder().build());
        data = profile.getData();
        TestHelper.setId(profile.getProperty(), 1L);
    }

    @Test
    public void propertyMeetsRequirementsForCreatingProposalWhenPropertyNotNullAndIdsEqual() {
        assertNotNull(profile.getProperty());
        assertEquals(property.getId(), profile.getProperty().getId());
        when(userService.isUserBlockedForCreatingProposals(any())).thenReturn(false);
        assertFalse(propertyRequirementService.propertyMeetsRequirementsForCreatingProposal(property, profile));
    }

    @Test
    public void propertyDoesNotMeetRequirementsForCreatingProposalWhenPropertyStatusIsReserved() {
        assertNotNull(profile.getProperty());
        profile.getProperty().setStatus(PropertyStatus.RESERVED);
        assertEquals(property.getId(), profile.getProperty().getId());
        assertFalse(propertyRequirementService.propertyMeetsRequirementsForCreatingProposal(property, profile));
    }

    @Test
    public void propertyFulfillsSPWhenPropertyIsRented() {
        property.setTenant(new PropertyTenant());

        assertFalse(propertyRequirementService.propertyFulfillsSP(property, profile));
    }

    @Test
    public void propertyFulfillsSPWhenRentsDontMatch() {
        property.getData().setBasePrice(10.0);
        data.setRent(new DoubleInterval(0.0, 0.0));

        assertFalse(propertyRequirementService.propertyFulfillsSP(property, profile));
    }

    @Test
    public void propertyFulfillsSPWhenSizesDontMatch() {
        property.getData().setBasePrice(0.0);
        data.setRent(new DoubleInterval(0.0, 10.0));
        data.setSize(new DoubleInterval(10.0, 10.0));
        property.getData().setSize(0.0);

        assertFalse(propertyRequirementService.propertyFulfillsSP(property, profile));
    }

    @Test
    public void propertyFulfillsSPWhenRoomsDontMatch() {
        property.getData().setBasePrice(0.0);
        data.setRent(new DoubleInterval(0.0, 10.0));
        data.setSize(new DoubleInterval(0.0, 0.0));
        property.getData().setSize(10.0);
        data.setRooms(new DoubleInterval(10.0, 0.0));
        property.getData().setRooms(0.0);

        assertFalse(propertyRequirementService.propertyFulfillsSP(property, profile));
    }

    @Test
    public void propertyFulfillsSPTrue() {
        property.setTenant(null);
        property.getData().setBasePrice(0.0);
        property.getData().setHeatingCost(0.0);
        property.getData().setServiceCharge(0.0);
        data.setRent(new DoubleInterval(99999.0, 1000000.0));
        data.setSize(new DoubleInterval(0.0, 0.0));
        property.getData().setSize(1000.0);
        data.setRooms(new DoubleInterval(0.0, 0.0));
        property.getData().setRooms(100.0);

        assertTrue(propertyRequirementService.propertyFulfillsSP(property, profile));
    }
}