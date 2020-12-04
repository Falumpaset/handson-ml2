package de.immomio.model.repository.core.propertysearcher.product;

import de.immomio.data.propertysearcher.entity.product.PropertySearcherProduct;
import de.immomio.model.abstractrepository.product.BaseAbstractProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "products")
public interface BasePropertySearcherProductRepository extends BaseAbstractProductRepository<PropertySearcherProduct> {

}
