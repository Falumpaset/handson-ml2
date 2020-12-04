package de.immomio.service.landlord.product;

import de.immomio.data.landlord.bean.product.LandlordProductAddonBean;
import de.immomio.data.landlord.bean.product.LandlordProductOverviewBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import de.immomio.service.landlord.discount.DiscountCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */
@Service
public class LandlordProductOverviewCollector {

    private LandlordProductDataConverter productDataConverter;

    private DiscountCalculationService discountCalculationService;

    @Autowired
    public LandlordProductOverviewCollector(LandlordProductDataConverter productDataConverter,
            DiscountCalculationService discountCalculationService) {
        this.productDataConverter = productDataConverter;
        this.discountCalculationService = discountCalculationService;
    }

    public LandlordProductOverviewBean getProductOverview(LandlordCustomer customer) {
        LandlordProductOverviewBean.LandlordProductOverviewBeanBuilder beanBuilder = LandlordProductOverviewBean.builder();
        LandlordCustomerProduct activeProduct = customer.getActiveProduct();
        beanBuilder.dueDate(activeProduct.getDueDate());
        beanBuilder.product(productDataConverter.convertProductToProductBean(activeProduct.getProduct()));
        beanBuilder.discount(discountCalculationService.getNewestCustomerDiscount(customer));
        beanBuilder.trial(activeProduct.getTrial());
        if (activeProduct.getNextProduct() != null) {
            beanBuilder.nextProduct(productDataConverter.convertProductToProductBean(activeProduct.getNextProduct()));
        }

        Set<LandlordProductAddonBean> addonBeans = activeProduct.getAddons().stream()
                .map(landlordCustomerAddonProduct -> productDataConverter.convertCustomerAddonProductToAddonBean(landlordCustomerAddonProduct))
                .collect(Collectors.toSet());

        activeProduct.getProduct().getProductAddons().stream()
                .filter(productAddon -> addonNotAddedToSet(addonBeans, productAddon))
                .map(landlordCustomerAddonProduct -> productDataConverter.convertAddonToProductAddonBean(landlordCustomerAddonProduct, activeProduct))
                .forEach(addonBeans::add);

        beanBuilder.addons(addonBeans);

        return beanBuilder.build();

    }

    private boolean addonNotAddedToSet(Set<LandlordProductAddonBean> addonBeans, LandlordProductAddon productAddon) {
        return addonBeans.stream()
                .filter(landlordProductAddonBean -> landlordProductAddonBean.getId().equals(productAddon.getId()))
                .findFirst()
                .isEmpty();
    }

}
