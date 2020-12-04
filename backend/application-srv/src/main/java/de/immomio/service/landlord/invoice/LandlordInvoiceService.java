package de.immomio.service.landlord.invoice;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.landlord.entity.invoice.LandlordInvoice;
import de.immomio.model.repository.service.landlord.invoice.LandlordInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Service
public class LandlordInvoiceService {

    private LandlordInvoiceRepository landlordInvoiceRepository;

    @Autowired
    public LandlordInvoiceService(LandlordInvoiceRepository landlordInvoiceRepository) {
        this.landlordInvoiceRepository = landlordInvoiceRepository;
    }

    public LandlordInvoice cancel(Long invoiceId) {
        LandlordInvoice invoice = landlordInvoiceRepository.findByInvoiceId(invoiceId);
        if (invoice == null) {
            throw new ApiValidationException("invoice with id not found");
        }
        invoice.setCancellation(true);

        return landlordInvoiceRepository.save(invoice);
    }

}
