package de.immomio.model.repository.core.propertysearcher.invoice;

import de.immomio.data.propertysearcher.entity.invoice.PropertySearcherInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "invoices")
public interface BasePropertySearcherInvoiceRepository extends JpaRepository<PropertySearcherInvoice, Long> {
}
