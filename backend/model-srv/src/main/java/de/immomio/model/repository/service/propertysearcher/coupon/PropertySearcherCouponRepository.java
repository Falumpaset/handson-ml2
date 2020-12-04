package de.immomio.model.repository.service.propertysearcher.coupon;

import de.immomio.model.repository.core.propertysearcher.coupon.BasePropertySearcherCouponRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "ps-coupons")
public interface PropertySearcherCouponRepository extends BasePropertySearcherCouponRepository {

}
