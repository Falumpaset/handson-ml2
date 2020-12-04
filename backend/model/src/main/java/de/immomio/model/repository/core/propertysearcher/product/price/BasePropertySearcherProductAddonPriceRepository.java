package de.immomio.model.repository.core.propertysearcher.product.price;

import de.immomio.data.propertysearcher.entity.product.price.PropertySearcherProductAddonPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasePropertySearcherProductAddonPriceRepository
        extends JpaRepository<PropertySearcherProductAddonPrice, Long> {

}
