package de.immomio.landlord.service.product.quota;

import de.immomio.beans.landlord.product.quota.QuotaProductBean;
import de.immomio.data.base.type.product.quota.QuotaProductType;
import de.immomio.data.landlord.entity.product.quota.LandlordQuotaCustomerProduct;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.landlord.product.quota.LandlordQuotaCustomerProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Niklas Lindemann
 */

@Service
public class LandlordQuotaProductService {

    private LandlordQuotaCustomerProductRepository quotaCustomerProductRepository;
    private final UserSecurityService userSecurityService;

    @Autowired
    public LandlordQuotaProductService(
            LandlordQuotaCustomerProductRepository quotaCustomerProductRepository,
            UserSecurityService userSecurityService
    ) {
        this.quotaCustomerProductRepository = quotaCustomerProductRepository;
        this.userSecurityService = userSecurityService;
    }

    public QuotaProductBean getQuotaProductDetails(QuotaProductType quotaProductType) {
        Optional<LandlordQuotaCustomerProduct> quotaCustomerProduct = quotaCustomerProductRepository
                .findFirstFromCustomerByType(userSecurityService.getPrincipalUser().getCustomer(), quotaProductType);

        return quotaCustomerProduct.map(landlordQuotaCustomerProduct ->
                QuotaProductBean
                        .builder()
                        .totalQuota(landlordQuotaCustomerProduct.getTotalQuota())
                        .usedQuota(landlordQuotaCustomerProduct.getUsedQuota())
                        .build()).orElse(QuotaProductBean.builder().totalQuota(0L).usedQuota(0L).build());
    }
}
