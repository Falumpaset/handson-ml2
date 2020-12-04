package de.immomio.model.repository.service.shared.application;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.core.shared.application.BasePropertyApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "applications")
public interface PropertyApplicationRepository extends BasePropertyApplicationRepository {

    List<PropertyApplication> findByUserProfileAndProperty(PropertySearcherUserProfile userProfile, Property property);

    @Override
    @RestResource(exported = false)
    PropertyApplication save(PropertyApplication propertyApplication);

    @Override
    Page<PropertyApplication> findByUserProfile(@Param("userProfile") PropertySearcherUserProfile userProfile, Pageable pageable);

    @Override
    Page<PropertyApplication> findByProperty(@Param("property") Property property, Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    void delete(@Param("application") PropertyApplication propertyApplication);

    @Override
    @RestResource(exported = false)
    void deleteAll();

    @RestResource(exported = false)
    List<PropertyApplication> findByPropertyId(Long propertyId);

}
