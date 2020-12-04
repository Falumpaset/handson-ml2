package de.immomio.controller.filer.landlord.file.invoice;

import de.immomio.constants.exceptions.InvoiceCreationException;
import de.immomio.controller.BaseController;
import de.immomio.data.base.type.common.FileType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.invoice.LandlordInvoice;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.model.repository.landlord.invoice.LandlordInvoiceRepository;
import de.immomio.pdf.invoice.InvoicePDFGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Johannes Hiemer.
 */
@Slf4j
@Controller
@RequestMapping(value = "/invoices")
public class InvoicePdfController extends BaseController {

    private final LandlordInvoiceRepository invoiceRepository;

    private final InvoicePDFGenerator invoicePDFGenerator;

    private static final String INVOICE_NOT_FOUND = "INVOICE_NOT_FOUND_L";

    private static final String INVOICE_NOT_CREATED = "INVOICE_NOT_CREATED_L";

    public InvoicePdfController(
            LandlordInvoiceRepository invoiceRepository,
            InvoicePDFGenerator invoicePDFGenerator
    ) {
        this.invoiceRepository = invoiceRepository;
        this.invoicePDFGenerator = invoicePDFGenerator;
    }

    @GetMapping(value = "/{id}/pdf")
    public ResponseEntity<Object> invoice(@PathVariable Long id) throws IOException {
        Optional<LandlordInvoice> invoiceOpt = invoiceRepository.findById(id);

        if (invoiceOpt.isPresent()) {
            LandlordInvoice invoice = invoiceOpt.get();
            try {
                File file = createInvoiceOrCancelation(invoice, false);
                return responseWithFile(file, FileType.PDF, INVOICE_NOT_CREATED, HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (InvoiceCreationException e) {
                log.error(e.getMessage(), e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(INVOICE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{id}/pdf/refund")
    public ResponseEntity<Object> refund(@PathVariable Long id) throws IOException {
        Optional<LandlordInvoice> invoiceOpt = invoiceRepository.findById(id);

        if (invoiceOpt.isPresent()) {
            LandlordInvoice invoice = invoiceOpt.get();

            if (!invoice.isCancellation()) {
                return new ResponseEntity<>("INVOICE_NOT_CANCELED_L", HttpStatus.BAD_REQUEST);
            }

            try {
                File file = createInvoiceOrCancelation(invoice, true);
                return responseWithFile(file, FileType.PDF, INVOICE_NOT_FOUND, HttpStatus.NOT_FOUND);
            } catch (InvoiceCreationException e) {
                log.error(e.getMessage(), e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(INVOICE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private File createInvoiceOrCancelation(LandlordInvoice invoice, boolean cancellation)
            throws InvoiceCreationException {
        File file = null;

        if (invoice != null) {
            LandlordCustomer customer = invoice.getCustomer();

            Address address = customer.getAddress();
            if (address != null) {
                if (invoice.getAddress() == null) {
                    invoice.setAddress(customer.getAddress());
                    invoiceRepository.customSave(invoice);
                }
            } else {
                throw new InvoiceCreationException("No address for customer specified");
            }

            file = invoicePDFGenerator.createDocument(invoice, cancellation);
        }

        return file;
    }
}
