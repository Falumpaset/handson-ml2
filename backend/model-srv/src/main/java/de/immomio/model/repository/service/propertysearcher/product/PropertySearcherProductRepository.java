package de.immomio.model.repository.service.propertysearcher.product;

import de.immomio.model.repository.core.propertysearcher.product.BasePropertySearcherProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ps-products")
public interface PropertySearcherProductRepository extends BasePropertySearcherProductRepository {

}
