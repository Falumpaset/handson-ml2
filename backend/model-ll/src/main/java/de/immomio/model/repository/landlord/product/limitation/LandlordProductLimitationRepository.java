package de.immomio.model.repository.landlord.product.limitation;

import de.immomio.data.landlord.entity.product.limitation.LandlordProductLimitation;
import de.immomio.model.abstractrepository.product.limitation.AbstractProductLimitationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */
@RepositoryRestResource(path = "productLimitations")
public interface LandlordProductLimitationRepository
        extends AbstractProductLimitationRepository<LandlordProductLimitation> {

}
