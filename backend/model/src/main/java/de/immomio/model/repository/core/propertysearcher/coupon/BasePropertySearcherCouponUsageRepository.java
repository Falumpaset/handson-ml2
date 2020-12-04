package de.immomio.model.repository.core.propertysearcher.coupon;

import de.immomio.data.propertysearcher.entity.coupon.PropertySearcherCouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasePropertySearcherCouponUsageRepository extends JpaRepository<PropertySearcherCouponUsage, Long> {
}
