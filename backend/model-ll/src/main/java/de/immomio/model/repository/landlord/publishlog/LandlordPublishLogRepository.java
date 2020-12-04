package de.immomio.model.repository.landlord.publishlog;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.publish.PublishLog;
import de.immomio.model.repository.core.landlord.customer.property.publishlog.BaseLandlordPublishLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * @author Niklas Lindemann
 */
@RepositoryRestResource(path = "publishLogs")
public interface LandlordPublishLogRepository extends BaseLandlordPublishLogRepository {

    @Override
    @RestResource(exported = false)
    @Query("SELECT o FROM #{#entityName} o WHERE o.customer.id = ?#{principal.customer.id} order by created desc")
    Page<PublishLog> findAll(Pageable pageable);

    @PreAuthorize("#property.customer.id == principal.customer.id")
    @RestResource(exported = false)
    @Query("SELECT o FROM #{#entityName} o WHERE o.property = :property order by created desc")
    List<PublishLog> findByProperty(@Param("property") Property property);

    @Override
    @RestResource(exported = false)
    <S extends PublishLog> S save(S entity);

    @Override
    @RestResource(exported = false)
    void delete(PublishLog entity);
}
