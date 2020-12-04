package de.immomio.model.repository.core.landlord.product.quota;

import de.immomio.data.base.type.product.quota.QuotaProductType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.quota.LandlordQuotaCustomerProduct;
import de.immomio.model.abstractrepository.product.quota.BaseAbstractQuotaCustomerProductRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseLandlordQuotaCustomerProductRepository
        extends BaseAbstractQuotaCustomerProductRepository<LandlordQuotaCustomerProduct, LandlordCustomer> {

    @Override
    @Query("SELECT q from LandlordQuotaCustomerProduct q where q.customer = :customer and q.type = :type")
    Optional<LandlordQuotaCustomerProduct> findFirstFromCustomerByType(@Param("customer") LandlordCustomer customer, @Param("type") QuotaProductType type);
}
