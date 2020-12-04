package de.immomio.model.repository.service.landlord.coupon.usage;

import de.immomio.model.repository.core.landlord.couponusage.BaseCouponUsageRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "ll-couponUsages")
public interface CouponUsageRepository extends BaseCouponUsageRepository {

}
