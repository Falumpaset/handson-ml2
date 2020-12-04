package de.immomio.model.abstractrepository.coupon.usage;

import de.immomio.data.base.entity.couponusage.AbstractCouponUsage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.persistence.MappedSuperclass;
import java.util.Optional;

@MappedSuperclass
@RepositoryRestResource(path = "couponUsages")
public interface AbstractCouponUsageRepository<CU extends AbstractCouponUsage>
        extends PagingAndSortingRepository<CU, Long> {

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = :id AND o.user.id = ?#{principal.id}")
    Optional<CU> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.user.id = ?#{principal.id}")
    Page<CU> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    @PreAuthorize("#couponUsage?.user?.id == principal?.id")
    void delete(@Param("couponUsage") CU couponUsage);

    @Override
    @RestResource(exported = false)
    @PreAuthorize("#couponUsage?.user?.id == principal?.id") <T extends CU> T save(@Param("couponUsage") T couponUsage);
}
