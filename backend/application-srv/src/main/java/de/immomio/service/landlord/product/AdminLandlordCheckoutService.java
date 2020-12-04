package de.immomio.service.landlord.product;

import de.immomio.billing.landlord.AbstractLandlordCheckoutService;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.model.repository.service.landlord.invoice.LandlordInvoiceRepository;
import de.immomio.model.repository.service.landlord.product.basket.LandlordProductBasketProductAddonRepository;
import de.immomio.model.repository.service.landlord.product.basket.LandlordProductBasketRepository;
import de.immomio.model.repository.service.landlord.product.customer.addon.LandlordCustomerAddonProductRepository;
import de.immomio.pdf.invoice.InvoicePDFGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Bastian Bliemeister, Maik Kingma
 */

@Slf4j
@Service
public class AdminLandlordCheckoutService extends AbstractLandlordCheckoutService<
        AdminLandlordInvoiceCalculator,
        AdminLandlordPlanHandler,
        LandlordInvoiceRepository,
        LandlordCustomerAddonProductRepository,
        LandlordProductBasketProductAddonRepository,
        LandlordProductBasketRepository,
        AdminLandlordCacheEvictService
        > {

    private final AdminLandlordCacheEvictService landlordCacheEvictService;

    @Autowired
    public AdminLandlordCheckoutService(
            LandlordMailSender mailSender,
            AdminLandlordPlanHandler planHandler,
            InvoicePDFGenerator invoicePDFGenerator,
            AdminLandlordInvoiceCalculator invoiceCalculator,
            LandlordCustomerAddonProductRepository addonProductRepository,
            LandlordProductBasketProductAddonRepository basketProductAddonRepository,
            LandlordProductBasketRepository productBasketRepository,
            AdminLandlordCacheEvictService landlordCacheEvictService
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
    protected AdminLandlordCacheEvictService getCacheEvictService() {
        return landlordCacheEvictService;
    }
}
