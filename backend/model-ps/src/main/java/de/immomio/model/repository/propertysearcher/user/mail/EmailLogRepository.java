package de.immomio.model.repository.propertysearcher.user.mail;


import de.immomio.model.repository.core.propertysearcher.emaillog.BaseEmailLogRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "email_logs")
public interface EmailLogRepository extends BaseEmailLogRepository {

}
