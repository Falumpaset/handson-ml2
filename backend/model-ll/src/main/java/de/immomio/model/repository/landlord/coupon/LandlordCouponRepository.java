package de.immomio.model.repository.landlord.coupon;

import de.immomio.data.landlord.entity.coupon.LandlordCoupon;
import de.immomio.model.abstractrepository.coupon.AbstractCouponRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Kingma
 */
@RepositoryRestResource(path = "coupons")
public interface LandlordCouponRepository extends AbstractCouponRepository<LandlordCoupon> {

}
