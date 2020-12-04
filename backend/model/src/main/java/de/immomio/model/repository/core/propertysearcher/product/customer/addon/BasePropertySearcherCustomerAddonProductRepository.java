package de.immomio.model.repository.core.propertysearcher.product.customer.addon;

import de.immomio.data.propertysearcher.entity.customer.product.addon.PropertySearcherCustomerAddonProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasePropertySearcherCustomerAddonProductRepository
        extends JpaRepository<PropertySearcherCustomerAddonProduct, Long> {

}
