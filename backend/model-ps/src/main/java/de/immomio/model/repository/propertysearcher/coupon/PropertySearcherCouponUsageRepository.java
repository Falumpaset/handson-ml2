package de.immomio.model.repository.propertysearcher.coupon;

import de.immomio.data.propertysearcher.entity.coupon.PropertySearcherCouponUsage;
import de.immomio.model.abstractrepository.coupon.usage.AbstractCouponUsageRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "couponUsages")
public interface PropertySearcherCouponUsageRepository
        extends AbstractCouponUsageRepository<PropertySearcherCouponUsage> {

}
