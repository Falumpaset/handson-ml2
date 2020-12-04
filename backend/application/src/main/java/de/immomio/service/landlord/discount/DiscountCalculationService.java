package de.immomio.service.landlord.discount;

import de.immomio.data.base.entity.discount.AbstractDiscount;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.discount.LandlordDiscount;
import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import de.immomio.data.shared.bean.discount.LandlordAddonDiscountBean;
import de.immomio.data.shared.bean.discount.LandlordDiscountBean;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Service
public class DiscountCalculationService {

    public LandlordDiscountBean getNewestCustomerDiscount(LandlordCustomer customer) {
        List<LandlordDiscount> filteredDiscounts = filterCurrentDiscounts(customer);
        return getNewestCustomerDiscount(filteredDiscounts);
    }

    public LandlordAddonDiscountBean getAddonDiscount(LandlordCustomer customer, LandlordProductAddon productAddon) {
        return customer
                .getAddonProductDiscounts()
                .stream()
                .filter(addonProductDiscount -> addonProductDiscount.getProductAddon().equals(productAddon) &&
                        discountIsValid(addonProductDiscount.getDiscount()))
                .map(discount -> new LandlordAddonDiscountBean(discount.getDiscount().getValue(), discount.getDiscount().getEndDate(), productAddon.getAddonProduct().getId()))
                .findFirst()
                .orElse(new LandlordAddonDiscountBean());
    }

    private LandlordDiscountBean getNewestCustomerDiscount(List<LandlordDiscount> filteredDiscounts) {
        LandlordDiscountBean discount = filteredDiscounts.stream()
                .max(Comparator.comparing(AbstractDiscount::getStartDate))
                .map(landlordDiscount -> new LandlordDiscountBean(landlordDiscount.getValue(), landlordDiscount.getEndDate()))
                .orElse(new LandlordDiscountBean());
        return discount;
    }

    private List<LandlordDiscount> filterCurrentDiscounts(LandlordCustomer customer) {
        return customer.getDiscounts()
                .stream()
                .filter(discount -> discount.getEndDate().after(new Date()))
                .collect(Collectors.toList());
    }

    private boolean discountIsValid(LandlordDiscount discount) {
        LocalDateTime now = LocalDateTime.now();

        return now.isAfter(LocalDateTime.fromDateFields(discount.getStartDate()))
                && now.isBefore(LocalDateTime.fromDateFields(discount.getEndDate()));
    }
}
