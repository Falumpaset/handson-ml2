package de.immomio.model.repository.service.propertysearcher.coupon.usage;

import de.immomio.model.repository.core.propertysearcher.coupon.BasePropertySearcherCouponUsageRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "ps-couponUsages")
public interface PropertySearcherCouponUsageRepository extends BasePropertySearcherCouponUsageRepository {

}
