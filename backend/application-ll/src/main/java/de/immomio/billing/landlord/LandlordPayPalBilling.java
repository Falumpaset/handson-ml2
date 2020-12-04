package de.immomio.billing.landlord;

import de.immomio.billing.landlord.providers.AbstractLandlordPayPalBilling;
import de.immomio.model.repository.landlord.customer.LandlordCustomerRepository;
import de.immomio.model.repository.landlord.invoice.LandlordInvoiceRepository;
import org.springframework.stereotype.Service;

/**
 * @author Bastian Bliemeister
 */
@Service
public class LandlordPayPalBilling extends AbstractLandlordPayPalBilling<LandlordCustomerRepository,
        LandlordInvoiceRepository> {
}
