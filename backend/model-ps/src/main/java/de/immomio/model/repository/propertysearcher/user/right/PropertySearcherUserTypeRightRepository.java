package de.immomio.model.repository.propertysearcher.user.right;

import de.immomio.data.propertysearcher.entity.user.right.PropertySearcherUsertypeRight;
import de.immomio.model.repository.core.propertysearcher.user.right.usertype.BasePropertySearcherUserTypeRightRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface PropertySearcherUserTypeRightRepository extends BasePropertySearcherUserTypeRightRepository {

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("userTypeRight") PropertySearcherUsertypeRight userTypeRight);

    @Override
    @RestResource(exported = false) <T extends PropertySearcherUsertypeRight> T save(
            @Param("userTypeRight") T userTypeRight);

}
