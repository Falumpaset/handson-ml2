package de.immomio.service.landlord.providers;

import de.immomio.billing.landlord.providers.AbstractLandlordInvoiceBilling;
import de.immomio.model.repository.core.landlord.customer.BaseLandlordCustomerRepository;
import de.immomio.model.repository.core.landlord.invoice.BaseLandlordInvoiceRepository;
import org.springframework.stereotype.Service;

@Service
public class LandlordInvoiceBilling
        extends AbstractLandlordInvoiceBilling<BaseLandlordCustomerRepository, BaseLandlordInvoiceRepository> {
}
