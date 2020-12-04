package de.immomio.service.landlord.product;

import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.landlord.bean.product.LandlordProductAddonBean;
import de.immomio.data.landlord.bean.product.LandlordProductBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.product.addon.LandlordAddonProduct;
import de.immomio.data.landlord.entity.product.addonproduct.LandlordCustomerAddonProduct;
import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import de.immomio.data.shared.bean.price.PriceBean;
import de.immomio.service.landlord.discount.DiscountCalculationService;
import de.immomio.service.landlord.product.price.PriceCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */
@Service
public class LandlordProductDataConverter {

    private final PriceCalculationService priceCalculationService;

    private final DiscountCalculationService discountCalculationService;

    @Autowired
    public LandlordProductDataConverter(PriceCalculationService priceCalculationService,
            DiscountCalculationService discountCalculationService) {
        this.priceCalculationService = priceCalculationService;
        this.discountCalculationService = discountCalculationService;
    }

    public LandlordProductBean convertProductToProductBean(LandlordProduct product) {
        return LandlordProductBean.builder()
                .id(product.getId())
                .subscriptionPeriod(product.getSubscriptionPeriod())
                .name(product.getName())
                .description(product.getDescription())
                .build();
    }

    public LandlordProductAddonBean convertCustomerAddonProductToAddonBean(LandlordCustomerAddonProduct addon) {
        LandlordAddonProduct addonProduct = addon.getAddonProduct();
        LandlordProductAddon productAddon = getProductAddon(addonProduct, addon.getCustomerProduct());
        return LandlordProductAddonBean.builder()
                .description(addonProduct.getDescription())
                .name(addonProduct.getName())
                .id(addonProduct.getId())
                .booked(true)
                .renew(addon.isRenew())
                .type(addonProduct.getAddonType())
                .price(calculateAddonPrice(addon.getCustomerProduct().getCustomer(), productAddon))
                .amount(getAddonAmount(addon.getCustomerProduct(), addon.getAddonProduct().getAddonType()))
                .discount(discountCalculationService.getAddonDiscount(addon.getCustomerProduct().getCustomer(), productAddon))
                .build();
    }

    public LandlordProductAddonBean convertAddonToProductAddonBean(LandlordProductAddon productAddon, LandlordCustomerProduct customerProduct) {
        LandlordAddonProduct addonProduct = productAddon.getAddonProduct();
        return LandlordProductAddonBean.builder()
                .description(addonProduct.getDescription())
                .name(addonProduct.getName())
                .id(addonProduct.getId())
                .booked(false)
                .renew(false)
                .type(addonProduct.getAddonType())
                .price(calculateAddonPrice(customerProduct.getCustomer(), getProductAddon(addonProduct, customerProduct)))
                .amount(0L)
                .discount(discountCalculationService.getAddonDiscount(customerProduct.getCustomer(), productAddon))
                .build();
    }

    public LandlordProductAddonBean convertAddonToProductAddonBean(LandlordProductAddon productAddon) {
        LandlordAddonProduct addonProduct = productAddon.getAddonProduct();
        return LandlordProductAddonBean.builder()
                .description(addonProduct.getDescription())
                .name(addonProduct.getName())
                .id(addonProduct.getId())
                .booked(false)
                .renew(false)
                .type(addonProduct.getAddonType())
                .amount(0L)
                .build();
    }

    public LandlordProductAddonBean convertAddonToProductAddonBean(LandlordProductAddon productAddon, LandlordCustomer customer) {
        LandlordProductAddonBean productAddonBean = convertAddonToProductAddonBean(productAddon);
        productAddonBean.setPrice(calculateAddonPrice(customer, productAddon));
        return productAddonBean;
    }

    public LandlordProductAddonBean convertAddonToProductAddonBean(LandlordProductAddon productAddon, PriceBean priceBean) {
        LandlordProductAddonBean landlordProductAddonBean = convertAddonToProductAddonBean(productAddon);
        landlordProductAddonBean.setPrice(priceBean);
        return landlordProductAddonBean;
    }

    private Long getAddonAmount(LandlordCustomerProduct customerProduct, AddonType addonType) {
        return customerProduct.getAddons().stream()
                .map(landlordCustomerAddonProduct -> landlordCustomerAddonProduct.getAddonProduct().getAddonType())
                .filter(addonType::equals)
                .count();
    }

    private LandlordProductAddon getProductAddon(LandlordAddonProduct addonProduct, LandlordCustomerProduct customerProduct) {
        LandlordProductAddon customerAddonProduct = customerProduct.getProduct().getProductAddons()
                .stream()
                .filter(landlordCustomerAddonProduct -> landlordCustomerAddonProduct.getAddonProduct().equals(addonProduct))
                .findFirst()
                .orElseThrow();

        return getProductAddon(customerAddonProduct, addonProduct);
    }

    private LandlordProductAddon getProductAddon(LandlordProductAddon addon, LandlordAddonProduct addonProduct) {
        return addon
                .getProduct()
                .getProductAddons()
                .stream()
                .filter(productAddon -> productAddon.getAddonProduct().equals(addonProduct))
                .findFirst()
                .orElseThrow();
    }

    private PriceBean calculateAddonPrice(LandlordCustomer customer, LandlordProductAddon productAddon) {
        return priceCalculationService.calcAddonPrice(customer, productAddon).getPriceBean();
    }
}
