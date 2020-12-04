package de.immomio.service.landlord.invoice;

import de.immomio.AbstractTest;
import de.immomio.data.landlord.entity.invoice.LandlordInvoice;
import de.immomio.model.repository.service.landlord.invoice.LandlordInvoiceRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Niklas Lindemann
 */
public class LandlordInvoiceServiceTest extends AbstractTest {

    @Mock
    private LandlordInvoiceRepository invoiceRepository;

    @InjectMocks
    private LandlordInvoiceService invoiceService;

    @Test
    public void cancel()  {
        LandlordInvoice invoice = new LandlordInvoice();
        invoice.setCancellation(false);

        when(invoiceRepository.findByInvoiceId(anyLong())).thenReturn(invoice);
        when(invoiceRepository.save(any(LandlordInvoice.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        LandlordInvoice cancelled = invoiceService.cancel(0L);

        Assert.assertTrue(cancelled.isCancellation());
        verify(invoiceRepository, times(1)).save(any(LandlordInvoice.class));
    }
}