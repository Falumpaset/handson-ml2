package de.immomio.model.repository.service.landlord.product;

import de.immomio.model.repository.core.landlord.product.BaseLandlordProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-products")
public interface LandlordProductRepository extends BaseLandlordProductRepository {

}
