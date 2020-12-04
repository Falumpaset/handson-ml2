package de.immomio.model.repository.service.propertysearcher.product.addon;

import de.immomio.model.repository.core.propertysearcher.product.addon.BasePropertySearcherAddonProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ps-addonProducts")
public interface PropertySearcherAddonProductRepository extends BasePropertySearcherAddonProductRepository {

}
