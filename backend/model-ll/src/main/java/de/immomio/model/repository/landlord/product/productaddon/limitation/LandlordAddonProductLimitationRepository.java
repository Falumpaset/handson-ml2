package de.immomio.model.repository.landlord.product.productaddon.limitation;

import de.immomio.data.landlord.entity.product.addon.limitation.LandlordAddonProductLimitation;
import de.immomio.model.abstractrepository.product.limitation.AbstractAddonProductLimitationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */
@RepositoryRestResource(path = "addonProductLimitations")
public interface LandlordAddonProductLimitationRepository
        extends AbstractAddonProductLimitationRepository<LandlordAddonProductLimitation> {

}
