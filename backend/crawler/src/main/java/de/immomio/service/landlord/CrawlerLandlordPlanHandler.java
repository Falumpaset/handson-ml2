package de.immomio.service.landlord;


import de.immomio.billing.landlord.AbstractLandlordPlanHandler;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.addonproduct.LandlordCustomerAddonProduct;
import de.immomio.model.repository.core.landlord.product.customer.BaseCustomerProductRepository;
import de.immomio.model.repository.core.landlord.product.customer.addon.BaseCustomerAddonProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Maik Kingma
 */

@Service
@Slf4j
public class CrawlerLandlordPlanHandler extends AbstractLandlordPlanHandler {

    private final BaseCustomerProductRepository customerProductRepository;

    private final BaseCustomerAddonProductRepository customerAddonProductRepository;

    @Autowired
    public CrawlerLandlordPlanHandler(
            BaseCustomerProductRepository customerProductRepository,
            BaseCustomerAddonProductRepository customerAddonProductRepository
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
