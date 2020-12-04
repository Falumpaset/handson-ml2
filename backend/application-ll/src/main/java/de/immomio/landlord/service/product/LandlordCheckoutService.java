package de.immomio.landlord.service.product;

import de.immomio.billing.landlord.AbstractLandlordCheckoutService;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.model.repository.landlord.invoice.LandlordInvoiceRepository;
import de.immomio.model.repository.landlord.product.basket.LandlordProductBasketProductAddonRepository;
import de.immomio.model.repository.landlord.product.basket.LandlordProductBasketRepository;
import de.immomio.model.repository.landlord.product.customer.addon.LandlordCustomerAddonProductRepository;
import de.immomio.pdf.invoice.InvoicePDFGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Bastian Bliemeister, Maik Kingma
 */

@Slf4j
@Service
public class LandlordCheckoutService extends AbstractLandlordCheckoutService<
        LandlordInvoiceCalculator,
        LandlordPlanHandler,
        LandlordInvoiceRepository,
        LandlordCustomerAddonProductRepository,
        LandlordProductBasketProductAddonRepository,
        LandlordProductBasketRepository,
        LandlordCacheEvictService
        > {

    private final LandlordCacheEvictService landlordCacheEvictService;

    @Autowired
    public LandlordCheckoutService(
            LandlordMailSender mailSender,
            LandlordPlanHandler planHandler,
            InvoicePDFGenerator invoicePDFGenerator,
            LandlordInvoiceCalculator invoiceCalculator,
            LandlordCustomerAddonProductRepository addonProductRepository,
            LandlordProductBasketProductAddonRepository basketProductAddonRepository,
            LandlordProductBasketRepository productBasketRepository,
            LandlordCacheEvictService landlordCacheEvictService
    ) {
        super(
                mailSender,
                planHandler,
                invoicePDFGenerator,
                invoiceCalculator,
                addonProductRepository,
                basketProductAddonRepository,
                productBasketRepository
        );
        this.landlordCacheEvictService = landlordCacheEvictService;
    }

    @Override
    protected LandlordCacheEvictService getCacheEvictService() {
        return landlordCacheEvictService;
    }
}
