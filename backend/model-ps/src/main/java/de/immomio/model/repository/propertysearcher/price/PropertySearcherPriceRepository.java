package de.immomio.model.repository.propertysearcher.price;

import de.immomio.data.propertysearcher.entity.price.PropertySearcherPrice;
import de.immomio.model.abstractrepository.price.AbstractPriceRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "prices")
public interface PropertySearcherPriceRepository extends AbstractPriceRepository<PropertySearcherPrice> {
}
