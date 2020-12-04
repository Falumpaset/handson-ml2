package de.immomio.model.repository.service.propertysearcher.customer.discount;

import de.immomio.model.repository.core.propertysearcher.discount.BasePropertySearcherDiscountRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ps-discounts")
public interface PropertySearcherDiscountRepository extends BasePropertySearcherDiscountRepository {

}
