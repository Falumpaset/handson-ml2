package de.immomio.billing.landlord;

import de.immomio.billing.landlord.providers.AbstractLandlordInvoiceBilling;
import de.immomio.model.repository.landlord.customer.LandlordCustomerRepository;
import de.immomio.model.repository.landlord.invoice.LandlordInvoiceRepository;
import org.springframework.stereotype.Service;

/**
 * @author Bastian Bliemeister
 */
@Service
public class LandlordInvoiceBilling
        extends AbstractLandlordInvoiceBilling<LandlordCustomerRepository, LandlordInvoiceRepository> {
}
