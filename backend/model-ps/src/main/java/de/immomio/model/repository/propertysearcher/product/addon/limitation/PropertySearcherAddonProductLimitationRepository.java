package de.immomio.model.repository.propertysearcher.product.addon.limitation;

import de.immomio.data.propertysearcher.entity.product.addon.limitation.PropertySearcherAddonProductLimitation;
import de.immomio.model.abstractrepository.product.limitation.AbstractAddonProductLimitationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "addonProductLimitations")
public interface PropertySearcherAddonProductLimitationRepository
        extends AbstractAddonProductLimitationRepository<PropertySearcherAddonProductLimitation> {

}
