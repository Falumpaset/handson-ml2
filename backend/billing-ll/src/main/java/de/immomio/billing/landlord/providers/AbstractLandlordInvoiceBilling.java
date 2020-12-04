package de.immomio.billing.landlord.providers;

import de.immomio.billing.provider.AbstractInvoiceBilling;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.invoice.LandlordInvoice;
import de.immomio.model.abstractrepository.customer.BaseAbstractCustomerRepository;
import de.immomio.model.abstractrepository.invoice.BaseAbstractInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractLandlordInvoiceBilling<CR extends BaseAbstractCustomerRepository,
        IR extends BaseAbstractInvoiceRepository<LandlordInvoice>>
        extends AbstractInvoiceBilling<LandlordCustomer, LandlordInvoice, IR> {

    @Autowired
    private CR landlordCustomerRepository;

    @Override
    public void saveCustomer(LandlordCustomer customer) {
        landlordCustomerRepository.save(customer);
    }
}
