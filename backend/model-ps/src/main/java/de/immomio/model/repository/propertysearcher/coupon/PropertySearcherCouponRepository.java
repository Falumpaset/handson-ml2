package de.immomio.model.repository.propertysearcher.coupon;

import de.immomio.data.propertysearcher.entity.coupon.PropertySearcherCoupon;
import de.immomio.model.abstractrepository.coupon.AbstractCouponRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "coupons")
public interface PropertySearcherCouponRepository extends AbstractCouponRepository<PropertySearcherCoupon> {

}
