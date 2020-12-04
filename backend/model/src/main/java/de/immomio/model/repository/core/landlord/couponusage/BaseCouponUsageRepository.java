package de.immomio.model.repository.core.landlord.couponusage;

import de.immomio.data.landlord.entity.couponusage.LandlordCouponUsage;
import de.immomio.data.landlord.entity.user.LandlordUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author Maik Kingma
 */
@RepositoryRestResource(path = "couponUsages")
public interface BaseCouponUsageRepository extends JpaRepository<LandlordCouponUsage, Long> {

    List<LandlordCouponUsage> findByUser(LandlordUser user);
}
