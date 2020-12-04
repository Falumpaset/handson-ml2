package de.immomio.service.landlord.addon;

import de.immomio.data.landlord.bean.product.LandlordProductAddonBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.service.landlord.product.LandlordProductDataConverter;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */
@Service
public class LandlordAddonService {

    private final LandlordProductDataConverter productDataConverter;

    public LandlordAddonService(LandlordProductDataConverter productDataConverter) {
        this.productDataConverter = productDataConverter;
    }

    public Set<LandlordProductAddonBean> getAllAddonsByProduct(LandlordProduct product) {
        return product.getProductAddons().stream()
                .map(productDataConverter::convertAddonToProductAddonBean)
                .collect(Collectors.toSet());
    }

    public Set<LandlordProductAddonBean> getAllAddonsByProduct(LandlordProduct product, LandlordCustomer customer) {
        return product.getProductAddons().stream()
                .map(productAddon -> productDataConverter.convertAddonToProductAddonBean(productAddon, customer))
                .collect(Collectors.toSet());
    }

}
