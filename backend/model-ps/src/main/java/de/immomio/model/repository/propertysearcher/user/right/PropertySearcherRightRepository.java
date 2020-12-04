/**
 *
 */
package de.immomio.model.repository.propertysearcher.user.right;

import de.immomio.data.propertysearcher.entity.user.right.PropertySearcherRight;
import de.immomio.model.repository.core.propertysearcher.user.right.BasePropertySearcherRightRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface PropertySearcherRightRepository extends BasePropertySearcherRightRepository {

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("right") PropertySearcherRight right);

    @Override
    @RestResource(exported = false) <T extends PropertySearcherRight> T save(@Param("right") T right);
}
