package de.immomio.model.repository.core.landlord.invoice;

import de.immomio.data.landlord.entity.invoice.LandlordInvoice;
import de.immomio.model.abstractrepository.invoice.BaseAbstractInvoiceRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "invoices")
public interface BaseLandlordInvoiceRepository extends BaseAbstractInvoiceRepository<LandlordInvoice> {
}
