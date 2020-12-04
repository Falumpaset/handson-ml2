package de.immomio.model.abstractrepository.invoice;

import de.immomio.data.base.entity.invoice.AbstractInvoice;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;
import java.util.Optional;

@RepositoryRestResource(path = "invoices")
public interface BaseAbstractInvoiceRepository<I extends AbstractInvoice<?>> extends JpaRepository<I, Long> {

    String ID_PARAM = "id";
    String INVOICE_PARAM = "invoice";
    String INVOICE_ID_PARAM = "invoiceId";
    String FROM_DATE_PARAM = "fromDate";
    String TO_DATE_PARAM = "toDate";
    String CUSTOMER_PARAM = "customer";

    @Override
    Optional<I> findById(@Param(ID_PARAM) Long id);

    @Override
    <S extends I> S save(@Param(INVOICE_PARAM) S invoice);

    @Override
    @Query("SELECT o FROM #{#entityName} o")
    Page<I> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param(ID_PARAM) Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param(INVOICE_PARAM) I invoice);

    @RestResource(exported = false)
    @Query("SELECT SUM(o.postDiscountPrice) FROM #{#entityName} o WHERE o.invoiceDate >= :fromDate AND o.invoiceDate < :toDate AND o.cancellation = false")
    Double getRevenueBetweenDates(@Param(FROM_DATE_PARAM) Date fromDate, @Param(TO_DATE_PARAM) Date toDate);


    @RestResource(exported = false)
    @Query("SELECT SUM(o.postDiscountPrice) FROM #{#entityName} o WHERE o.customer = :customer AND o.invoiceDate >= :fromDate AND o.invoiceDate < :toDate AND o.cancellation = false")
    Double getRevenueByCustomerBetweenDates(@Param(CUSTOMER_PARAM) LandlordCustomer customer, @Param(FROM_DATE_PARAM) Date fromDate, @Param(TO_DATE_PARAM) Date toDate);

    @RestResource(exported = false)
    @Query("SELECT i FROM #{#entityName} i where i.invoiceId = :invoiceId")
    Optional<I> findFirstByInvoiceId(@Param("invoiceId") Long invoiceId);

}