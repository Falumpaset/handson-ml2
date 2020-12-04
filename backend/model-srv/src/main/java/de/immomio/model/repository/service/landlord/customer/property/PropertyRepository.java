package de.immomio.model.repository.service.landlord.customer.property;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-properties")
public interface PropertyRepository extends BasePropertyRepository {

    Optional<Property> findById(@Param("id") Long id);

    @Query("SELECT id from Property")
    List<Long> findAllIds();
}
