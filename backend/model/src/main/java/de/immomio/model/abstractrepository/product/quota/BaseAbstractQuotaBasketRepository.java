package de.immomio.model.abstractrepository.product.quota;

import de.immomio.data.base.entity.customer.AbstractCustomer;
import de.immomio.data.base.entity.product.basket.quota.BaseQuotaBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseAbstractQuotaBasketRepository <QB extends BaseQuotaBasket<C ,? >,
        C extends AbstractCustomer<?, ?>>  extends JpaRepository<QB, Long>{
}
