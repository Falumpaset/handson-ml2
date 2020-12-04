package de.immomio.billing.landlord;

import de.immomio.billing.product.AbstractPlanHandler;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.product.addon.LandlordAddonProduct;
import de.immomio.data.landlord.entity.product.addonproduct.LandlordCustomerAddonProduct;
import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Bastian Bliemeister, Maik Kingma
 */
@Service
@Slf4j
public abstract class AbstractLandlordPlanHandler extends AbstractPlanHandler<LandlordCustomer, LandlordCustomerProduct,
        LandlordCustomerAddonProduct, LandlordProduct, LandlordProductAddon, LandlordAddonProduct> {

    public abstract void saveCustomerProduct(LandlordCustomerProduct customerProduct);

    public abstract void saveCustomerAddonProduct(LandlordCustomerAddonProduct customerAddon);

    @Override
    public void saveProduct(LandlordCustomerProduct customerProduct) {
        saveCustomerProduct(customerProduct);
    }

    @Override
    public void saveAddon(LandlordCustomerAddonProduct customerAddon) {
        saveCustomerAddonProduct(customerAddon);
    }

    @Override
    public LandlordCustomerProduct createCustomerProduct() {
        return new LandlordCustomerProduct();
    }

    @Override
    public LandlordCustomerAddonProduct createCustomerAddonProduct() {
        return new LandlordCustomerAddonProduct();
    }

}
