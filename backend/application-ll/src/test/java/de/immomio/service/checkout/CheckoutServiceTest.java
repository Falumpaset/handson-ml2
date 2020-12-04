package de.immomio.service.checkout;

import de.immomio.data.base.entity.product.basket.BaseProductBasketProductAddon;
import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.addon.LandlordAddonProduct;
import de.immomio.data.landlord.entity.product.addonproduct.LandlordCustomerAddonProduct;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasket;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketProductAddon;
import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import de.immomio.landlord.service.product.LandlordCheckoutService;
import de.immomio.model.repository.landlord.product.basket.LandlordProductBasketProductAddonRepository;
import de.immomio.model.repository.landlord.product.customer.addon.LandlordCustomerAddonProductRepository;
import de.immomio.service.AbstractTest;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Niklas Lindemann
 */

@TestPropertySource(properties = { "products.trial.possible=false" })
public class CheckoutServiceTest extends AbstractTest {

    @Mock
    private LandlordCustomerAddonProductRepository addonProductRepository;

    @Mock
    private LandlordProductBasketProductAddonRepository basketProductAddonRepository;

    @InjectMocks
    @Spy
    private LandlordCheckoutService landlordCheckoutService;


    private List<LandlordCustomerAddonProduct> generateAddons(int amount, boolean renew, AddonType addonType){
        List<LandlordCustomerAddonProduct> addonProducts = new ArrayList<>();
        IntStream.range(0, amount).forEach(i -> {
            LandlordAddonProduct addonProduct = getLandlordAddonProduct(addonType);

            LandlordCustomerAddonProduct customerAddonProduct = new LandlordCustomerAddonProduct();
            customerAddonProduct.setRenew(renew);
            customerAddonProduct.setAddonProduct(addonProduct);
            addonProducts.add(customerAddonProduct);
        });
        return addonProducts;
    }

    private LandlordAddonProduct getLandlordAddonProduct(AddonType addonType) {
        LandlordAddonProduct addonProduct = new LandlordAddonProduct();
        addonProduct.setAddonType(addonType);
        return addonProduct;
    }

    private LandlordProductBasketProductAddon generateBasketAddons(int quantity, AddonType addonType){
        LandlordProductAddon landlordProductAddon = new LandlordProductAddon();
        landlordProductAddon.setAddonProduct(getLandlordAddonProduct(addonType));

        LandlordProductBasketProductAddon addon = new LandlordProductBasketProductAddon();
        addon.setQuantity(quantity);
        addon.setProductAddon(landlordProductAddon);
        return addon;
    }

    private AddonType getAddonType(LandlordCustomerAddonProduct addon) {
        return addon.getAddonProduct().getAddonType();
    }

    @Test
    public void basketShouldBeEmpty(){
        // Given

        LandlordCustomerProduct landlordCustomerProduct = new LandlordCustomerProduct();
        landlordCustomerProduct.getAddons().addAll(generateAddons(3, true, AddonType.AGENT));
        landlordCustomerProduct.getAddons().addAll(generateAddons(2, false, AddonType.AGENT));
        landlordCustomerProduct.getAddons().addAll(generateAddons(1, false, AddonType.IMPORT));

        LandlordCustomer landlordCustomer = new LandlordCustomer();
        landlordCustomer.setProducts(Collections.singletonList(landlordCustomerProduct));

        LandlordProductBasket basket = new LandlordProductBasket();
        basket.setCustomer(landlordCustomer);
        basket.getProductAddons().add(generateBasketAddons(2, AddonType.AGENT));
        basket.getProductAddons().add(generateBasketAddons(1, AddonType.IMPORT));

        // When
        landlordCheckoutService.renewExpiringProducts(basket);

        //Then
        long renewedAgents = landlordCustomerProduct.getAddons().stream()
                .filter(addon -> getAddonType(addon).equals(AddonType.AGENT) && addon.isRenew())
                .count();

        verify(basketProductAddonRepository, times(2)).delete(any(LandlordProductBasketProductAddon.class));
        verify(landlordCheckoutService, times(3)).renewAddon(anyObject());
        verify(addonProductRepository, times(3)).save(any(LandlordCustomerAddonProduct.class));

        assertEquals(0, basket.getProductAddons().size());
        assertEquals(5, renewedAgents);
    }

    @Test
    public void basketShouldHaveOneAddonWithAQuantityOfTwo(){
        // Given
        LandlordCustomerProduct landlordCustomerProduct = new LandlordCustomerProduct();
        landlordCustomerProduct.getAddons().addAll(generateAddons(2, true, AddonType.AGENT));
        landlordCustomerProduct.getAddons().addAll(generateAddons(3, false, AddonType.AGENT));
        landlordCustomerProduct.getAddons().addAll(generateAddons(1, true, AddonType.BRANDING));

        LandlordCustomer landlordCustomer = new LandlordCustomer();
        landlordCustomer.setProducts(Collections.singletonList(landlordCustomerProduct));

        LandlordProductBasket basket = new LandlordProductBasket();
        basket.setCustomer(landlordCustomer);
        basket.setProductAddons(new ArrayList<>(Collections.singletonList(generateBasketAddons(5, AddonType.AGENT))));

        // When
        landlordCheckoutService.renewExpiringProducts(basket);

        // Then
        long renewedAgents = landlordCustomerProduct.getAddons().stream()
                .filter(addon -> getAddonType(addon).equals(AddonType.AGENT) && addon.isRenew())
                .count();

        verify(basketProductAddonRepository, never()).delete(any(LandlordProductBasketProductAddon.class));
        verify(landlordCheckoutService, times(3)).renewAddon(anyObject());
        verify(addonProductRepository, times(3)).save(any(LandlordCustomerAddonProduct.class));


        assertEquals(1, basket.getProductAddons().size());
        assertEquals(2, (int) basket.getProductAddons().get(0).getQuantity());
        assertEquals(5, (int) renewedAgents);

    }

    @Test
    public void basketShouldHaveTwoAddons(){
        // Given
        LandlordCustomerProduct landlordCustomerProduct = new LandlordCustomerProduct();
        landlordCustomerProduct.getAddons().addAll(generateAddons(2, true, AddonType.AGENT));
        landlordCustomerProduct.getAddons().addAll(generateAddons(3, false, AddonType.AGENT));
        landlordCustomerProduct.getAddons().addAll(generateAddons(1, false, AddonType.EMAILEDITOR));

        LandlordCustomer landlordCustomer = new LandlordCustomer();
        landlordCustomer.setProducts(Collections.singletonList(landlordCustomerProduct));

        LandlordProductBasket basket = new LandlordProductBasket();
        basket.setCustomer(landlordCustomer);

        basket.getProductAddons().add(generateBasketAddons(5, AddonType.AGENT));
        basket.getProductAddons().add(generateBasketAddons(1, AddonType.DATAINSIGHTS));
        basket.getProductAddons().add(generateBasketAddons(1, AddonType.EMAILEDITOR));

        // When
        landlordCheckoutService.renewExpiringProducts(basket);

        // Then
        long quantitySum = basket.getProductAddons().stream()
                .mapToInt(BaseProductBasketProductAddon::getQuantity)
                .sum();

        long renewedAgents = landlordCustomerProduct.getAddons().stream()
                .filter(addon -> getAddonType(addon).equals(AddonType.AGENT) && addon.isRenew())
                .count();

        long renewedMailEditors = landlordCustomerProduct.getAddons().stream()
                .filter(addon -> getAddonType(addon).equals(AddonType.EMAILEDITOR) && addon.isRenew())
                .count();

        verify(basketProductAddonRepository, times(1)).delete(any(LandlordProductBasketProductAddon.class));
        verify(landlordCheckoutService, times(4)).renewAddon(anyObject());
        verify(addonProductRepository, times(4)).save(any(LandlordCustomerAddonProduct.class));

        assertEquals(2, basket.getProductAddons().size());
        assertEquals(3, (int) quantitySum);
        assertEquals(5, (int) renewedAgents);
        assertEquals(1, (int) renewedMailEditors);
    }

    @Test
    public void basketShouldntBeChanged() {
        // Given
        LandlordCustomerProduct landlordCustomerProduct = new LandlordCustomerProduct();
        landlordCustomerProduct.getAddons().addAll(generateAddons(2, true, AddonType.AGENT));
        landlordCustomerProduct.getAddons().addAll(generateAddons(1, false, AddonType.EMAILEDITOR));

        LandlordCustomer landlordCustomer = new LandlordCustomer();
        landlordCustomer.setProducts(Collections.singletonList(landlordCustomerProduct));

        LandlordProductBasket basket = new LandlordProductBasket();
        basket.setCustomer(landlordCustomer);

        basket.getProductAddons().add(generateBasketAddons(5, AddonType.AGENT));
        basket.getProductAddons().add(generateBasketAddons(1, AddonType.DATAINSIGHTS));
        basket.getProductAddons().add(generateBasketAddons(1, AddonType.IMPORT));

        // When
        landlordCheckoutService.renewExpiringProducts(basket);

        // Then
        long quantitySum = basket.getProductAddons().stream()
                .mapToInt(BaseProductBasketProductAddon::getQuantity)
                .sum();

        long renewedAgents = landlordCustomerProduct.getAddons().stream()
                .filter(addon -> getAddonType(addon).equals(AddonType.AGENT) && addon.isRenew())
                .count();

        long renewedMailEditors = landlordCustomerProduct.getAddons().stream()
                .filter(addon -> getAddonType(addon).equals(AddonType.EMAILEDITOR) && addon.isRenew())
                .count();

        verify(basketProductAddonRepository, never()).delete(any(LandlordProductBasketProductAddon.class));
        verify(landlordCheckoutService, never()).renewAddon(anyObject());
        verify(addonProductRepository, never()).save(any(LandlordCustomerAddonProduct.class));

        assertEquals(3, basket.getProductAddons().size());
        assertEquals(7, (int) quantitySum);
        assertEquals(2, (int) renewedAgents);
        assertEquals(0, (int) renewedMailEditors);
    }
}