package de.immomio.billing.landlord.quota;

import de.immomio.billing.exception.InvoiceGenerationException;
import de.immomio.billing.invoice.AbstractInvoiceCalculator;
import de.immomio.constants.CheckoutResult;
import de.immomio.constants.exceptions.BasketCheckoutException;
import de.immomio.data.base.bean.product.basket.BasketValueBean;
import de.immomio.data.base.bean.product.basket.CheckoutBean;
import de.immomio.data.base.type.customer.PaymentMethodType;
import de.immomio.data.base.type.invoice.InvoiceStatus;
import de.immomio.data.base.type.product.basket.ProductBasketStatus;
import de.immomio.data.base.type.product.quota.QuotaProductType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.invoice.LandlordInvoice;
import de.immomio.data.landlord.entity.product.basket.quota.LandlordQuotaBasket;
import de.immomio.data.landlord.entity.product.quota.LandlordQuota;
import de.immomio.data.landlord.entity.product.quota.LandlordQuotaCustomerProduct;
import de.immomio.model.abstractrepository.invoice.BaseAbstractInvoiceRepository;
import de.immomio.model.abstractrepository.product.quota.BaseAbstractQuotaBasketRepository;
import de.immomio.model.abstractrepository.product.quota.BaseAbstractQuotaCustomerProductRepository;
import de.immomio.model.abstractrepository.product.quota.BaseAbstractQuotaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @author Niklas Lindemann
 */
@Slf4j
public abstract class AbstractLandlordQuotaCheckoutService
        <QR extends BaseAbstractQuotaRepository<LandlordQuota>,
                QCPR extends BaseAbstractQuotaCustomerProductRepository<LandlordQuotaCustomerProduct, LandlordCustomer>,
                IC extends AbstractInvoiceCalculator,
                QBR extends BaseAbstractQuotaBasketRepository<LandlordQuotaBasket, LandlordCustomer>,
                IR extends BaseAbstractInvoiceRepository> {

    private QR quotaRepository;

    private QCPR quotaCustomerProductRepository;

    private final IC invoiceCalculator;

    private final QBR quotaBasketRepository;

    private final IR invoiceRepository;

    private static final String BASKET_CHECKOUT = "basketCheckout";
    private static final String BASKET_IS_EMPTY_ERROR = "BASKET_IS_EMPTY_L";
    private static final String BASKET_ALREADY_CHARGED_ERROR = "BASKET_IS_ALREADY_CHARGED_L";
    private static final String NO_BASKET_FOUND = "errors.basketCheckout.noBasket";

    public AbstractLandlordQuotaCheckoutService(
            QR quotaRepository,
            QCPR quotaCustomerProductRepository,
            IC invoiceCalculator,
            QBR quotaBasketRepository,
            IR invoiceRepository
    ) {
        this.quotaRepository = quotaRepository;
        this.quotaCustomerProductRepository = quotaCustomerProductRepository;
        this.invoiceCalculator = invoiceCalculator;
        this.quotaBasketRepository = quotaBasketRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public BasketValueBean basketValue(LandlordQuotaBasket basket, PaymentMethodType paymentMethod) throws InvoiceGenerationException {
        LandlordInvoice invoice = new LandlordInvoice();

        invoiceCalculator.calculateQuotaPackageInvoice(
                invoice,
                basket,
                false,
                paymentMethod, true);

        BasketValueBean basketValue = new BasketValueBean();
        basketValue.setPriceGross(invoice.getGrossPrice());
        basketValue.setPostDiscountPriceGross(invoice.getPostDiscountGrossPrice());
        basketValue.setPriceNet(invoice.getPrice());
        basketValue.setPostDiscountPriceNet(invoice.getPostDiscountPrice());
        basketValue.setCurrency(invoice.getCurrency());

        return basketValue;
    }

    public CheckoutResult processCheckout(Long id, CheckoutBean checkout) throws BasketCheckoutException {
        Optional<LandlordQuotaBasket> basketOpt = quotaBasketRepository.findById(id);
        if (basketOpt.isPresent()) {
            LandlordQuotaBasket basket = basketOpt.get();
            try {
                validateBasket(basket);
                setProcessing(basket);
                activateQuotaPackages(basket);

                LandlordInvoice invoice = new LandlordInvoice();
                invoiceCalculator.calculateQuotaPackageInvoice(invoice, basket, true, checkout.getPaymentMethod(), false);
                setCharged(basket, invoice);

            } catch (RuntimeException e) {
                basket.setStatus(ProductBasketStatus.ERROR);
                quotaBasketRepository.save(basket);

                return CheckoutResult.ERROR;
            }
        }

        return CheckoutResult.ACCEPTED;

    }

    private void setCharged(LandlordQuotaBasket basket, LandlordInvoice invoice) {
        invoice.setStatus(InvoiceStatus.CHARGED);
        invoiceRepository.save(invoice);

        basket.setStatus(ProductBasketStatus.CHARGED);
        quotaBasketRepository.save(basket);
    }

    private void validateBasket(LandlordQuotaBasket basket) throws BasketCheckoutException {
        if (basket == null) {
            throw new BasketCheckoutException(new ObjectError(BASKET_CHECKOUT, NO_BASKET_FOUND), HttpStatus.BAD_REQUEST);
        }

        if (basket.getQuotaPackages() == null || basket.getQuotaPackages().isEmpty()) {
            throw new BasketCheckoutException(new ObjectError(BASKET_CHECKOUT, BASKET_IS_EMPTY_ERROR), HttpStatus.BAD_REQUEST);
        }

        if (ProductBasketStatus.CHARGED.equals(basket.getStatus())) {
            throw new BasketCheckoutException(new ObjectError(BASKET_CHECKOUT, BASKET_ALREADY_CHARGED_ERROR), HttpStatus.BAD_REQUEST);
        }
    }

    private void setProcessing(LandlordQuotaBasket basket) {
        if (!ProductBasketStatus.PROCESSING.equals(basket.getStatus())) {
            basket.setStatus(ProductBasketStatus.PROCESSING);

            quotaBasketRepository.save(basket);
        }
    }

    private void activateQuotaPackages(LandlordQuotaBasket basket) {
        basket.getQuotaPackages().forEach(basketQuotaPackage -> {
            QuotaProductType quotaProductType = basketQuotaPackage.getQuotaPackage().getType();
            LandlordQuotaCustomerProduct landlordQuotaCustomerProduct = getLandlordQuotaCustomerProduct(basket.getCustomer(), quotaProductType);
            IntStream.range(0, basketQuotaPackage.getQuantity()).forEach(i -> {
                LandlordQuota landlordQuota = new LandlordQuota();
                landlordQuota.setQuantity(basketQuotaPackage.getQuotaPackage().getQuantity());
                landlordQuota.setQuotaCustomerProduct(landlordQuotaCustomerProduct);
                quotaRepository.save(landlordQuota);
            });
        });
    }

    private LandlordQuotaCustomerProduct getLandlordQuotaCustomerProduct(LandlordCustomer customer, QuotaProductType quotaProductType) {
        Optional<LandlordQuotaCustomerProduct> quotaCustomerProduct = quotaCustomerProductRepository.findFirstFromCustomerByType(customer, quotaProductType);

        return quotaCustomerProduct.orElseGet(() -> {
            LandlordQuotaCustomerProduct newQuotaCustomerProduct = new LandlordQuotaCustomerProduct();
            newQuotaCustomerProduct.setCustomer(customer);
            newQuotaCustomerProduct.setType(quotaProductType);

            return quotaCustomerProductRepository.save(newQuotaCustomerProduct);
        });
    }
}
