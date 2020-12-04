package de.immomio.service.landlord.product;

import de.immomio.billing.landlord.AbstractLandlordInvoiceCalculator;
import de.immomio.model.repository.service.landlord.discount.LandlordCustomerQuotaPackageDiscountRepository;
import de.immomio.model.repository.service.landlord.invoice.LandlordInvoiceRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminLandlordInvoiceCalculator
        extends AbstractLandlordInvoiceCalculator<AdminLandlordDiscountHandler, LandlordInvoiceRepository, LandlordCustomerQuotaPackageDiscountRepository> {
}
