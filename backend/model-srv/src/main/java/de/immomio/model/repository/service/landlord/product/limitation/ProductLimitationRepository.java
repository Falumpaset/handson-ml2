package de.immomio.model.repository.service.landlord.product.limitation;

import de.immomio.model.repository.core.landlord.product.limitation.BaseProductLimitationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-productLimitations")
public interface ProductLimitationRepository extends BaseProductLimitationRepository {

}
