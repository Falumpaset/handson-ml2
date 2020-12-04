package de.immomio.landlord.service.product;

import de.immomio.billing.invoice.AbstractDiscountHandler;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.discount.LandlordDiscount;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import org.springframework.stereotype.Service;

/**
 * @author Bastian Bliemeister.
 */
@Service
public class LandlordDiscountHandler
        extends AbstractDiscountHandler<LandlordCustomer, LandlordProduct, LandlordProductAddon, LandlordDiscount> {
}
