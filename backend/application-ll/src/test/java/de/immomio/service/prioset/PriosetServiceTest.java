package de.immomio.service.prioset;

import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.landlord.service.prioset.PriosetService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.landlord.customer.prioset.LandlordPriosetRepository;
import de.immomio.model.repository.shared.property.PropertyRepository;
import de.immomio.service.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static utils.TestHelper.createPrioset;
import static utils.TestHelper.generateLandlordCustomer;
import static utils.TestHelper.generateLandlordUser;

/**
 * @author Niklas Lindemann
 */


public class PriosetServiceTest extends AbstractTest {

    @Mock
    private LandlordPriosetRepository priosetRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private UserSecurityService userSecurityService;

    @InjectMocks
    private PriosetService priosetService;

    @Test
    public void deletePrioset() {
        Prioset prioset = createPrioset(true, 1L);

        priosetService.delete(prioset, null, null);

        verify(priosetRepository, times(1)).delete(any(Prioset.class));
    }

    @Test
    public void replacePriosetWithAnother() {
        Prioset prioset = createPrioset(false, 1L);
        Prioset priosetToReplace = createPrioset(false, 1L);

        when(propertyRepository.saveAll(anyListOf(Property.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        List<Property> properties = priosetService.replacePrioset(prioset, priosetToReplace, null);

        verify(propertyRepository, times(1)).saveAll(anyListOf(Property.class));
        Assert.assertEquals(properties.get(0).getPrioset(), priosetToReplace);
    }

    @Test
    public void replacePriosetWithDefault() {
        Prioset prioset = createPrioset(false, 1L);
        LandlordUser landlordUser = generateLandlordUser(generateLandlordCustomer(1L), LandlordUsertype.COMPANYADMIN, 1L);

        when(propertyRepository.saveAll(anyListOf(Property.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        List<Property> properties = priosetService.replacePrioset(prioset, null, landlordUser.getCustomer());

        verify(propertyRepository, times(1)).saveAll(anyListOf(Property.class));
        verify(priosetRepository, times(1)).save(any(Prioset.class));

        Assert.assertNotEquals(properties.get(0).getPrioset(), prioset);
    }
}