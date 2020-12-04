package de.immomio.model.repository.landlord.product.basket.quota;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.basket.quota.LandlordQuotaBasket;
import de.immomio.model.abstractrepository.product.quota.BaseAbstractQuotaBasketRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface LandlordQuotaBasketRepository extends BaseAbstractQuotaBasketRepository<LandlordQuotaBasket, LandlordCustomer> {
}
