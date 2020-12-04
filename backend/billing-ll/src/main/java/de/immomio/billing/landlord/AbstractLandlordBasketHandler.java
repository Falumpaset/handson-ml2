package de.immomio.billing.landlord;

import de.immomio.billing.exception.InvoiceGenerationException;
import de.immomio.caching.service.AbstractCacheEvictService;
import de.immomio.constants.CheckoutResult;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.BasketCheckoutException;
import de.immomio.data.base.bean.customer.PaymentMethod;
import de.immomio.data.base.bean.product.basket.BasketValueBean;
import de.immomio.data.base.bean.product.basket.CheckoutBean;
import de.immomio.data.base.bean.product.basket.LandlordProductAddonQuantity;
import de.immomio.data.base.bean.product.basket.LandlordProductBasketBean;
import de.immomio.data.base.bean.product.basket.LandlordProductQuantity;
import de.immomio.data.base.type.customer.PaymentMethodType;
import de.immomio.data.base.type.product.basket.ProductBasketStatus;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasket;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketProduct;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketProductAddon;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketProductAddonToRemove;
import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import de.immomio.model.abstractrepository.product.BaseAbstractProductBasketRepository;
import de.immomio.model.repository.core.landlord.customer.BaseLandlordCustomerRepository;
import de.immomio.model.repository.core.landlord.product.BaseLandlordProductRepository;
import de.immomio.model.repository.core.landlord.product.productaddon.BaseLandlordProductAddonRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Slf4j
public abstract class AbstractLandlordBasketHandler<
        PBR extends BaseAbstractProductBasketRepository<LandlordProductBasket>,
        CS extends AbstractLandlordCheckoutService<?, ?, ?, ?, ?, ?, ?>,
        CES extends AbstractCacheEvictService<LandlordCustomer>,
        PAR extends BaseLandlordProductAddonRepository,
        PR extends BaseLandlordProductRepository,
        CR extends BaseLandlordCustomerRepository> {
    private PBR productBasketRepository;
    private CS checkoutService;
    private CES cacheEvictService;
    private CR customerRepository;
    private PAR productAddonRepository;
    private PR productRepository;

    public AbstractLandlordBasketHandler(
            PBR productBasketRepository,
            CS checkoutService,
            CES cacheEvictService,
            CR customerRepository,
            PAR productAddonRepository,
            PR productRepository
    ) {
        this.productBasketRepository = productBasketRepository;
        this.checkoutService = checkoutService;
        this.cacheEvictService = cacheEvictService;
        this.customerRepository = customerRepository;
        this.productAddonRepository = productAddonRepository;
        this.productRepository = productRepository;
    }

    public CheckoutResult checkoutBasket(Long basketId, CheckoutBean checkoutBean) throws BasketCheckoutException {
        LandlordProductBasket basket = productBasketRepository.findById(basketId).orElseThrow(() -> new ApiValidationException("basket not found"));


        List<PaymentMethod> paymentMethods = basket.getCustomer().getPaymentMethods();
        if (checkoutBean.getPaymentMethod() != PaymentMethodType.INVOICE) {
            throw new ApiValidationException("ONLY_INVOICE_SUPPORTED");
        }
        boolean noValidPaymentMethod = paymentMethods.stream().noneMatch(paymentMethod -> paymentMethod.getMethod() == PaymentMethodType.INVOICE && paymentMethod.getPreferred());
        if (noValidPaymentMethod) {
            throw new ApiValidationException("NO_VALID_PAYMENT_METHOD_FOUND");
        }
        CheckoutResult checkoutResult = checkoutService.processCheckout(basket, checkoutBean);
        if (checkoutResult == CheckoutResult.ACCEPTED || checkoutResult == CheckoutResult.PAYMENT_REQUIRED) {
            initializeAddons(basket);
            cacheEvictService.evictCustomerCache(basket.getCustomer());
        }
        return checkoutResult;
    }

    public BasketValueBean basketValue(LandlordProductBasketBean basketBean) throws InvoiceGenerationException {
        LandlordProductBasket basket = createBasket(basketBean);
        return checkoutService.basketValue(basket, basketBean.getPaymentMethod());
    }

    public LandlordProductBasket createAndSaveBasket(LandlordProductBasketBean basketBean) {
        LandlordProductBasket basket = createBasket(basketBean);
        return productBasketRepository.save(basket);
    }

    public boolean paymentRequired(Long basketId, CheckoutBean checkout) throws InvoiceGenerationException {
        LandlordProductBasket basket = productBasketRepository.findById(basketId).orElseThrow();
        return checkoutService.paymentRequired(basket, checkout);
    }

    private LandlordProductBasket createBasket(LandlordProductBasketBean basketBean) {
        LandlordProductBasket basket = new LandlordProductBasket();
        List<LandlordProductBasketProduct> basketProducts = new ArrayList<>();
        List<LandlordProductBasketProductAddon> basketProductAddons = new ArrayList<>();
        List<LandlordProductBasketProductAddonToRemove> basketProductAddonsToRemove = new ArrayList<>();
        LandlordCustomer customer = customerRepository.findById(basketBean.getCustomerId()).orElseThrow();

        basket.setCustomer(customer);
        for (LandlordProductQuantity productQuantity : basketBean.getProducts()) {

            LandlordProductBasketProduct basketProduct = new LandlordProductBasketProduct();
            LandlordProduct product;
            product = productRepository.findById(productQuantity.getProductId()).orElseThrow();

            basketProduct.setProduct(product);
            basketProduct.setProductBasket(basket);
            basketProduct.setQuantity(productQuantity.getQuantity());
            basketProducts.add(basketProduct);
        }
        basket.setProducts(basketProducts);

        for (LandlordProductAddonQuantity productAddonQuantity : basketBean.getAddonProducts()) {
            LandlordProductBasketProductAddon basketProductAddon = new LandlordProductBasketProductAddon();

            LandlordProductAddon productAddon;
            productAddon = productAddonRepository.findById(productAddonQuantity.getProductAddonId()).orElseThrow();

            basketProductAddon.setProductAddon(productAddon);
            basketProductAddon.setProductBasket(basket);
            basketProductAddon.setQuantity(productAddonQuantity.getQuantity());
            basketProductAddons.add(basketProductAddon);
        }

        basket.setProductAddons(basketProductAddons);
        for (LandlordProductAddonQuantity productAddonQuantity : basketBean.getAddonProductsToRemove()) {
            LandlordProductAddon productAddon;
            productAddon = productAddonRepository.findById(productAddonQuantity.getProductAddonId()).orElseThrow();
            LandlordProductBasketProductAddonToRemove basketProductAddonToRemove = new LandlordProductBasketProductAddonToRemove();
            basketProductAddonToRemove.setProductAddon(productAddon);
            basketProductAddonToRemove.setProductBasket(basket);
            basketProductAddonToRemove.setQuantity(productAddonQuantity.getQuantity());
            basketProductAddonsToRemove.add(basketProductAddonToRemove);
        }

        basket.setProductAddonsToRemove(basketProductAddonsToRemove);

        basket.setStatus(ProductBasketStatus.PROCESSING);

        return basket;
    }

    public abstract void initializeAddons(LandlordProductBasket basket);


}
