package de.immomio.model.repository.service.landlord.coupon;

import de.immomio.model.repository.core.landlord.coupon.BaseCouponRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "ll-coupons")
public interface CouponRepository extends BaseCouponRepository {

}
