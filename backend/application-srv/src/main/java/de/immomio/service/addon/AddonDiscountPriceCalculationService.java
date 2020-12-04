package de.immomio.service.addon;

import de.immomio.data.landlord.bean.product.LandlordProductAddonBean;
import de.immomio.data.landlord.bean.product.LandlordProductAddonDiscountOverviewBean;
import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import de.immomio.data.shared.bean.price.PriceBean;
import de.immomio.model.repository.service.landlord.product.productaddon.LandlordProductAddonRepository;
import de.immomio.service.landlord.product.LandlordProductDataConverter;
import de.immomio.service.landlord.product.price.PriceCalculationService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */
@Service
public class AddonDiscountPriceCalculationService {
    private final LandlordProductDataConverter productDataConverter;
    private final PriceCalculationService priceCalculationService;
    private final LandlordProductAddonRepository landlordProductAddonRepository;

    public AddonDiscountPriceCalculationService(PriceCalculationService priceCalculationService,
            LandlordProductAddonRepository landlordProductAddonRepository,
            LandlordProductDataConverter productDataConverter) {
        this.priceCalculationService = priceCalculationService;
        this.landlordProductAddonRepository = landlordProductAddonRepository;
        this.productDataConverter = productDataConverter;
    }

    public Set<LandlordProductAddonBean> getAllAddonsWithDiscount(LandlordProductAddonDiscountOverviewBean overviewBean) {
        return overviewBean.getAddonDiscounts().stream().map(addonDiscountBean -> {
            LandlordProductAddon productAddon = landlordProductAddonRepository.findById(addonDiscountBean.getAddonId()).orElseThrow();
            PriceBean priceBean = priceCalculationService.calcAddonPrice(productAddon, overviewBean.getCustomerLocation(), overviewBean.getMultiplier(),
                    overviewBean.getCustomerDiscount(),
                    addonDiscountBean.getValue());
            return productDataConverter.convertAddonToProductAddonBean(productAddon, priceBean);
        }).collect(Collectors.toSet());
    }
}
