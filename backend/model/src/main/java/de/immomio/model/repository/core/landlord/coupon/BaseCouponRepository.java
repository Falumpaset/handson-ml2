package de.immomio.model.repository.core.landlord.coupon;

import de.immomio.data.landlord.entity.coupon.LandlordCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Kingma
 */
@RepositoryRestResource(path = "coupons")
public interface BaseCouponRepository extends JpaRepository<LandlordCoupon, Long> {

}
