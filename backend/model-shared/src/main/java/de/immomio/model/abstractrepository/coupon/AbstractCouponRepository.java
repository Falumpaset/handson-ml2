package de.immomio.model.abstractrepository.coupon;

import de.immomio.data.base.entity.coupon.AbstractCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@RepositoryRestResource(path = "coupons")
public interface AbstractCouponRepository<C extends AbstractCoupon> extends JpaRepository<C, Long> {

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("coupon") C coupon);

    @Override
    @RestResource(exported = false) <T extends C> T save(@Param("coupon") T coupon);

}
