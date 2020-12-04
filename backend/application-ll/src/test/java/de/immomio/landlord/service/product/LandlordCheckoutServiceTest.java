package de.immomio.landlord.service.product;

import de.immomio.constants.CheckoutResult;
import de.immomio.constants.exceptions.BasketCheckoutException;
import de.immomio.data.base.bean.product.basket.CheckoutBean;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasket;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketProductAddon;
import de.immomio.model.repository.landlord.product.basket.LandlordProductBasketRepository;
import de.immomio.service.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import utils.TestHelper;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doReturn;

/**
 * @author Niklas Lindemann
 */
public class LandlordCheckoutServiceTest extends AbstractTest {

    @Mock
    private LandlordProductBasketRepository productBasketRepository;

    @Mock
    private LandlordCacheEvictService cacheEvictService;

    @Spy
    @InjectMocks
    private LandlordCheckoutService landlordCheckoutService;


    @Test(expected = BasketCheckoutException.class)
    public void processCheckoutValidationFails() throws BasketCheckoutException {
        landlordCheckoutService.processCheckout(null, new CheckoutBean());
    }

    @Test
    public void processCheckout() throws BasketCheckoutException {
        LandlordProductBasket basket = new LandlordProductBasket();
        basket.setCustomer(TestHelper.generateLandlordCustomer(1L));
        basket.getProductAddons().add(new LandlordProductBasketProductAddon());
        doReturn(CheckoutResult.ACCEPTED).when(landlordCheckoutService).checkout(any(), any(), anyBoolean(), anyBoolean() , any());
        CheckoutResult checkoutResult = landlordCheckoutService.processCheckout(basket, new CheckoutBean());
        Assert.assertEquals(CheckoutResult.ACCEPTED, checkoutResult);
    }
}