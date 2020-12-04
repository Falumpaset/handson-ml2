package de.immomio.service.property;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.landlord.service.property.PropertyCountService;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.service.AbstractTest;
import de.immomio.service.property.cache.PropertyCacheCountService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import utils.TestHelper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Niklas Lindemann
 */

public class PropertyCountServiceTest extends AbstractTest {

    private static final long SIZE_OF_INVITEES = 3L;

    private static final long SIZE_OF_APPLICATIONS = 4L;

    private static final long SIZE_OF_PROPOSALS = 5L;

    private Property property;

    @Mock
    private PropertyCacheCountService propertyCacheCountService;

    @Mock
    private PropertyApplicationRepository propertyApplicationRepository;

    @InjectMocks
    private PropertyCountService populateCountService;

    @Before
    public void init() {
        property = spy(TestHelper.generateProperty(TestHelper.generateLandlordCustomer(1L), 1L));
    }

    @Test
    public void populateSizeOfInvitees() {
        when(propertyCacheCountService.getInviteeCount(any(Long.class))).thenReturn(SIZE_OF_INVITEES);

        populateCountService.populatePropertyWithCountData(property);
        Long sizeOfInvitees = property.getSizeOfInvitees();

        verify(property, never()).getPropertyApplications();
        assertEquals(SIZE_OF_INVITEES, (long) sizeOfInvitees);
    }

    @Test
    public void populateNothingAndGettingSizeOfInvitees() {
        property.getSizeOfInvitees();

        verify(property, atLeastOnce()).getPropertyApplications();
    }

    @Test
    public void populateApplications() {
        Long id = property.getId();
        when(propertyCacheCountService.getApplicationCount(any(Long.class))).thenReturn(SIZE_OF_APPLICATIONS);

        populateCountService.populatePropertyWithCountData(property);
        Long sizeOfApplications = property.getSizeOfApplications();

        verify(property, never()).getPropertyApplications();
        assertEquals(SIZE_OF_APPLICATIONS, (long) sizeOfApplications);
    }

    @Test
    public void populateNothingAndGettingSizeOfApplications() {
        property.getSizeOfApplications();

        verify(property, atLeastOnce()).getPropertyApplications();
    }

    @Test
    public void populateProposals() {
        Long id = property.getId();
        when(propertyCacheCountService.getProposalCount(any(Long.class))).thenReturn(SIZE_OF_PROPOSALS);

        populateCountService.populatePropertyWithCountData(property);
        Long sizeOfProposals = property.getSizeOfProposals();

        verify(property, never()).getPropertyProposals();
        assertEquals(SIZE_OF_PROPOSALS, (long) sizeOfProposals);
    }

    @Test
    public void populateNothingAndGettingSizeOfProposals() {
        property.getSizeOfProposals();

        verify(property, atLeastOnce()).getPropertyProposals();
    }
}