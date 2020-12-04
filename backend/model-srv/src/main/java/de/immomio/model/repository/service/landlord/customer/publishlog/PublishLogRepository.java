package de.immomio.model.repository.service.landlord.customer.publishlog;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.publish.PublishLog;
import de.immomio.model.repository.core.landlord.customer.property.publishlog.BaseLandlordPublishLogRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author Niklas Lindemann
 */

@RepositoryRestResource(path = "ll-publishlogs")
public interface PublishLogRepository extends BaseLandlordPublishLogRepository {
    List<PublishLog> findByProperty(Property property);
}
