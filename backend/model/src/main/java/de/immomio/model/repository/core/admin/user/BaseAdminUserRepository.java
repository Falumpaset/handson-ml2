package de.immomio.model.repository.core.admin.user;

import de.immomio.model.entity.admin.user.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "adm-users")
public interface BaseAdminUserRepository extends JpaRepository<AdminUser, Long> {

    AdminUser findByEmail(@Param("email") String email);

}