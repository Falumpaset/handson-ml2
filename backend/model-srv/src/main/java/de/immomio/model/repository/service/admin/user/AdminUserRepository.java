package de.immomio.model.repository.service.admin.user;

import de.immomio.model.repository.core.admin.user.BaseAdminUserRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Kingma
 */
@RepositoryRestResource(path = "adm-users")
public interface AdminUserRepository extends BaseAdminUserRepository {
}
