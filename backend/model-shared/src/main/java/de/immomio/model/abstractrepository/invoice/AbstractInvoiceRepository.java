package de.immomio.model.abstractrepository.invoice;

import de.immomio.data.base.entity.invoice.AbstractInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

@RepositoryRestResource(path = "invoices")
public interface AbstractInvoiceRepository<I extends AbstractInvoice<?>> extends BaseAbstractInvoiceRepository<I>,
        InvoiceRepositoryCustom<I> {

    String ID_PARAM = "id";
    String INVOICE_PARAM = "invoice";

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = :id AND o.customer.id = ?#{principal.customer.id}")
    Optional<I> findById(@Param(ID_PARAM) Long id);

    @Override
    @PreAuthorize("#invoice.customer.id == principal.customer.id")
    <S extends I> S save(@Param(INVOICE_PARAM) S invoice);

    @Override
    @Query("SELECT o FROM #{#entityName} o INNER JOIN o.customer c WHERE c.id = ?#{principal.customer.id}")
    Page<I> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param(ID_PARAM) Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param(INVOICE_PARAM) I invoice);
}
