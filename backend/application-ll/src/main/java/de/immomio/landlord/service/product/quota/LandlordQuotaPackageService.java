package de.immomio.landlord.service.product.quota;

import de.immomio.beans.landlord.product.quota.QuotaPackageBean;
import de.immomio.data.base.type.product.quota.QuotaProductType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.discount.LandlordCustomerQuotaPackageDiscount;
import de.immomio.data.landlord.entity.product.quota.LandlordQuotaPackage;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.landlord.discount.LandlordCustomerQuotaPackageDiscountRepository;
import de.immomio.model.repository.landlord.product.quota.LandlordQuotaPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Service
public class LandlordQuotaPackageService {

    private LandlordQuotaPackageRepository quotaPackageRepository;

    private LandlordCustomerQuotaPackageDiscountRepository quotaPackageDiscountRepository;

    private final UserSecurityService userSecurityService;

    @Autowired
    public LandlordQuotaPackageService(
            LandlordQuotaPackageRepository quotaPackageRepository,
            LandlordCustomerQuotaPackageDiscountRepository quotaPackageDiscountRepository,
            UserSecurityService userSecurityService
    ) {
        this.quotaPackageRepository = quotaPackageRepository;
        this.quotaPackageDiscountRepository = quotaPackageDiscountRepository;
        this.userSecurityService = userSecurityService;
    }

    public List<QuotaPackageBean> getQuotaPackagesByType(QuotaProductType quotaProductType) {
        LandlordCustomer customer = userSecurityService.getPrincipalUser().getCustomer();

        List<LandlordQuotaPackage> quotaPackages = quotaPackageRepository.findAllByType(quotaProductType);

        return quotaPackages.stream()
                .map(landlordQuotaPackage -> getQuotaPackageBean(customer, landlordQuotaPackage))
                .collect(Collectors.toList());
    }

    private QuotaPackageBean getQuotaPackageBean(LandlordCustomer customer, LandlordQuotaPackage landlordQuotaPackage) {
        Optional<LandlordCustomerQuotaPackageDiscount> discount =
                quotaPackageDiscountRepository.findFirstByCustomerAndQuotaPackage(customer, landlordQuotaPackage);

        QuotaPackageBean quotaPackageBean = QuotaPackageBean.builder()
                .id(landlordQuotaPackage.getId())
                .quantity(landlordQuotaPackage.getQuantity())
                .price(BigDecimal.valueOf(landlordQuotaPackage.getPrice().getFixedPart()))
                .type(landlordQuotaPackage.getType())
                .build();

        discount.ifPresentOrElse(
                landlordCustomerQuotaPackageDiscount -> {
                    Double discountValue = landlordCustomerQuotaPackageDiscount.getDiscount().getValue();
                    quotaPackageBean.setPostDiscountPrice(calculateDiscount(quotaPackageBean.getPrice(), discountValue));
                    quotaPackageBean.setDiscount(BigDecimal.valueOf(discountValue));
                },
                () -> quotaPackageBean.setPostDiscountPrice(quotaPackageBean.getPrice()));

        return quotaPackageBean;
    }

    private BigDecimal calculateDiscount(BigDecimal price, Double discount) {
        return price.subtract(price.multiply(BigDecimal.valueOf(discount)));
    }
}
