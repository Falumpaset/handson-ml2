package de.immomio.model.repository.core.landlord.product.quota;

import de.immomio.data.landlord.entity.product.quota.LandlordQuota;
import de.immomio.model.abstractrepository.product.quota.BaseAbstractQuotaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Niklas Lindemann
 */

@Repository
public interface BaseLandlordQuotaRepository extends BaseAbstractQuotaRepository<LandlordQuota> {
}
