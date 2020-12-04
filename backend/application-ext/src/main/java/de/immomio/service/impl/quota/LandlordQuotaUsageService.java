package de.immomio.service.impl.quota;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.model.repository.landlord.product.quota.LandlordQuotaCustomerProductRepository;
import de.immomio.model.repository.landlord.product.quota.LandlordQuotaUsageRepository;
import de.immomio.service.impl.UserSecurityService;
import de.immomio.service.landlord.product.quota.AbstractLandlordQuotaUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Service
public class LandlordQuotaUsageService extends AbstractLandlordQuotaUsageService<LandlordQuotaUsageRepository, LandlordQuotaCustomerProductRepository> {

    private final UserSecurityService userSecurityService;

    @Autowired
    public LandlordQuotaUsageService(
            LandlordQuotaUsageRepository quotaUsageRepository,
            LandlordQuotaCustomerProductRepository quotaCustomerProductRepository,
            UserSecurityService userSecurityService
    ) {
        super(quotaUsageRepository, quotaCustomerProductRepository);
        this.userSecurityService = userSecurityService;
    }

    protected LandlordCustomer getCustomer() {
        return userSecurityService.getPrincipalUser().getCustomer();
    }

}
