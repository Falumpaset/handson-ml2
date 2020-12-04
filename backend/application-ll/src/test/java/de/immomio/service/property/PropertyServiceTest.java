package de.immomio.service.property;

import de.immomio.beans.landlord.UpdatePropertyBean;
import de.immomio.constants.exceptions.NotAuthorizedException;
import de.immomio.data.base.type.property.PropertyTask;
import de.immomio.data.base.type.property.PropertyWriteProtection;
import de.immomio.data.landlord.bean.prioset.ChangePriosetBean;
import de.immomio.data.landlord.bean.prioset.PriosetData;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.portal.PropertyPortal;
import de.immomio.data.landlord.entity.property.portal.PropertyPortalState;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.landlord.service.customquestion.LandlordCustomQuestionAssociationService;
import de.immomio.landlord.service.property.PropertyCountService;
import de.immomio.landlord.service.property.PropertyService;
import de.immomio.landlord.service.property.portal.PropertyPortalService;
import de.immomio.landlord.service.reporting.indexing.delegate.LandlordPropertyIndexingDelegate;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.landlord.service.sender.LandlordProposalMessageSender;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import de.immomio.model.repository.shared.property.PropertyRepository;
import de.immomio.service.AbstractTest;
import de.immomio.service.RabbitMQService;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import utils.TestComparatorHelper;
import utils.TestHelper;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Niklas Lindemann
 */
public class PropertyServiceTest extends AbstractTest {

    private static final long CUSTOMER_ID_ONE = 1L;

    private static final long CUSTOMER_ID_TWO = 2L;

    private static final long PROPERTY_ID = 1L;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private PropertyCountService populateCountService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private UserSecurityService userSecurityService;

    @Mock
    private LandlordProposalMessageSender messageSender;

    @Mock
    private LandlordPropertyIndexingDelegate propertyIndexingDelegate;

    @InjectMocks
    @Spy
    private PropertyService propertyService;

    @Mock
    private RabbitMQService rabbitMQService;

    @Mock
    private LandlordUserRepository landlordUserRepository;

    @Mock
    private PropertyPortalService propertyPortalService;

    @Test(expected = NotAuthorizedException.class)
    public void findByIdNotNullNotAuthorized() throws NotAuthorizedException {
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer(CUSTOMER_ID_ONE),
                PROPERTY_ID);

        when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));
        when(userSecurityService.getPrincipalUser().getCustomer().getId()).thenReturn(2L);

        propertyService.findById(1L);
    }

    @Test(expected = NotAuthorizedException.class)
    public void findByIdNotNullAuthorized() throws NotAuthorizedException {
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer(CUSTOMER_ID_TWO),
                PROPERTY_ID);

        when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));
        when(userSecurityService.getPrincipalUser().getCustomer().getId()).thenReturn(1L);

        Property propertyFound = propertyService.findById(1L);

        verify(populateCountService, times(1)).populatePropertyWithCountData(eq(property));
        TestComparatorHelper.compareProperty(property, propertyFound);
    }

    @Test
    public void findByIdNull() throws NotAuthorizedException {
        when(propertyRepository.findById(nullable(Long.class))).thenReturn(Optional.empty());

        propertyService.findById(PROPERTY_ID);

        verify(populateCountService, never()).populatePropertyWithCountData(any(Property.class));
    }

    @Test
    public void updateProperty() {
        LandlordCustomer customer = TestHelper.generateLandlordCustomer(1L);
        Property property = TestHelper.generateProperty(customer, 1L);
        UpdatePropertyBean updatePropertyBean = generateUpdatePropertyBean();

        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(landlordUserRepository.findById(anyLong())).thenReturn(Optional.of(TestHelper.generateLandlordUser(LandlordUsertype.COMPANYADMIN, 1L)));

        propertyService.updateProperty(updatePropertyBean, property);

        verify(propertyPortalService, times(1)).mergePropertyPortals(eq(property), anyList());
        verify(populateCountService, times(1)).populatePropertyWithCountData(eq(property));
        verify(messageSender, times(1)).sendProposalUpdateMessage(eq(property), eq(true));
        verify(rabbitMQService, never()).queueProperty(nullable(Property.class));
        verify(propertyIndexingDelegate, times(1)).propertyUpdated(nullable(Property.class));

        comparePropertyUpdate(updatePropertyBean, property);
    }

    @Test
    public void givenActivePropertyToUpdate_PropertyTaskUpdateShouldBeSet() {
        LandlordCustomer customer = TestHelper.generateLandlordCustomer(1L);
        Property property = TestHelper.generateProperty(customer, 1L);
        UpdatePropertyBean updatePropertyBean = generateUpdatePropertyBean();
        var propertyPortal = new PropertyPortal();
        propertyPortal.setState(PropertyPortalState.ACTIVE);
        property.setPortals(Collections.singletonList(propertyPortal));

        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(landlordUserRepository.findById(anyLong())).thenReturn(Optional.of(TestHelper.generateLandlordUser(LandlordUsertype.COMPANYADMIN, 1L)));

        propertyService.updateProperty(updatePropertyBean, property);

        verify(propertyPortalService, times(1)).mergePropertyPortals(eq(property), anyList());
        verify(populateCountService, times(1)).populatePropertyWithCountData(eq(property));
        verify(messageSender, times(1)).sendProposalUpdateMessage(eq(property), eq(true));
        verify(rabbitMQService, times(1)).queueProperty(nullable(Property.class));
        verify(propertyIndexingDelegate, times(1)).propertyUpdated(nullable(Property.class));

        assertEquals(property.getTask(), PropertyTask.UPDATE);
    }

    @Test
    public void setPropertyNote() {
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer(1L), 1L);
        String note = "testNote";

        propertyService.setPropertyNote(property, note);
        verify(propertyRepository, times(1)).save(eq(property));

        assertEquals(property.getNote().getContent(), note);
    }

    private UpdatePropertyBean generateUpdatePropertyBean() {
        UpdatePropertyBean updatePropertyBean = new UpdatePropertyBean();
        PropertyData propertyData = new PropertyData();
        propertyData.setName("Testflat");
        updatePropertyBean.setData(propertyData);
        updatePropertyBean.setUserId(1L);
        updatePropertyBean.setWriteProtection(PropertyWriteProtection.UNPROTECTED);
        return updatePropertyBean;
    }

    private void comparePropertyUpdate(UpdatePropertyBean updateBean, Property property) {
        assertEquals(updateBean.getData(), property.getData());
        assertEquals(updateBean.getWriteProtection(), property.getWriteProtection());
        assertEquals(updateBean.getUserId(), property.getUser().getId());
    }
}

