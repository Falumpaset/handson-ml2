package de.immomio.model.repository.propertysearcher.product;

import de.immomio.data.propertysearcher.entity.product.PropertySearcherProduct;
import de.immomio.model.abstractrepository.product.AbstractProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "products")
public interface PropertySearcherProductRepository extends AbstractProductRepository<PropertySearcherProduct> {

}
