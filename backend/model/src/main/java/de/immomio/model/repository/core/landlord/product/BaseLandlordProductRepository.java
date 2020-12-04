package de.immomio.model.repository.core.landlord.product;

import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.model.abstractrepository.product.BaseAbstractProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Kingma
 */

@RepositoryRestResource(path = "products")
public interface BaseLandlordProductRepository extends BaseAbstractProductRepository<LandlordProduct> {

    @Override
    Page<LandlordProduct> findAll(Pageable pageable);
}
