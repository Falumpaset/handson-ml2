package de.immomio.model.repository.landlord.product.addon;

import de.immomio.model.repository.core.landlord.product.addon.BaseAddonProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */
@RepositoryRestResource(path = "addonProducts")
public interface LandlordAddonProductRepository extends BaseAddonProductRepository {

}
