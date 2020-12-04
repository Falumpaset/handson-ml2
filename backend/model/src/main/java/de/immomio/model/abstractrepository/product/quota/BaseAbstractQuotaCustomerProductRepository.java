package de.immomio.model.abstractrepository.product.quota;

import de.immomio.data.base.entity.customer.AbstractCustomer;
import de.immomio.data.base.entity.product.quota.AbstractQuotaCustomerProduct;
import de.immomio.data.base.type.product.quota.QuotaProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseAbstractQuotaCustomerProductRepository
        <QCP extends AbstractQuotaCustomerProduct<C>, C extends AbstractCustomer<?, ?>> extends JpaRepository<QCP, Long> {

    Optional<QCP> findFirstFromCustomerByType(C customer, QuotaProductType type);
}
