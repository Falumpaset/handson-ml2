package de.immomio.model.repository.service.landlord.product.addon.limitation;

import de.immomio.model.repository.core.landlord.product.productaddon.limitation.BaseAddonProductLimitationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-addonProductLimitations")
public interface AddonProductLimitationRepository extends BaseAddonProductLimitationRepository {

}
