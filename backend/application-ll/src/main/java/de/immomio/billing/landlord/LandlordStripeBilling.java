package de.immomio.billing.landlord;

import de.immomio.billing.landlord.providers.AbstractLandlordStripeBilling;
import de.immomio.model.repository.landlord.customer.LandlordCustomerRepository;
import de.immomio.model.repository.landlord.invoice.LandlordInvoiceRepository;
import org.springframework.stereotype.Service;

/**
 * @author Bastian Bliemeister
 */
@Service
public class LandlordStripeBilling
        extends AbstractLandlordStripeBilling<LandlordCustomerRepository, LandlordInvoiceRepository> {
}
