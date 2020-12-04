package de.immomio.model.repository.propertysearcher.discount;

import de.immomio.data.propertysearcher.entity.discount.PropertySearcherDiscount;
import de.immomio.model.abstractrepository.discount.AbstractDiscountRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "discounts")
public interface PropertySearcherDiscountRepository extends AbstractDiscountRepository<PropertySearcherDiscount> {
}
