package de.immomio.model.repository.core.propertysearcher.product.limitation;

import de.immomio.data.propertysearcher.entity.product.limitation.PropertySearcherProductLimitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasePropertySearcherProductLimitationRepository
        extends JpaRepository<PropertySearcherProductLimitation, Long> {

}
