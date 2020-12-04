package de.immomio.model.repository.core.landlord.product.quota;

import de.immomio.data.landlord.entity.product.quota.LandlordQuotaUsage;
import de.immomio.model.abstractrepository.product.quota.BaseAbstractQuotaUsageRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseLandlordQuotaUsageRepository extends BaseAbstractQuotaUsageRepository<LandlordQuotaUsage> {
}
