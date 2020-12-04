package de.immomio.model.repository.core.propertysearcher.user.right;

import de.immomio.data.propertysearcher.entity.user.right.PropertySearcherRight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface BasePropertySearcherRightRepository extends JpaRepository<PropertySearcherRight, Long> {

    @Override
    void deleteById(@Param("id") Long id);

    @Override
    void delete(@Param("right") PropertySearcherRight right);

    @Override <T extends PropertySearcherRight> T save(@Param("right") T right);
}
