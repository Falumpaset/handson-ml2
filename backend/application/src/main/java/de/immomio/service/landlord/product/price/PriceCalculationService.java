package de.immomio.service.landlord.product.price;

import de.immomio.beans.ProductAddonPriceAndTypeBean;
import de.immomio.data.base.entity.AbstractEntity;
import de.immomio.data.base.type.customer.CustomerLocation;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import de.immomio.data.landlord.entity.product.productaddonprice.LandlordProductAddonPrice;
import de.immomio.data.landlord.entity.product.productprice.LandlordProductPrice;
import de.immomio.data.shared.bean.price.PriceBean;
import de.immomio.data.shared.util.price.PriceUtils;
import de.immomio.service.LocationService;
import de.immomio.service.landlord.discount.DiscountCalculationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Maik Kingma
 */

@Service
public class PriceCalculationService {

    private final LocationService locationService;

    private DiscountCalculationService discountCalculationService;

    public PriceCalculationService(LocationService locationService, DiscountCalculationService discountCalculationService) {
        this.locationService = locationService;
        this.discountCalculationService = discountCalculationService;
    }

    public ProductAddonPriceAndTypeBean calcAddonPrice(
            LandlordCustomer customer,
            LandlordProductAddon productAddon
    ) {
        Double addonDiscount = discountCalculationService.getAddonDiscount(customer, productAddon).getValue();
        Double customerDiscount = discountCalculationService.getNewestCustomerDiscount(customer).getValue();
        PriceBean priceBean = calcAddonPrice(productAddon, customer.getLocation(),
                customer.getPriceMultiplier(), customerDiscount, addonDiscount);
        return new ProductAddonPriceAndTypeBean(priceBean, productAddon.getAddonProduct().getAddonType());
    }

    public Map<Long, ProductAddonPriceAndTypeBean> getAddonPrices(
            LandlordCustomer customer,
            LandlordProduct landlordProduct
    ) {
        List<LandlordProductAddon> addons = landlordProduct.getProductAddons();
        return addons.stream()
                .collect(Collectors.toMap(
                        AbstractEntity::getId,
                        addon -> calcAddonPrice(customer, addon)));
    }

    public PriceBean calcPrice(LandlordProduct product, LandlordCustomer customer) {

        LandlordProductPrice app = product.getPrice(customer);

        BigDecimal price = app.getPrice().calculatePrice(customer.getPriceMultiplier());

        return PriceUtils.calculatePrice(product.getSubscriptionPeriod(), price, locationService.getTaxrate());
    }

    public PriceBean calcPrice(LandlordProduct product, CustomerLocation location, Double multiplier) {

        LandlordProductPrice app = product.getPrice(location);

        BigDecimal price = app.getPrice().calculatePrice(multiplier);

        return PriceUtils.calculatePrice(product.getSubscriptionPeriod(), price, locationService.getTaxrate());
    }

    public PriceBean calcAddonPrice(
            LandlordProductAddon addon,
            CustomerLocation location,
            Double multiplier,
            Double customerDiscount,
            Double customerAddonDiscount) {

        LandlordProductAddonPrice app = addon.getPrice(location);

        BigDecimal price = app.getPrice().calculatePrice(multiplier);

        PriceBean result = PriceUtils.calculatePrice(addon.getProduct().getSubscriptionPeriod(), price,
                locationService.getTaxrate(), app.getPrice().getSetup());

        applyCustomerDiscount(result, customerDiscount, customerAddonDiscount);
        return result;
    }

    private void applyCustomerDiscount(PriceBean result, Double customerDiscount, Double customerAddonDiscunt) {
        BigDecimal customerDiscountBd = BigDecimal.valueOf(customerDiscount);
        BigDecimal customerAddonDiscuntBd = BigDecimal.valueOf(customerAddonDiscunt);
        result.setPostDiscountMonthlyPriceNet(applyDiscount(result.getMonthlyPriceNet(), customerDiscountBd, customerAddonDiscuntBd));
        result.setPostDiscountYearlyPriceNet(applyDiscount(result.getYearlyPriceNet(), customerDiscountBd, customerAddonDiscuntBd));
        result.setPostDiscountWeeklyPriceNet(applyDiscount(result.getWeeklyPriceNet(), customerDiscountBd, customerAddonDiscuntBd));
        result.setPostDiscountYearlyPriceGross(applyDiscount(result.getYearlyPriceGross(), customerDiscountBd, customerAddonDiscuntBd));
        result.setPostDiscountWeeklyPriceGross(applyDiscount(result.getWeeklyPriceGross(), customerDiscountBd, customerAddonDiscuntBd));
        result.setPostDiscountMonthlyPriceGross(applyDiscount(result.getMonthlyPriceGross(), customerDiscountBd, customerAddonDiscuntBd));
    }

    private BigDecimal applyDiscount(BigDecimal value, BigDecimal customerDiscount, BigDecimal customerAddonDiscount) {
        return value.multiply(new BigDecimal(1).subtract(customerDiscount)).setScale(2,
                RoundingMode.CEILING).multiply(new BigDecimal(1).subtract(customerAddonDiscount)).setScale(2,
                RoundingMode.CEILING);
    }
}
