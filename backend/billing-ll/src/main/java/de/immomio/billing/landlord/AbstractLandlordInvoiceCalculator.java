package de.immomio.billing.landlord;

import de.immomio.billing.invoice.AbstractDiscountHandler;
import de.immomio.billing.invoice.AbstractInvoiceCalculator;
import de.immomio.data.base.bean.invoice.DiscountDetails;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.discount.LandlordCustomerProductAddonDiscount;
import de.immomio.data.landlord.entity.discount.LandlordCustomerQuotaPackageDiscount;
import de.immomio.data.landlord.entity.discount.LandlordDiscount;
import de.immomio.data.landlord.entity.invoice.LandlordInvoice;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasket;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketProduct;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketProductAddon;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketQuotaPackage;
import de.immomio.data.landlord.entity.product.basket.quota.LandlordQuotaBasket;
import de.immomio.model.abstractrepository.invoice.BaseAbstractInvoiceRepository;
import de.immomio.model.repository.core.landlord.discount.BaseLandlordCustomerQuotaPackageDiscountRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AbstractLandlordInvoiceCalculator<DH extends AbstractDiscountHandler,
        AIR extends BaseAbstractInvoiceRepository<LandlordInvoice>,
        QPDR extends BaseLandlordCustomerQuotaPackageDiscountRepository> extends AbstractInvoiceCalculator<
        LandlordProductBasket, LandlordCustomer, LandlordProductBasketProduct, LandlordProductBasketProductAddon,
        LandlordDiscount, LandlordInvoice, AIR, LandlordCustomerProductAddonDiscount, LandlordQuotaBasket, LandlordProductBasketQuotaPackage> {

    @Autowired
    DH landlordDiscountHandler;

    @Autowired
    QPDR quotaPackageDiscountRepository;

    @Override
    public DiscountDetails applyCustomerProductAddonDiscounts(
            DiscountDetails discountDetails,
            LandlordCustomerProductAddonDiscount productAddonDiscount
    ) {
        LandlordDiscount discount = productAddonDiscount.getDiscount();
        if (discountIsValid(discount)) {
            discountDetails.setCustomerProductAddonDiscount(discount.getValue());
        }

        return discountDetails;
    }

    @Override
    public DiscountDetails calculateDiscount(LandlordProductBasketProduct product) {
        List<LandlordDiscount> discounts = landlordDiscountHandler.findDiscountsByProduct(product.getProduct());

        return createDiscountDetails(discounts);
    }

    @Override
    public DiscountDetails calculateDiscount(LandlordProductBasketProductAddon addon) {
        List<LandlordDiscount> discounts = landlordDiscountHandler.findDiscountsByProductAddon(addon.getProductAddon());

        List<LandlordCustomerProductAddonDiscount> addonProductDiscounts =
                addon.getProductBasket()
                        .getCustomer().getAddonProductDiscounts();

        DiscountDetails discountDetails = createDiscountDetails(discounts);
        addonProductDiscounts.stream()
                .filter(item -> item.getProductAddon().equals(addon.getProductAddon()))
                .findFirst()
                .ifPresent(item -> applyCustomerProductAddonDiscounts(discountDetails, item));

        return discountDetails;
    }

    @Override
    public DiscountDetails calculateDiscount(LandlordCustomer customer) {
        List<LandlordDiscount> discounts = landlordDiscountHandler.findDiscountsByCustomer(customer);

        return createDiscountDetails(discounts);
    }


    @Override
    public DiscountDetails calculateDiscount(LandlordProductBasketQuotaPackage quotaPackage) {
        LandlordCustomer customer = quotaPackage.getQuotaBasket().getCustomer();
        Optional<LandlordCustomerQuotaPackageDiscount> quotaDiscount = quotaPackageDiscountRepository
                .findFirstByCustomerAndQuotaPackage(customer, quotaPackage.getQuotaPackage());

        return quotaDiscount.map(landlordCustomerQuotaPackageDiscount ->
                createDiscountDetails(Collections.singletonList(landlordCustomerQuotaPackageDiscount.getDiscount())))
                .orElse(null);
    }

    private boolean discountIsValid(LandlordDiscount discount) {
        LocalDateTime now = LocalDateTime.now();

        return now.isAfter(LocalDateTime.fromDateFields(discount.getStartDate()))
                && now.isBefore(LocalDateTime.fromDateFields(discount.getEndDate()));
    }
}
