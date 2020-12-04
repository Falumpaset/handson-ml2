package de.immomio.crawler.schedule.task;

import de.immomio.billing.provider.AbstractBillingProvider;
import de.immomio.constants.CheckoutResult;
import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.data.base.bean.customer.PaymentMethod;
import de.immomio.data.base.bean.product.basket.CheckoutBean;
import de.immomio.data.base.type.customer.PaymentMethodType;
import de.immomio.data.base.type.product.basket.ProductBasketStatus;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasket;
import de.immomio.model.repository.core.landlord.product.basket.BaseLandlordProductBasketRepository;
import de.immomio.service.landlord.CrawlerLandlordCheckoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Maik Kingma
 */

@Slf4j
@Component

public class HandleProductBasketTask extends BaseTask {

    private static final int MAX_RECORDS = 5000;

    private final BaseLandlordProductBasketRepository landlordProductBasketRepository;

    private final CrawlerLandlordCheckoutService checkoutService;

    @Autowired
    public HandleProductBasketTask(BaseLandlordProductBasketRepository landlordProductBasketRepository,
                                   CrawlerLandlordCheckoutService checkoutService) {
        this.landlordProductBasketRepository = landlordProductBasketRepository;
        this.checkoutService = checkoutService;
    }

    @Override
    public boolean run() {
        try {
            handle();
        } catch (Exception ex) {
            log.error("Exception thrown during crawling -> ", ex);
        }

        return true;
    }

    private void handle() {
        AtomicBoolean addingToActiveCustomerProduct = new AtomicBoolean(false);
        AtomicReference<Date> dueDate = new AtomicReference<>();

        List<ProductBasketStatus> statuses = new ArrayList<>();
        statuses.add(ProductBasketStatus.PENDING);

        PageRequest pageRequest = PageRequest.of(0, MAX_RECORDS);
        List<LandlordProductBasket> baskets = landlordProductBasketRepository.findByStatuses(statuses, pageRequest);

        if (baskets.isEmpty()) {
            return;
        }

        baskets.forEach(basket -> {
            LandlordCustomer customer = basket.getCustomer();
            PaymentMethodType paymentMethod = getPreferredPaymentMethod(customer);
            if (checkoutService.isBasketEmpty(basket)) {
                landlordProductBasketRepository.delete(basket);
                return;
            }

            try {
                basket.setStatus(ProductBasketStatus.PROCESSING);
                landlordProductBasketRepository.save(basket);
            } catch (Exception e) {
                log.error("Error saving the processing state of Basket -> " + basket.getId());
                return;
            }

            if (paymentMethod == null) {
                log.error("Extending CustomerProduct -> no paymentMethod -> Basket -> " + basket.getId());
                updateBasketStatus(basket, ProductBasketStatus.ERROR);
                return;
            }

            AbstractBillingProvider<LandlordCustomer, ?, ?, ?, ?, ?> paymentProvider = checkoutService
                    .getPaymentProvider(paymentMethod);
            if (paymentProvider == null) {
                log.warn("No Payment Provider for Payment Method: " + paymentMethod);
                updateBasketStatus(basket, ProductBasketStatus.ERROR);
                return;
            }

            if (!paymentProvider.paymentDetailsProvided(customer)) {
                log.error("Extending CustomerProduct -> no paymentDetails -> Basket -> " + basket.getId());
                updateBasketStatus(basket, ProductBasketStatus.ERROR);
                return;
            }

            if (basket.getProducts() == null || basket.getProducts().isEmpty()) {
                LandlordCustomerProduct customerProduct = customer.getActiveProduct();

                if (customerProduct != null) {
                    log.info("FOUND active customer product with ID: " + customerProduct.getId());
                    addingToActiveCustomerProduct.set(true);
                    dueDate.set(customerProduct.getDueDate());
                }
            }

            log.info("VALUE dueDate: " + dueDate.get());
            CheckoutBean checkout = new CheckoutBean(paymentMethod);
            CheckoutResult result = checkoutService.checkout(
                    basket,
                    checkout,
                    addingToActiveCustomerProduct.get(),
                    dueDate.get());
            handleCheckoutResult(result, basket);
        });
    }

    private PaymentMethodType getPreferredPaymentMethod(LandlordCustomer customer) {
        for (PaymentMethod paymentMethod : customer.getPaymentMethods()) {
            if (paymentMethod.getPreferred() && paymentMethod.getMethod() != PaymentMethodType.DEFAULT) {
                return paymentMethod.getMethod();
            }
        }
        return PaymentMethodType.DEFAULT;
    }

    private void handleCheckoutResult(CheckoutResult result, LandlordProductBasket basket) {
        switch (result) {
            case ACCEPTED:
                log.info(
                        "*******************************************************************************\n" +
                                "Successfully charged the customer for new CustomerProduct -> " +
                                "Basket -> " + basket.getId() + "\n" +
                                "********************************************************************************");
                basket.setStatus(ProductBasketStatus.CHARGED);
                landlordProductBasketRepository.save(basket);
                break;

            case PAYMENT_REQUIRED:
                basket.setStatus(ProductBasketStatus.PENDING);
                landlordProductBasketRepository.save(basket);
                break;
            case ERROR:
                log.error(
                        "*******************************************************************************\n" +
                                "Error charging the customer for new CustomerProduct -> " +
                                "Basket -> " + basket.getId() + "\n" +
                                "********************************************************************************");
                basket.setStatus(ProductBasketStatus.ERROR);
                landlordProductBasketRepository.save(basket);
                break;
            default:
                log.error(
                        "*******************************************************************************\n" +
                                "DEFAULT Error charging the customer for new CustomerProduct -> " +
                                "Basket -> " + basket.getId() + "\n" +
                                "********************************************************************************");
                basket.setStatus(ProductBasketStatus.ERROR);
                landlordProductBasketRepository.save(basket);
                break;
        }
    }

    private void updateBasketStatus(LandlordProductBasket basket, ProductBasketStatus status) {
        try {
            basket.setStatus(status);
            landlordProductBasketRepository.save(basket);
        } catch (Exception e) {
            log.error("Error saving the {} state of Basket -> {}", status, basket.getId());
        }
    }

}
