package de.immomio.model.repository.service.propertysearcher.product.addon.limitation;

import de.immomio.model.repository.core.propertysearcher.product.addon.limitation.BasePropertySearcherAddonProductLimitationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ps-addonProductLimitations")
public interface PropertySearcherAddonProductLimitationRepository
        extends BasePropertySearcherAddonProductLimitationRepository {

}
