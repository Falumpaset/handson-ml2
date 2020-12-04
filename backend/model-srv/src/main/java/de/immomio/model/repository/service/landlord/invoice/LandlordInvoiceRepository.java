package de.immomio.model.repository.service.landlord.invoice;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.invoice.LandlordInvoice;
import de.immomio.model.repository.core.landlord.invoice.BaseLandlordInvoiceRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Maik Kingma
 */
@RepositoryRestResource(path = "ll-invoices")
public interface LandlordInvoiceRepository extends BaseLandlordInvoiceRepository {

    @Query("SELECT o FROM #{#entityName} o WHERE o.invoiceDate >= :fromDate AND o.invoiceDate < :toDate")
    List<LandlordInvoice> findAllBetweenDates(@Param(FROM_DATE_PARAM) Date fromDate, @Param(TO_DATE_PARAM) Date toDate);

    @Query("SELECT o FROM #{#entityName} o WHERE o.cancellation = true AND o.invoiceDate >= :fromDate AND o.invoiceDate < :toDate")
    List<LandlordInvoice> findAllCancelledBetweenDates(@Param(FROM_DATE_PARAM) Date fromDate, @Param(TO_DATE_PARAM) Date toDate);

    @Override
    Optional<LandlordInvoice> findById(@Param(ID_PARAM) Long id);

    LandlordInvoice findByInvoiceId(@Param(INVOICE_ID_PARAM) Long invoiceId);

    List<LandlordInvoice> findAllByCustomer(LandlordCustomer customer);
}
