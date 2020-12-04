package de.immomio.service.landlord.product;

import de.immomio.billing.landlord.AbstractLandlordPlanHandler;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.addonproduct.LandlordCustomerAddonProduct;
import de.immomio.model.repository.service.landlord.product.customer.LandlordCustomerProductRepository;
import de.immomio.model.repository.service.landlord.product.customer.addon.LandlordCustomerAddonProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Bastian Bliemeister, Maik Kingma
 */
@Service
@Slf4j
public class AdminLandlordPlanHandler extends AbstractLandlordPlanHandler {

    private final LandlordCustomerProductRepository customerProductRepository;

    private final LandlordCustomerAddonProductRepository customerAddonProductRepository;

    @Autowired
    public AdminLandlordPlanHandler(
            LandlordCustomerProductRepository customerProductRepository,
            LandlordCustomerAddonProductRepository customerAddonProductRepository
    ) {
        this.customerProductRepository = customerProductRepository;
        this.customerAddonProductRepository = customerAddonProductRepository;
    }

    @Override
    public void saveCustomerProduct(LandlordCustomerProduct customerProduct) {
        customerProductRepository.save(customerProduct);
    }

    @Override
    public void saveCustomerAddonProduct(LandlordCustomerAddonProduct customerAddon) {
        customerAddonProductRepository.save(customerAddon);
    }
}
