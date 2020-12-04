package de.immomio.model.repository.landlord.customer.property.portal;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.portal.PropertyPortal;
import de.immomio.model.repository.core.landlord.customer.property.portal.BaseLandlordPropertyPortalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "propertyPortals")
public interface LandlordPropertyPortalRepository extends BaseLandlordPropertyPortalRepository {

    @Override
    @Query("SELECT o FROM #{#entityName} o INNER JOIN o.property p WHERE o.id = :id AND p.customer.id = ?#{principal.customer.id}")
    Optional<PropertyPortal> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o INNER JOIN o.property p WHERE p.customer.id = ?#{principal.customer.id}")
    Page<PropertyPortal> findAll(Pageable pageable);

    @Override
    @PreAuthorize("#propertyPortal.property.customer == principal?.customer")
    PropertyPortal save(@Param("propertyPortal") PropertyPortal propertyPortal);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("#propertyPortal.property.customer == principal?.customer")
    void delete(@Param("propertyPortal") PropertyPortal propertyPortal);

    @RestResource(exported = false)
    List<PropertyPortal> findAllByProperty(Property property);
}
