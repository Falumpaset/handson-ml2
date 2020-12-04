package de.immomio.model.repository.landlord.product;

import de.immomio.model.repository.core.landlord.product.BaseLandlordProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "products")
public interface LandlordProductRepository
        extends BaseLandlordProductRepository, LandlordProductRepositoryCustom {
}
