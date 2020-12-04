package de.immomio.billing.landlord;

import com.stripe.model.Discount;
import de.immomio.billing.exception.InvoiceGenerationException;
import de.immomio.billing.invoice.AbstractInvoiceCalculator;
import de.immomio.billing.product.AbstractCheckoutService;
import de.immomio.billing.product.BasketActivationException;
import de.immomio.caching.service.AbstractCacheEvictService;
import de.immomio.constants.CheckoutResult;
import de.immomio.constants.exceptions.BasketCheckoutException;
import de.immomio.data.base.bean.product.basket.CheckoutBean;
import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.base.type.customer.PaymentMethodType;
import de.immomio.data.base.type.product.basket.ProductBasketStatus;
import de.immomio.data.landlord.bean.customer.LandlordCustomerBean;
import de.immomio.data.landlord.bean.invoice.LandlordInvoiceBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.invoice.LandlordInvoice;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.product.addonproduct.LandlordCustomerAddonProduct;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasket;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketProductAddon;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.abstractrepository.invoice.BaseAbstractInvoiceRepository;
import de.immomio.model.abstractrepository.product.BaseAbstractCustomerAddonProductRepository;
import de.immomio.model.abstractrepository.product.BaseAbstractProductBasketProductAddonRepository;
import de.immomio.model.abstractrepository.product.BaseAbstractProductBasketRepository;
import de.immomio.pdf.invoice.InvoicePDFGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author Maik Kingma,
 */

@Slf4j
public abstract class AbstractLandlordCheckoutService<
        IC extends AbstractInvoiceCalculator,
        PH extends AbstractLandlordPlanHandler,
        AIR extends BaseAbstractInvoiceRepository<LandlordInvoice>,
        APR extends BaseAbstractCustomerAddonProductRepository<LandlordCustomerAddonProduct>,
        PAR extends BaseAbstractProductBasketProductAddonRepository<LandlordProductBasketProductAddon>,
        PBR extends BaseAbstractProductBasketRepository<LandlordProductBasket>,
        CES extends AbstractCacheEvictService<LandlordCustomer>
        > extends AbstractCheckoutService<
        LandlordCustomer,
        LandlordProductBasket,
        LandlordInvoice,
        LandlordCustomerProduct,
        AIR
        > {


    private static final String BASKET_CHECKOUT = "basketCheckout";
    private static final String BASKET_IS_EMPTY_ERROR = "BASKET_IS_EMPTY_L";
    private static final String BASKET_ALREADY_CHARGED_ERROR = "BASKET_IS_ALREADY_CHARGED_L";
    private static final String NO_BASKET_FOUND = "errors.basketCheckout.noBasket";


    private static final String INVOICE_BOOKING_SUBJECT_KEY = "invoice.booking.subject";

    protected final LandlordMailSender mailSender;

    private final PH planHandler;

    private final InvoicePDFGenerator invoicePDFGenerator;

    private final IC invoiceCalculator;

    private final APR addonProductRepository;

    private final PAR basketProductAddonRepository;

    private final PBR productBasketRepository;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private Environment environment;

    private List<String> NEW_INVOICE_GENERATED_EMAILS = Arrays.asList(
            "appinvoices@immomio.de");

    public AbstractLandlordCheckoutService(
            LandlordMailSender mailSender,
            PH planHandler,
            InvoicePDFGenerator invoicePDFGenerator,
            IC invoiceCalculator,
            APR addonProductRepository,
            PAR basketProductAddonRepository,
            PBR productBasketRepository
    ) {
        this.mailSender = mailSender;
        this.planHandler = planHandler;
        this.invoicePDFGenerator = invoicePDFGenerator;
        this.invoiceCalculator = invoiceCalculator;
        this.addonProductRepository = addonProductRepository;
        this.basketProductAddonRepository = basketProductAddonRepository;
        this.productBasketRepository = productBasketRepository;
    }

    @Override
    protected void sentNotification(LandlordProductBasket basket, LandlordInvoice invoice, boolean attachInvoice) {
        Map<String, Object> bookingDetails = new HashMap<>();
        bookingDetails.put(ModelParams.MODEL_INVOICE, new LandlordInvoiceBean(invoice));
        bookingDetails.put(ModelParams.MODEL_IGNORE_DISCLAIMER, true);
        bookingDetails.put(ModelParams.MODEL_CUSTOMER, new LandlordCustomerBean(invoice.getCustomer(), invoice.getCustomer().getName()));

        Map<String, String> attachments = new HashMap<>();
        if (attachInvoice) {
            File file = invoicePDFGenerator.createDocument(invoice, false);
            attachments.put(file.getName(), file.getPath());
        }

        mailSender.send(basket.getCustomer(), MailTemplate.INVOICE_BOOKING, INVOICE_BOOKING_SUBJECT_KEY, bookingDetails,
                attachments);


        if(Arrays.asList(environment.getActiveProfiles()).contains("production")) {
            NEW_INVOICE_GENERATED_EMAILS.forEach(mailAddress -> {
                mailSender.send(mailAddress, MailTemplate.INTERNAL_INVOICE_HAS_BEEN_GENERATED, null, bookingDetails,
                    attachments,true);
            });
        }
    }

    public CheckoutResult processCheckout(LandlordProductBasket basket, CheckoutBean checkout) throws BasketCheckoutException {
        boolean addingToActiveCustomerProduct = false;
        Date dueDate = null;

        validateBasket(basket);

        LandlordCustomer customer = basket.getCustomer();
        setProcessing(basket);

        if (basket.getProducts() == null || basket.getProducts().isEmpty()) {
            LandlordCustomerProduct customerProduct = customer.getActiveProduct();

            if (customerProduct != null) {
                addingToActiveCustomerProduct = true;
                dueDate = customerProduct.getDueDate();
            }
        }

        CheckoutResult checkoutResult = checkout(basket, checkout, true,
                addingToActiveCustomerProduct, dueDate);

        return handleCheckoutResult(basket, checkoutResult);

    }

    private void setProcessing(LandlordProductBasket basket) {
        if (!ProductBasketStatus.PROCESSING.equals(basket.getStatus())) {
            basket.setStatus(ProductBasketStatus.PROCESSING);
            productBasketRepository.save(basket);
        }
    }

    private CheckoutResult handleCheckoutResult(LandlordProductBasket basket, CheckoutResult checkoutResult) throws BasketCheckoutException {
        switch (checkoutResult) {
            case ACCEPTED: {
                basket.setStatus(ProductBasketStatus.CHARGED);
                productBasketRepository.save(basket);
                executeCacheEvict(basket);
                return checkoutResult;
            }
            case PAYMENT_REQUIRED: {
                basket.setStatus(ProductBasketStatus.PENDING);
                productBasketRepository.save(basket);
                executeCacheEvict(basket);

                throw new BasketCheckoutException(new ObjectError(BASKET_CHECKOUT, "errors.basketCheckout"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            case ERROR: {
                basket.setStatus(ProductBasketStatus.ERROR);
                productBasketRepository.save(basket);
                throw new BasketCheckoutException(new ObjectError(BASKET_CHECKOUT, "errors.basketCheckout"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            default: {
                basket.setStatus(ProductBasketStatus.ERROR);
                productBasketRepository.save(basket);
                throw new BasketCheckoutException(new ObjectError(BASKET_CHECKOUT, "errors.basketCheckout.unknownCheckoutResult"), HttpStatus.BAD_REQUEST);
            }
        }
    }

    private void validateBasket(LandlordProductBasket basket) throws BasketCheckoutException {
        if (basket == null) {
            throw new BasketCheckoutException(new ObjectError(BASKET_CHECKOUT, NO_BASKET_FOUND), HttpStatus.BAD_REQUEST);
        }

        if (isBasketEmpty(basket)) {
            throw new BasketCheckoutException(new ObjectError(BASKET_CHECKOUT, BASKET_IS_EMPTY_ERROR), HttpStatus.BAD_REQUEST);
        }

        if (ProductBasketStatus.CHARGED.equals(basket.getStatus())) {
            throw new BasketCheckoutException(new ObjectError(BASKET_CHECKOUT, BASKET_ALREADY_CHARGED_ERROR), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    protected LandlordInvoice calculateInvoice(LandlordProductBasket basket, List<Discount> discounts,
                                               boolean generateId,
                                               PaymentMethodType paymentMethod, boolean trial,
                                               boolean basketValueCalculation, Double percentage)
            throws InvoiceGenerationException {
        LandlordInvoice invoice = new LandlordInvoice();
        invoiceCalculator.calculateInvoice(basket, true, paymentMethod, invoice, trial, basketValueCalculation,
                percentage);
        return invoice;
    }

    @Override
    protected LandlordCustomerProduct activateBasket(LandlordProductBasket basket) throws BasketActivationException {
        LandlordProduct product;
        if (basket.getProducts() == null || basket.getProducts().isEmpty()) {
            product = null;
        } else if (basket.getProducts().size() > 1) {
            throw new BasketActivationException("ERROR activating the Products inside the basket - more than one main" +
                    " product added");
        } else if (basket.getProducts().get(0).getQuantity() > 1) {
            log.error("ERROR activating the Products inside the basket - the quantity of the one main product is " +
                    "higher than 1");
            throw new BasketActivationException("ERROR activating the Products inside the basket" +
                    " - the quantity of the one main product is higher than 1");
        } else {
            product = basket.getProducts().get(0).getProduct();
        }

        LandlordCustomerProduct cProduct = getCustomerProduct(basket, product);
        activateProductAddons(basket, cProduct);

        return cProduct;
    }

    private LandlordCustomerProduct getCustomerProduct(LandlordProductBasket basket, LandlordProduct product) throws BasketActivationException {
        LandlordCustomerProduct customerProduct;
        if (product != null) {
            try {
                customerProduct = planHandler.activateProduct(
                        basket.getCustomer(),
                        product,
                        new Date(),
                        null,
                        true,
                        getTrial(basket));
            } catch (Exception e) {
                throw new BasketActivationException("ERROR activating the Product inside the basket" +
                        " - " + e.getMessage(), e);
            }
        } else {
            customerProduct = basket.getCustomer().getActiveProduct();
        }

        if (customerProduct == null) {
            throw new BasketActivationException("ERROR activating the Basket" +
                    " - no active customer product found for customer " + basket.getCustomer().getId());
        }
        return customerProduct;
    }

    private void activateProductAddons(LandlordProductBasket basket, LandlordCustomerProduct customerProduct) {
        for (LandlordProductBasketProductAddon addon : basket.getProductAddons()) {
            int quantity = addon.getQuantity() == null ? 1 : addon.getQuantity();

            for (int i = 0; i < quantity; i++) {
                LandlordCustomerAddonProduct cAddon;

                try {
                    cAddon = planHandler.addAddon(customerProduct, addon.getProductAddon());
                } catch (Exception e) {
                    log.error("Exception -> activating productaddon ...", e);
                    continue;
                }

                if (cAddon == null) {
                    log.error("Addon is NULL");
                }
            }
        }
    }

    @Override
    public void renewExpiringProducts(LandlordProductBasket basket) {
        LandlordCustomer customer = basket.getCustomer();
        LandlordCustomerProduct activeProduct = customer.getActiveProduct();
        if (activeProduct == null) {
            return;
        }
        Set<LandlordCustomerAddonProduct> addons = activeProduct.getAddons();

        // value = count of addons to remove
        Map<AddonType, Integer> addonsToRemove = new HashMap<>();

        basket.getProductAddons()
                .forEach(addon -> {
                    IntStream.range(0, addon.getQuantity()).forEach(i -> {
                        addons.stream()
                                .filter(customerAddon -> isExpiringAddon(addon, customerAddon))
                                .findFirst()
                                .ifPresent(customerAddon -> {
                                    populateAddonsToRemove(addonsToRemove, addon);
                                    renewAddon(customerAddon);
                                });
                    });
                });
        removeProductAddons(addonsToRemove, basket);
    }

    private void populateAddonsToRemove(
            Map<AddonType, Integer> addonsToRemove,
            LandlordProductBasketProductAddon addon
    ) {
        AddonType addonType = addon.getProductAddon().getAddonProduct().getAddonType();
        addonsToRemove.compute(addonType, (type, quantity) -> {
            if (quantity == null) {
                return 1;
            } else {
                return quantity + 1;
            }
        });
    }

    public void renewAddon(LandlordCustomerAddonProduct customerAddon) {
        customerAddon.setRenew(true);
        addonProductRepository.save(customerAddon);
    }

    @Override
    protected void unsetRenewForRemovedAddons(LandlordProductBasket basket) {
        LandlordCustomerProduct activeProduct = basket.getCustomer().getActiveProduct();
        basket.getProductAddonsToRemove().forEach(addonToRemove -> {
            IntStream.range(0, addonToRemove.getQuantity()).forEach(i -> {
                List<LandlordCustomerAddonProduct> existingAddons = addonProductRepository.findFirstByCustomerProductRenew(activeProduct.getId(), addonToRemove.getProductAddon().getId(), PageRequest.of(0, 1));
                if (!existingAddons.isEmpty()) {
                    LandlordCustomerAddonProduct existingAddon = existingAddons.get(0);
                    existingAddon.setRenew(false);
                    addonProductRepository.save(existingAddon);
                }
            });
        });
    }

    private void removeProductAddons(Map<AddonType, Integer> toRemove, LandlordProductBasket basket) {
        List<LandlordProductBasketProductAddon> productAddons = basket.getProductAddons();
        toRemove.forEach((addonType, quantityExpiringAddons) -> {
            productAddons.stream()
                    .filter(filterAddon -> filterAddon
                            .getProductAddon()
                            .getAddonProduct()
                            .getAddonType()
                            .equals(addonType))
                    .findFirst()
                    .ifPresent(productAddon -> {
                        if (quantityExpiringAddons >= productAddon.getQuantity()) {
                            productAddons.remove(productAddon);
                            basketProductAddonRepository.delete(productAddon);
                        } else {
                            productAddon.setQuantity(productAddon.getQuantity() - quantityExpiringAddons);
                        }
                    });
        });
    }

    private boolean isExpiringAddon(
            LandlordProductBasketProductAddon addon,
            LandlordCustomerAddonProduct customerAddon
    ) {
        AddonType customerAddonType = customerAddon.getAddonProduct().getAddonType();
        AddonType basketAddonType = addon.getProductAddon().getAddonProduct().getAddonType();
        return !customerAddon.isRenew() && customerAddonType.equals(basketAddonType);
    }

    @Override
    protected void executeCacheEvict(LandlordProductBasket basket) {
        getCacheEvictService().evictCustomerCache(basket.getCustomer());
    }



    protected abstract CES getCacheEvictService();
}
