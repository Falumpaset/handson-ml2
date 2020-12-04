package de.immomio.model.repository.core.propertysearcher.coupon;

import de.immomio.data.propertysearcher.entity.coupon.PropertySearcherCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasePropertySearcherCouponRepository extends JpaRepository<PropertySearcherCoupon, Long> {
}
