package de.immomio.service.landlord;

import de.immomio.billing.landlord.AbstractLandlordInvoiceCalculator;
import de.immomio.model.repository.core.landlord.discount.BaseLandlordCustomerQuotaPackageDiscountRepository;
import de.immomio.model.repository.core.landlord.invoice.BaseLandlordInvoiceRepository;
import org.springframework.stereotype.Service;

@Service
public class CrawlerLandlordInvoiceCalculator
        extends AbstractLandlordInvoiceCalculator<CrawlerLandlordDiscountHandler, BaseLandlordInvoiceRepository, BaseLandlordCustomerQuotaPackageDiscountRepository> {
}
