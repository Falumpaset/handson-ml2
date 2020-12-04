package de.immomio.service.landlord.providers;

import de.immomio.billing.landlord.providers.AbstractLandlordStripeBilling;
import de.immomio.model.repository.service.landlord.customer.LandlordCustomerRepository;
import de.immomio.model.repository.service.landlord.invoice.LandlordInvoiceRepository;
import org.springframework.stereotype.Service;

@Service
public class LandlordStripeBilling
        extends AbstractLandlordStripeBilling<LandlordCustomerRepository, LandlordInvoiceRepository> {
}
