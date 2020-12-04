package de.immomio.service.landlord;

import de.immomio.AbstractTest;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.model.repository.service.landlord.product.customer.LandlordCustomerProductRepository;
import de.immomio.utils.TestHelper;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Niklas Lindemann
 */
public class LandlordOnboardingServiceTest extends AbstractTest {

    @Mock
    private LandlordCustomerProductRepository customerProductRepository;

    @InjectMocks
    private LandlordOnboardingService onboardingService;

    @Test
    public void deactivate() {
        LandlordCustomerProduct product = new LandlordCustomerProduct();
        product.setRenew(true);
        product.setDueDate(new Date());

        LandlordCustomer landlordCustomer = TestHelper.generateLandlordCustomer(1L);
        landlordCustomer.getProducts().add(product);

        when(customerProductRepository.save(any(LandlordCustomerProduct.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        LandlordCustomerProduct savedProduct = onboardingService.deactivateActiveProduct(landlordCustomer, null);

        Assert.assertFalse(savedProduct.getRenew());
        Assert.assertNotNull(savedProduct.getRenew());
        verify(customerProductRepository, times(1)).save(any(LandlordCustomerProduct.class));
    }
}