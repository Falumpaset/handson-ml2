package de.immomio.model.repository.landlord.discount;

import de.immomio.data.landlord.entity.discount.LandlordDiscount;
import de.immomio.model.abstractrepository.discount.AbstractDiscountRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 * <p>
 * example post body:
 * <p>
 * { "name": "Rabatt: Thomas Clotten", "startDate": "2017-01-10T15:11:25.562", "endDate": "2017-01-11T15:11:25.562",
 * "value": 0.35 }
 */
@RepositoryRestResource(path = "discounts")
public interface LandlordDiscountRepository extends AbstractDiscountRepository<LandlordDiscount> {

}
