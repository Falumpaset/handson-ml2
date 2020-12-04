package de.immomio.model.repository.propertysearcher.product.limitation;

import de.immomio.data.propertysearcher.entity.product.limitation.PropertySearcherProductLimitation;
import de.immomio.model.abstractrepository.product.limitation.AbstractProductLimitationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "productLimitations")
public interface PropertySearcherProductLimitationRepository
        extends AbstractProductLimitationRepository<PropertySearcherProductLimitation> {

}
