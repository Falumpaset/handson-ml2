package de.immomio.service.landlord.providers;

import de.immomio.billing.landlord.providers.AbstractLandlordPayPalBilling;
import de.immomio.model.repository.core.landlord.customer.BaseLandlordCustomerRepository;
import de.immomio.model.repository.core.landlord.invoice.BaseLandlordInvoiceRepository;
import org.springframework.stereotype.Service;

@Service
public class LandlordPayPalBilling
        extends AbstractLandlordPayPalBilling<BaseLandlordCustomerRepository, BaseLandlordInvoiceRepository> {
}
