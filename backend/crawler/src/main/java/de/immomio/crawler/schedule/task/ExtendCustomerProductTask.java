package de.immomio.crawler.schedule.task;

import de.immomio.billing.provider.AbstractBillingProvider;
import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.data.base.bean.product.basket.CheckoutBean;
import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.base.type.customer.PaymentMethodType;
import de.immomio.data.base.type.product.basket.ProductBasketStatus;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.discount.LandlordCustomerProductAddonDiscount;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.product.addonproduct.LandlordCustomerAddonProduct;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasket;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketProduct;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketProductAddon;
import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import de.immomio.exception.ProductBasketHandlingException;
import de.immomio.model.repository.core.landlord.discount.BaseLandlordCustomerProductAddonDiscountRepository;
import de.immomio.model.repository.core.landlord.product.basket.BaseLandlordProductBasketProductAddonRepository;
import de.immomio.model.repository.core.landlord.product.basket.BaseLandlordProductBasketProductRepository;
import de.immomio.model.repository.core.landlord.product.basket.BaseLandlordProductBasketRepository;
import de.immomio.model.repository.core.landlord.product.customer.BaseCustomerProductRepository;
import de.immomio.model.repository.core.landlord.product.productaddon.BaseLandlordProductAddonRepository;
import de.immomio.service.landlord.CrawlerLandlordCheckoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
@Slf4j
@Component
@Transactional
public class ExtendCustomerProductTask extends BaseTask {

    private final BaseCustomerProductRepository customerProductRepository;

    private final BaseLandlordProductBasketRepository landlordProductBasketRepository;

    private final BaseLandlordProductBasketProductRepository landlordProductBasketProductRepository;

    private final BaseLandlordProductBasketProductAddonRepository landlordProductBasketProductAddonRepository;

    private final BaseLandlordProductAddonRepository productAddonRepository;

    private final BaseLandlordCustomerProductAddonDiscountRepository productAddonDiscountRepository;

    private final CrawlerLandlordCheckoutService checkoutService;

    @Autowired
    public ExtendCustomerProductTask(
            BaseCustomerProductRepository customerProductRepository,
            BaseLandlordProductBasketRepository landlordProductBasketRepository,
            BaseLandlordProductBasketProductRepository landlordProductBasketProductRepository,
            BaseLandlordProductBasketProductAddonRepository landlordProductBasketProductAddonRepository,
            BaseLandlordProductAddonRepository productAddonRepository,
            BaseLandlordCustomerProductAddonDiscountRepository productAddonDiscountRepository,
            CrawlerLandlordCheckoutService checkoutService
    ) {
        this.customerProductRepository = customerProductRepository;
        this.landlordProductBasketRepository = landlordProductBasketRepository;
        this.landlordProductBasketProductRepository = landlordProductBasketProductRepository;
        this.landlordProductBasketProductAddonRepository = landlordProductBasketProductAddonRepository;
        this.productAddonRepository = productAddonRepository;
        this.productAddonDiscountRepository = productAddonDiscountRepository;
        this.checkoutService = checkoutService;
    }

    @Override
    public boolean run() {
        try {
            extendProducts();
        } catch (Exception ex) {
            log.error("Exception thrown during crawling -> ", ex);
        }

        return true;
    }

    private void extendProducts() {

        Set<LandlordCustomerProduct> customerProducts = customerProductRepository.findByRenewAndDueDateIsBefore(true,
                new Date());

        customerProducts.forEach(customerProduct -> {
            try {
                LandlordCustomer customer = customerProduct.getCustomer();

                LandlordProductBasket basket = new LandlordProductBasket();
                LandlordProductBasketProduct product = new LandlordProductBasketProduct();

                List<LandlordProductBasketProduct> products = new ArrayList<>();

                basket.setCustomer(customer);
                basket.setStatus(ProductBasketStatus.PROCESSING);

                try {
                    basket = landlordProductBasketRepository.save(basket);
                } catch (Exception e) {
                    throw new ProductBasketHandlingException("Error saving the initial basket with id: ", e, basket);
                }

                LandlordProduct nextProduct = customerProduct.getNextProduct();
                if (nextProduct != null && !nextProduct.equals(customerProduct.getProduct())) {
                    product.setProduct(nextProduct);
                } else {
                    product.setProduct(customerProduct.getProduct());
                }
                product.setProductBasket(basket);
                product.setQuantity(1);

                try {
                    product = landlordProductBasketProductRepository.save(product);
                } catch (Exception e) {
                    throw new ProductBasketHandlingException("Error saving the product for the basket with id: ", e,
                            basket);
                }

                products.add(product);

                basket.setProducts(products);

                Map<Long, List<LandlordCustomerAddonProduct>> addonCount = new HashMap<>();
                for (LandlordCustomerAddonProduct tmpAddon : customerProduct.getAddons()) {
                    if (!tmpAddon.isRenew()) {
                        continue;
                    }

                    List<LandlordCustomerAddonProduct> value =
                            addonCount.computeIfAbsent(tmpAddon.getAddonProduct().getId(), k -> new ArrayList<>());

                    value.add(tmpAddon);
                }

                List<LandlordProductBasketProductAddon> addons = new ArrayList<>();
                for (List<LandlordCustomerAddonProduct> list : addonCount.values()) {
                    LandlordProductBasketProductAddon addon = new LandlordProductBasketProductAddon();

                    LandlordProductAddon nextProductAddon = productAddonRepository
                            .findByProductAndAddonProductAddonType(nextProduct, list.get(0).getAddonProduct().getAddonType());

                    LandlordProductAddon currentProductAddon = productAddonRepository
                            .findByProductAndAddonProductAddonType(customerProduct.getProduct(), list.get(0).getAddonProduct().getAddonType());


                    if (nextProductAddon != null && !nextProductAddon.equals(currentProductAddon)) {
                        synchronizeDiscountsForNewProduct(nextProductAddon, currentProductAddon, customer);
                        addon.setProductAddon(nextProductAddon);
                    } else {
                        addon.setProductAddon(currentProductAddon);
                    }

                    addon.setProductBasket(basket);
                    addon.setQuantity(list.size());

                    try {
                        addon = landlordProductBasketProductAddonRepository.save(addon);
                    } catch (Exception e) {
                        throw new ProductBasketHandlingException("Error saving an addon for the basket with id: ", e,
                                basket);
                    }

                    addons.add(addon);
                }
                PaymentMethodType paymentMethod = basket.getCustomer().getPreferredPaymentMethod();
                AbstractBillingProvider<LandlordCustomer, ?, ?, ?, ?, ?> paymentProvider = checkoutService
                        .getPaymentProvider(paymentMethod);

                if (paymentProvider == null) {
                    log.warn("No Payment Provider for Payment Method: " + paymentMethod);
                    basket.setStatus(ProductBasketStatus.ERROR);
                    landlordProductBasketRepository.save(basket);
                    return;
                }

                basket.setProductAddons(addons);
                basket.setStatus(ProductBasketStatus.PROCESSING);

                try {
                    basket = landlordProductBasketRepository.save(basket);
                    CheckoutBean checkoutBean = new CheckoutBean();
                    checkoutBean.setPaymentMethod(paymentMethod);
                    checkoutService.processCheckout(basket, checkoutBean);
                } catch (Exception e) {
                    throw new ProductBasketHandlingException("Error saving the final basket with id: ", e, basket);
                }

                // Once a ProductBasket has successfully been created the crawler does not need to revisit the
                // customerProduct, as CS will take care if something goes wring with the basket
                customerProduct.setRenew(false);

                try {
                    customerProductRepository.save(customerProduct);
                } catch (Exception e) {
                    log.error("The customer product abo with ID: " + customerProduct.getId() + " renewal could not be" +
                            " deactivated. Please consider manual action", e);
                }
            } catch (ProductBasketHandlingException e) {
                log.error(e.getMessage() + e.getBasket().getId(), e);
                if (e.getBasket().getId() != null) {
                    landlordProductBasketRepository.deleteById(e.getBasket().getId());
                }
            }

        });
    }

    private void synchronizeDiscountsForNewProduct(
            LandlordProductAddon newProductAddon,
            LandlordProductAddon oldProductAddon,
            LandlordCustomer customer
    ) {
        Optional<LandlordCustomerProductAddonDiscount> newDiscount = getAddonDiscount(newProductAddon, customer);

        Optional<LandlordCustomerProductAddonDiscount> oldDiscount = getAddonDiscount(oldProductAddon, customer);

        oldDiscount.ifPresent(oldProductAddonDiscount -> {
            LandlordCustomerProductAddonDiscount newCreatedDiscount = newDiscount.orElseGet(() -> createNewAddonDiscount(newProductAddon, customer));
            newCreatedDiscount.setDiscount(oldProductAddonDiscount.getDiscount());
            productAddonDiscountRepository.save(newCreatedDiscount);
        });
    }

    private Optional<LandlordCustomerProductAddonDiscount> getAddonDiscount(
            LandlordProductAddon newProductAddon,
            LandlordCustomer customer
    ) {
        return productAddonDiscountRepository
                .findFirstByCustomerAndProductAddonAddonProduct(
                        customer,
                        newProductAddon.getAddonProduct());
    }

    private LandlordCustomerProductAddonDiscount createNewAddonDiscount(
            LandlordProductAddon newProductAddon,
            LandlordCustomer customer
    ) {
        AddonType addonType = newProductAddon.getAddonProduct().getAddonType();
        LandlordProduct product = newProductAddon.getProduct();
        LandlordProductAddon productAddon = productAddonRepository.findByProductAndAddonProductAddonType(product, addonType);
        return new LandlordCustomerProductAddonDiscount(customer, productAddon);
    }
}
