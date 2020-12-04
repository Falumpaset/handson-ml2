package de.immomio.model.repository.service.landlord.customer.ftpaccess;

import de.immomio.model.repository.core.landlord.customer.ftpaccess.BaseFtpAccessRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "ll-ftpAccesses")
public interface FtpAccessRepository extends BaseFtpAccessRepository {

}
