package de.immomio.landlord.service.product.quota;

import de.immomio.billing.exception.InvoiceGenerationException;
import de.immomio.constants.CheckoutResult;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.BasketCheckoutException;
import de.immomio.data.base.bean.product.basket.BasketValueBean;
import de.immomio.data.base.bean.product.basket.CheckoutBean;
import de.immomio.data.base.bean.product.basket.quota.LandlordQuotaBasketBean;
import de.immomio.data.base.type.product.basket.ProductBasketStatus;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketQuotaPackage;
import de.immomio.data.landlord.entity.product.basket.quota.LandlordQuotaBasket;
import de.immomio.data.landlord.entity.product.quota.LandlordQuotaPackage;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.landlord.product.basket.quota.LandlordQuotaBasketRepository;
import de.immomio.model.repository.landlord.product.quota.LandlordQuotaPackageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Component
public class LandlordQuotaBasketHandler {

    public static final String QUOTA_PACKAGE_NOT_FOUND = "QUOTA_PACKAGE_NOT_FOUND_L";

    private LandlordQuotaCheckoutService checkoutService;

    private LandlordQuotaBasketRepository quotaBasketRepository;

    private LandlordQuotaPackageRepository quotaPackageRepository;

    private final UserSecurityService userSecurityService;

    @Autowired
    public LandlordQuotaBasketHandler(
            LandlordQuotaCheckoutService checkoutService,
            LandlordQuotaBasketRepository quotaBasketRepository,
            LandlordQuotaPackageRepository quotaPackageRepository,
            UserSecurityService userSecurityService
    ) {
        this.checkoutService = checkoutService;
        this.quotaBasketRepository = quotaBasketRepository;
        this.quotaPackageRepository = quotaPackageRepository;
        this.userSecurityService = userSecurityService;
    }

    public BasketValueBean getBasketValue(LandlordQuotaBasketBean basketBean) throws InvoiceGenerationException {
        LandlordQuotaBasket basket = createQuotaBasket(basketBean);
        BasketValueBean basketValueBean = checkoutService.basketValue(basket, basketBean.getPaymentMethod());

        return basketValueBean;
    }

    public LandlordQuotaBasket createAndSaveQuotaBasket(LandlordQuotaBasketBean basketBean) {
        LandlordQuotaBasket quotaBasket = createQuotaBasket(basketBean);

        return quotaBasketRepository.save(quotaBasket);
    }

    private LandlordQuotaBasket createQuotaBasket(LandlordQuotaBasketBean basketBean) {
        LandlordQuotaBasket basket = new LandlordQuotaBasket();
        LandlordCustomer customer = userSecurityService.getPrincipalUser().getCustomer();

        basket.setCustomer(customer);

        basket.setStatus(ProductBasketStatus.PENDING);
        basketBean.getQuotaPackages().forEach(quotaPackageQuantity -> {
            Optional<LandlordQuotaPackage> quotaPackage = quotaPackageRepository.findById(quotaPackageQuantity.getQuotaPackageId());
            quotaPackage.ifPresentOrElse(landlordQuotaPackage -> {
                LandlordProductBasketQuotaPackage basketQuotaPackage = new LandlordProductBasketQuotaPackage();
                basketQuotaPackage.setQuotaBasket(basket);
                basketQuotaPackage.setQuantity(quotaPackageQuantity.getQuantity());
                basketQuotaPackage.setQuotaPackage(landlordQuotaPackage);
                basket.getQuotaPackages().add(basketQuotaPackage);
            }, () -> {
                throw new ApiValidationException(QUOTA_PACKAGE_NOT_FOUND);
            });

        });

        return basket;
    }

    public CheckoutResult checkoutBasket(Long id, CheckoutBean checkoutBean) throws BasketCheckoutException {
        return checkoutService.processCheckout(id ,checkoutBean);
    }

}
