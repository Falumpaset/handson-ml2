package de.immomio.model.repository.core.propertysearcher.user.right.usertype;

import de.immomio.data.base.type.user.PropertySearcherUserRightType;
import de.immomio.data.propertysearcher.entity.user.right.PropertySearcherUsertypeRight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BasePropertySearcherUserTypeRightRepository
        extends JpaRepository<PropertySearcherUsertypeRight, Long> {

    List<PropertySearcherUsertypeRight> findAllByUserType(PropertySearcherUserRightType userType);

}
