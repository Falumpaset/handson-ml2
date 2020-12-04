package de.immomio.service.landlord;

import de.immomio.billing.landlord.AbstractLandlordCheckoutService;
import de.immomio.constants.CheckoutResult;
import de.immomio.data.base.bean.product.basket.CheckoutBean;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasket;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.model.repository.core.landlord.invoice.BaseLandlordInvoiceRepository;
import de.immomio.model.repository.core.landlord.product.basket.BaseLandlordProductBasketRepository;
import de.immomio.model.repository.core.landlord.product.basket.BaseProductBasketProductAddonRepository;
import de.immomio.model.repository.core.landlord.product.customer.addon.BaseCustomerAddonProductRepository;
import de.immomio.pdf.invoice.InvoicePDFGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class CrawlerLandlordCheckoutService extends AbstractLandlordCheckoutService<
        CrawlerLandlordInvoiceCalculator,
        CrawlerLandlordPlanHandler,
        BaseLandlordInvoiceRepository,
        BaseCustomerAddonProductRepository,
        BaseProductBasketProductAddonRepository,
        BaseLandlordProductBasketRepository,
        CrawlerCacheEvictService
        > {

    private static final String DEFAULT_PAYMENT_METHOD_IS_NOT_SUPPORTED = "DEFAULT payment method is not supported";

    private CrawlerCacheEvictService crawlerCacheEvictService;

    @Autowired
    public CrawlerLandlordCheckoutService(
            CrawlerLandlordPlanHandler planHandler,
            InvoicePDFGenerator invoicePDFGenerator,
            CrawlerLandlordInvoiceCalculator invoiceCalculator,
            LandlordMailSender mailSender,
            BaseCustomerAddonProductRepository customerAddonProductRepository,
            BaseProductBasketProductAddonRepository basketProductAddonRepository,
            CrawlerCacheEvictService crawlerCacheEvictService,
            BaseLandlordProductBasketRepository productBasketRepository
    ) {
        super(
                mailSender,
                planHandler,
                invoicePDFGenerator,
                invoiceCalculator,
                customerAddonProductRepository,
                basketProductAddonRepository,
                productBasketRepository
        );
        this.crawlerCacheEvictService = crawlerCacheEvictService;
    }

    public CheckoutResult checkout(LandlordProductBasket basket, CheckoutBean checkout,
                                   boolean addingToActiveCustomerProduct, Date dueDate) {
        CheckoutResult result;
        switch (checkout.getPaymentMethod()) {
            case INVOICE:
                result = handleInvoicePaymentMethod(basket, checkout, addingToActiveCustomerProduct, dueDate);
                break;
            case STRIPE:
                result = handleStripePaymentMethod(basket, checkout, addingToActiveCustomerProduct, dueDate);
                break;
            default:
                result = handleDefaultPaymentMethod();
        }

        return result;
    }

    private CheckoutResult handleDefaultPaymentMethod() {
        log.error(DEFAULT_PAYMENT_METHOD_IS_NOT_SUPPORTED);
        return CheckoutResult.ERROR;
    }

    private CheckoutResult handleInvoicePaymentMethod(LandlordProductBasket basket, CheckoutBean checkout,
                                                      boolean addingToActiveCustomerProduct, Date dueDate) {
        return checkout(basket, checkout, true, addingToActiveCustomerProduct, dueDate);
    }

    private CheckoutResult handleStripePaymentMethod(LandlordProductBasket basket, CheckoutBean checkout,
                                                     boolean addingToActiveCustomerProduct, Date dueDate) {
        return checkout(basket, checkout, true, addingToActiveCustomerProduct, dueDate);
    }

    @Override
    protected CrawlerCacheEvictService getCacheEvictService() {
        return crawlerCacheEvictService;
    }
}
