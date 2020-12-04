package de.immomio.model.repository.core.landlord.discount;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.discount.LandlordCustomerQuotaPackageDiscount;
import de.immomio.data.landlord.entity.product.quota.LandlordQuotaPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Maik Kingma
 */
@Repository
public interface BaseLandlordCustomerQuotaPackageDiscountRepository
        extends JpaRepository<LandlordCustomerQuotaPackageDiscount, Long> {

    List<LandlordCustomerQuotaPackageDiscount> findAllByCustomer(LandlordCustomer customer);

    @Query("SELECT q from LandlordCustomerQuotaPackageDiscount q " +
            "where q.customer = :customer and q.quotaPackage = :quotaPackage " +
            "AND q.discount.startDate < CURRENT_TIMESTAMP and q.discount.endDate > CURRENT_TIMESTAMP")
    Optional<LandlordCustomerQuotaPackageDiscount> findFirstByCustomerAndQuotaPackage(
            @Param("customer") LandlordCustomer customer,
            @Param("quotaPackage") LandlordQuotaPackage quotaPackage);
}
