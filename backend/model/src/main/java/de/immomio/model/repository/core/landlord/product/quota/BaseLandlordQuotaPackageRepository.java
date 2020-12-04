package de.immomio.model.repository.core.landlord.product.quota;

import de.immomio.data.base.type.product.quota.QuotaProductType;
import de.immomio.data.landlord.entity.product.quota.LandlordQuotaPackage;
import de.immomio.model.abstractrepository.product.quota.BaseAbstractQuotaPackageRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseLandlordQuotaPackageRepository extends BaseAbstractQuotaPackageRepository<LandlordQuotaPackage> {
    List<LandlordQuotaPackage> findAllByType(QuotaProductType type);
}
