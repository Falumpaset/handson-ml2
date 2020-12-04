package de.immomio.landlord.service.product;

import de.immomio.billing.landlord.AbstractLandlordPlanHandler;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.addonproduct.LandlordCustomerAddonProduct;
import de.immomio.model.repository.landlord.product.customer.LandlordCustomerProductRepository;
import de.immomio.model.repository.landlord.product.customer.addon.LandlordCustomerAddonProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Bastian Bliemeister, Maik Kingma
 */
@Service
@Slf4j
public class LandlordPlanHandler extends AbstractLandlordPlanHandler {

    private final LandlordCustomerProductRepository customerProductRepository;

    private final LandlordCustomerAddonProductRepository customerAddonProductRepository;

    @Autowired
    public LandlordPlanHandler(
            LandlordCustomerProductRepository customerProductRepository,
            LandlordCustomerAddonProductRepository customerAddonProductRepository
    ) {
        this.customerProductRepository = customerProductRepository;
        this.customerAddonProductRepository = customerAddonProductRepository;
    }

    @Override
    public void saveCustomerProduct(LandlordCustomerProduct customerProduct) {
        customerProductRepository.customSave(customerProduct);
    }

    @Override
    public void saveCustomerAddonProduct(LandlordCustomerAddonProduct customerAddon) {
        customerAddonProductRepository.customSave(customerAddon);
    }
}
