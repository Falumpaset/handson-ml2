package de.immomio.landlord.service.product;

import de.immomio.billing.landlord.AbstractLandlordInvoiceCalculator;
import de.immomio.model.repository.landlord.discount.LandlordCustomerQuotaPackageDiscountRepository;
import de.immomio.model.repository.landlord.invoice.LandlordInvoiceRepository;
import org.springframework.stereotype.Service;

@Service
public class LandlordInvoiceCalculator
        extends AbstractLandlordInvoiceCalculator<LandlordDiscountHandler, LandlordInvoiceRepository, LandlordCustomerQuotaPackageDiscountRepository> {
}
