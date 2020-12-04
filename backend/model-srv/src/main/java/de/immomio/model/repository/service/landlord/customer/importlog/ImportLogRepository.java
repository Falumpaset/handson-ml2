package de.immomio.model.repository.service.landlord.customer.importlog;

import de.immomio.model.repository.core.landlord.customer.importlog.BaseImportLogRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "ll-importLogs")
public interface ImportLogRepository extends BaseImportLogRepository {

}
