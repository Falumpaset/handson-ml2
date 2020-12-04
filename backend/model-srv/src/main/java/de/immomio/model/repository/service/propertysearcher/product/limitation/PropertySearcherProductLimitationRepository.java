package de.immomio.model.repository.service.propertysearcher.product.limitation;

import de.immomio.model.repository.core.propertysearcher.product.limitation.BasePropertySearcherProductLimitationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ps-productLimitations")
public interface PropertySearcherProductLimitationRepository extends BasePropertySearcherProductLimitationRepository {

}
