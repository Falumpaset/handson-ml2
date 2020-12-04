package de.immomio.model.repository.service.landlord.discount;

import de.immomio.data.landlord.entity.discount.LandlordDiscount;
import de.immomio.model.repository.core.landlord.discount.BaseLandlordDiscountRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.Optional;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-discounts")
public interface DiscountRepository extends BaseLandlordDiscountRepository {

    Optional<LandlordDiscount> findFirstByEndDateAndValue(Date endDate, Double value);

}
