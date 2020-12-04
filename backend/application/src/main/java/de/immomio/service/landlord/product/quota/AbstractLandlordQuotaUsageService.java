package de.immomio.service.landlord.product.quota;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.product.quota.QuotaProductType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.quota.LandlordQuotaCustomerProduct;
import de.immomio.data.landlord.entity.product.quota.LandlordQuotaUsage;
import de.immomio.model.repository.core.landlord.product.quota.BaseLandlordQuotaCustomerProductRepository;
import de.immomio.model.repository.core.landlord.product.quota.BaseLandlordQuotaUsageRepository;

import java.util.Optional;

/**
 * @author Niklas Lindemann
 */
public abstract class AbstractLandlordQuotaUsageService<QUR extends BaseLandlordQuotaUsageRepository,
        QCPR extends BaseLandlordQuotaCustomerProductRepository> {

    private QUR quotaUsageRepository;

    private QCPR quotaCustomerProductRepository;

    public AbstractLandlordQuotaUsageService(QUR quotaUsageRepository, QCPR quotaCustomerProductRepository) {
        this.quotaUsageRepository = quotaUsageRepository;
        this.quotaCustomerProductRepository = quotaCustomerProductRepository;
    }


    public void quotaUsed(QuotaProductType type) {
        LandlordCustomer customer = getCustomer();
        LandlordQuotaUsage quotaUsage = new LandlordQuotaUsage();
        quotaUsage.setQuotaCustomerProduct(getLandlordQuotaCustomerProduct(customer, type));
        quotaUsageRepository.save(quotaUsage);
    }

    public void validateQuota(QuotaProductType type) {
        LandlordCustomer customer = getCustomer();
        LandlordQuotaCustomerProduct quotaCustomerProduct = getLandlordQuotaCustomerProduct(customer, type);
        if (quotaCustomerProduct.getAvailableQuota() < 1) {
            throw new ApiValidationException("QUOTA_LIMIT_EXCEEDED_L");
        }
    }

    private LandlordQuotaCustomerProduct getLandlordQuotaCustomerProduct(LandlordCustomer customer, QuotaProductType quotaProductType) {
        Optional<LandlordQuotaCustomerProduct> quotaCustomerProduct = quotaCustomerProductRepository.findFirstFromCustomerByType(customer, quotaProductType);
        return quotaCustomerProduct.orElseGet(() -> {
            LandlordQuotaCustomerProduct newQuotaCustomerProduct = new LandlordQuotaCustomerProduct();
            newQuotaCustomerProduct.setCustomer(customer);
            newQuotaCustomerProduct.setType(quotaProductType);
            return quotaCustomerProductRepository.save(newQuotaCustomerProduct);
        });
    }

    protected abstract LandlordCustomer getCustomer();
}
