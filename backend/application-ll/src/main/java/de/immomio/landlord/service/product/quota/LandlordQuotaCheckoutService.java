package de.immomio.landlord.service.product.quota;

import de.immomio.billing.landlord.quota.AbstractLandlordQuotaCheckoutService;
import de.immomio.landlord.service.product.LandlordInvoiceCalculator;
import de.immomio.model.repository.landlord.invoice.LandlordInvoiceRepository;
import de.immomio.model.repository.landlord.product.basket.quota.LandlordQuotaBasketRepository;
import de.immomio.model.repository.landlord.product.quota.LandlordQuotaCustomerProductRepository;
import de.immomio.model.repository.landlord.product.quota.LandlordQuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Service
public class LandlordQuotaCheckoutService extends
        AbstractLandlordQuotaCheckoutService<LandlordQuotaRepository,
                LandlordQuotaCustomerProductRepository,
                LandlordInvoiceCalculator, LandlordQuotaBasketRepository, LandlordInvoiceRepository> {

    @Autowired
    public LandlordQuotaCheckoutService(
            LandlordQuotaRepository quotaRepository,
            LandlordQuotaCustomerProductRepository quotaCustomerProductRepository,
            LandlordInvoiceCalculator invoiceCalculator,
            LandlordQuotaBasketRepository quotaBasketRepository,
            LandlordInvoiceRepository invoiceRepository
    ) {
        super(quotaRepository, quotaCustomerProductRepository, invoiceCalculator, quotaBasketRepository, invoiceRepository);
    }
}
