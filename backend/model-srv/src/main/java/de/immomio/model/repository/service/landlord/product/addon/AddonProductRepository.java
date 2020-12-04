package de.immomio.model.repository.service.landlord.product.addon;

import de.immomio.model.repository.core.landlord.product.addon.BaseAddonProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-addonProducts")
public interface AddonProductRepository extends BaseAddonProductRepository {

}
