package de.immomio.model.repository.core.landlord.customer.property.publishlog;

import de.immomio.data.landlord.entity.property.publish.PublishLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister, Maik Kingma
 */
@RepositoryRestResource(path = "publishLogs")
public interface BaseLandlordPublishLogRepository extends JpaRepository<PublishLog, Long> {

}
