package de.immomio.controller.invoice;

import com.google.common.io.Files;
import de.immomio.common.zip.FileZipper;
import de.immomio.controller.BaseController;
import de.immomio.data.base.type.common.FileType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.invoice.LandlordInvoice;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.model.repository.service.landlord.invoice.LandlordInvoiceRepository;
import de.immomio.pdf.invoice.InvoicePDFGenerator;
import de.immomio.service.landlord.invoice.LandlordInvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Maik Kingma
 */

@Slf4j
@Controller
@RequestMapping(value = "/ll-invoices")
public class LandlordInvoiceController extends BaseController {

    private final LandlordInvoiceRepository landlordInvoiceRepository;

    private final LandlordInvoiceService invoiceService;

    private final InvoicePDFGenerator invoicePDFGenerator;

    private final FileZipper fileZipper;

    private static final String INVOICE_COULD_NOT_GENERATE = "INVOICE_COULD_NOT_GENERATE_L";

    private static final String NO_INVOICES_COULD_BE_CONVERTED = "NO_INVOICES_COULD_BE_CONVERTED_L";

    private static final String INVOICE_NOT_FOUND = "INVOICE_NOT_FOUND_L";

    private static final String INVOICE_NOT_A_STORNO = "INVOICE_NOT_A_STORNO_L";

    @Autowired
    public LandlordInvoiceController(
            LandlordInvoiceRepository landlordInvoiceRepository,
            LandlordInvoiceService invoiceService,
            InvoicePDFGenerator invoicePDFGenerator,
            FileZipper fileZipper
    ) {
        this.landlordInvoiceRepository = landlordInvoiceRepository;
        this.invoiceService = invoiceService;
        this.invoicePDFGenerator = invoicePDFGenerator;
        this.fileZipper = fileZipper;
    }

    @GetMapping(value = "/download", produces = "application/zip")
    public ResponseEntity<Object> download(
            @RequestParam(value = "from") @DateTimeFormat(pattern = "dd-MM-yyyy") Date from,
            @RequestParam(value = "to") @DateTimeFormat(pattern = "dd-MM-yyyy") Date to
    ) {
        to = DateUtils.addDays(to, 1);
        List<LandlordInvoice> invoices = landlordInvoiceRepository.findAllBetweenDates(from, to);
        return handleDownloadInvoiceResponse(invoices, false);
    }

    @GetMapping(value = "/download-cancellation", produces = "application/zip")
    public ResponseEntity<Object> downloadCancellation(
            @RequestParam(value = "from") @DateTimeFormat(pattern = "dd-MM-yyyy") Date from,
            @RequestParam(value = "to") @DateTimeFormat(pattern = "dd-MM-yyyy") Date to
    ) {
        to = DateUtils.addDays(to, 1);
        List<LandlordInvoice> invoices = landlordInvoiceRepository.findAllCancelledBetweenDates(from, to);
        return handleDownloadInvoiceResponse(invoices, true);
    }

    @GetMapping(value = "/{invoiceId}/pdf")
    public ResponseEntity<Object> invoice(@PathVariable Long invoiceId) throws IOException {
        LandlordInvoice invoice = landlordInvoiceRepository.findByInvoiceId(invoiceId);

        if (invoice == null) {
            return new ResponseEntity<>(INVOICE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        File tempDir = Files.createTempDir();
        File file = createInvoiceOrCancellation(invoice, false, tempDir);
        ResponseEntity<Object> response = responseWithFile(file,
                                                           FileType.PDF,
                                                           INVOICE_COULD_NOT_GENERATE,
                                                           HttpStatus.INTERNAL_SERVER_ERROR);

        deleteTempDirectory(tempDir);
        return response;
    }

    @GetMapping(value = "/{invoiceId}/pdf/storno")
    public ResponseEntity<Object> storno(@PathVariable Long invoiceId) throws IOException {
        LandlordInvoice invoice = landlordInvoiceRepository.findByInvoiceId(invoiceId);

        if (invoice == null) {
            return new ResponseEntity<>(INVOICE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if (!invoice.isCancellation()) {
            return new ResponseEntity<>(INVOICE_NOT_A_STORNO, HttpStatus.NOT_FOUND);
        }

        File tempDir = Files.createTempDir();
        File file = createInvoiceOrCancellation(invoice, invoice.isCancellation(), tempDir);
        ResponseEntity<Object> response = responseWithFile(file,
                                                           FileType.PDF,
                                                           INVOICE_COULD_NOT_GENERATE,
                                                           HttpStatus.INTERNAL_SERVER_ERROR);

        deleteTempDirectory(tempDir);
        return response;
    }

    @PostMapping("/cancel/{invoiceId}")
    public ResponseEntity<Object> cancel(@PathVariable Long invoiceId) {
        invoiceService.cancel(invoiceId);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<Object> handleDownloadInvoiceResponse(List<LandlordInvoice> invoices, boolean cancellation) {
        invoices = invoices.stream()
                           .filter(invoice -> invoice.getPostDiscountPrice() > 0.0)
                           .collect(Collectors.toList());

        if (invoices.isEmpty()) {
            return new ResponseEntity<>("NO_INVOICES_PRICE GREATER_0_COULD_BE_FOUND_L", HttpStatus.NOT_FOUND);
        }

        File tempDir = Files.createTempDir();
        File zipTempDir = Files.createTempDir();
        try {
            createInvoiceFiles(invoices, cancellation, tempDir);

            File zipFile = new File(zipTempDir, UUID.randomUUID().toString() + ".zip");
            if (Objects.requireNonNull(tempDir.listFiles()).length > 0) {
                fileZipper.zipFiles(tempDir, zipFile);
                return responseWithFile(zipFile, FileType.ZIP, NO_INVOICES_COULD_BE_CONVERTED, HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(NO_INVOICES_COULD_BE_CONVERTED, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            deleteTempDirectory(tempDir);
            deleteTempDirectory(zipTempDir);
        }
    }

    private void createInvoiceFiles(List<LandlordInvoice> invoices, boolean cancellation, File tempDir) {
        for (LandlordInvoice invoice : invoices) {
            createInvoiceOrCancellation(invoice, cancellation, tempDir);
        }
    }

    private void deleteTempDirectory(File tempDir) {
        try {
            FileUtils.forceDelete(tempDir);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private File createInvoiceOrCancellation(LandlordInvoice invoice, boolean cancellation, File tempDir) {
        File file = null;

        if (invoice != null) {
            LandlordCustomer customer = invoice.getCustomer();

            Address address = customer.getAddress();
            if (address != null) {
                if (invoice.getAddress() == null) {
                    invoice.setAddress(customer.getAddress());
                    landlordInvoiceRepository.save(invoice);
                }

                file = invoicePDFGenerator.createDocument(invoice, cancellation, tempDir);
            }
        }

        return file;
    }
}
