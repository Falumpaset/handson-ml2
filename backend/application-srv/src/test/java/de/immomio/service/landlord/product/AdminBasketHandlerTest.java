package de.immomio.service.landlord.product;

import de.immomio.AbstractTest;
import de.immomio.constants.CheckoutResult;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.BasketCheckoutException;
import de.immomio.data.base.bean.customer.PaymentMethod;
import de.immomio.data.base.bean.product.basket.CheckoutBean;
import de.immomio.data.base.type.customer.PaymentMethodType;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasket;
import de.immomio.model.repository.service.landlord.product.basket.LandlordProductBasketRepository;
import de.immomio.utils.TestHelper;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Niklas Lindemann
 */
public class AdminBasketHandlerTest extends AbstractTest {

    @Mock
    private LandlordProductBasketRepository productBasketRepository;

    @Mock
    private AdminLandlordCheckoutService landlordCheckoutService;

    @Mock
    private AdminLandlordCacheEvictService cacheEvictService;

    @InjectMocks
    private AdminBasketHandler adminBasketHandler;

    @Test(expected = ApiValidationException.class)
    public void checkoutBasketNull() throws BasketCheckoutException {
        when(productBasketRepository.findById(null)).thenReturn(null);
        adminBasketHandler.checkoutBasket(1L, null);
    }

    @Test
    public void checkoutBasketSuccess() throws BasketCheckoutException {

        var checkoutBasket = new LandlordProductBasket();
        var customer = TestHelper.generateLandlordCustomer(1L);
        customer.setPaymentMethods(singletonList(new PaymentMethod(PaymentMethodType.INVOICE, true)));
        checkoutBasket.setCustomer(customer);

        var checkoutBean = new CheckoutBean();
        checkoutBean.setPaymentMethod(PaymentMethodType.INVOICE);

        when(productBasketRepository.findById(anyLong())).thenReturn(Optional.of(checkoutBasket));
        when(landlordCheckoutService.processCheckout(any(), any())).thenReturn(CheckoutResult.ACCEPTED);

        adminBasketHandler.checkoutBasket(1L, checkoutBean);

        verify(cacheEvictService, times(1)).evictCustomerCache(any());
    }
}