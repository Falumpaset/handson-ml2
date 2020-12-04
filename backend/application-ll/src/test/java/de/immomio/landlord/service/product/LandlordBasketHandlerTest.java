package de.immomio.landlord.service.product;

import de.immomio.constants.CheckoutResult;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.BasketCheckoutException;
import de.immomio.data.base.bean.product.basket.CheckoutBean;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasket;
import de.immomio.model.repository.landlord.product.basket.LandlordProductBasketRepository;
import de.immomio.service.AbstractTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import utils.TestHelper;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Niklas Lindemann
 */
public class LandlordBasketHandlerTest extends AbstractTest {

    @Mock
    private LandlordProductBasketRepository productBasketRepository;

    @Mock
    private LandlordCheckoutService landlordCheckoutService;

    @Mock
    private LandlordCacheEvictService cacheEvictService;

    @InjectMocks
    private LandlordBasketHandler adminBasketHandler;

    @Test(expected = ApiValidationException.class)
    public void checkoutBasketNull() throws BasketCheckoutException {
        adminBasketHandler.checkoutBasket(1L, null);
    }

    @Test
    @Ignore
    public void checkoutBasketSuccess() throws BasketCheckoutException {
        LandlordProductBasket basket = new LandlordProductBasket();
        basket.setCustomer(TestHelper.generateLandlordCustomer(1L));
        when(productBasketRepository.customFindOne(anyLong())).thenReturn(basket);
        when(landlordCheckoutService.processCheckout(any(), any())).thenReturn(CheckoutResult.ACCEPTED);
        CheckoutResult checkoutResult = adminBasketHandler.checkoutBasket(1L, new CheckoutBean());
        verify(cacheEvictService, times(1)).evictCustomerCache(any());
        Assert.assertEquals(CheckoutResult.ACCEPTED, checkoutResult);
    }
}