package de.immomio.broker.service;

import de.immomio.broker.AbstractTest;
import de.immomio.broker.utils.TestHelper;
import de.immomio.cloud.service.google.GoogleMapsService;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Freddy Sawma.
 */
public class BrokerGeoCodingServiceTest extends AbstractTest {

    @Mock
    private BasePropertyRepository propertyRepository;

    @Mock
    private GoogleMapsService googleMapsService;

    @Spy
    @InjectMocks
    private BrokerGeoCodingService brokerGeoCodingService;

    @Test
    public void saveProperty() {
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer());
        brokerGeoCodingService.saveProperty(property);

        verify(propertyRepository, times(1)).save(eq(property));
    }

    @Test
    public void customUpdatePropertyLocation() {
        brokerGeoCodingService.customUpdatePropertyLocation(anyLong());

        verify(propertyRepository, times(1)).customUpdatePropertyLocation(anyLong());
    }

    @Test
    public void getGoogleMapsService() {
        brokerGeoCodingService.getGoogleMapsService();

        verify(brokerGeoCodingService, times(1)).getGoogleMapsService();
    }
}