package de.immomio.service.landlord;

import de.immomio.billing.invoice.AbstractDiscountHandler;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.discount.LandlordDiscount;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import org.springframework.stereotype.Service;

@Service
public class CrawlerLandlordDiscountHandler
        extends AbstractDiscountHandler<LandlordCustomer, LandlordProduct, LandlordProductAddon, LandlordDiscount> {
}
