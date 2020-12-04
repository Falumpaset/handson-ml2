package de.immomio.service.landlord.providers;

import de.immomio.billing.landlord.providers.AbstractLandlordStripeBilling;
import de.immomio.model.repository.core.landlord.customer.BaseLandlordCustomerRepository;
import de.immomio.model.repository.core.landlord.invoice.BaseLandlordInvoiceRepository;
import org.springframework.stereotype.Service;

@Service
public class LandlordStripeBilling
        extends AbstractLandlordStripeBilling<BaseLandlordCustomerRepository, BaseLandlordInvoiceRepository> {
}
