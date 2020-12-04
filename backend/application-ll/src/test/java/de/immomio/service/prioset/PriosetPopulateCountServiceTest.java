package de.immomio.service.prioset;

import de.immomio.data.landlord.bean.property.EntityCountBean;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.landlord.service.prioset.PriosetPopulateCountService;
import de.immomio.model.repository.shared.property.PropertyRepository;
import de.immomio.service.AbstractTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import utils.TestHelper;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Niklas Lindemann
 */
public class PriosetPopulateCountServiceTest extends AbstractTest {

    private static final long SIZE_OF_PROPERTIES = 3L;

    private static final long ID = 1L;

    private Prioset prioset;

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private PriosetPopulateCountService populateCountService;

    private List<EntityCountBean> getMockedCountValues(Long propertyId, Long count) {
        return Collections.singletonList(new EntityCountBean(propertyId, count));
    }

    @Before
    public void init() {
        prioset = spy(TestHelper.generatePrioset(TestHelper.generateLandlordCustomer(ID), ID));
    }

    @Test
    public void populateSizeOfProperties() {
        Long id = prioset.getId();
        when(propertyRepository.getSizeOfProperties(anyListOf(Prioset.class))).thenReturn(getMockedCountValues(id, SIZE_OF_PROPERTIES));

        populateCountService.populatePriosetWithCountData(prioset);
        Long sizeOfInvitees = prioset.getSizeOfProperties();

        verify(prioset, never()).getProperties();
        assertEquals(SIZE_OF_PROPERTIES, (long) sizeOfInvitees);
    }

    @Test
    public void populateNothingAndGettingSizeOfProperties() {
        prioset.getSizeOfProperties();

        verify(prioset, atLeastOnce()).getProperties();
    }
}