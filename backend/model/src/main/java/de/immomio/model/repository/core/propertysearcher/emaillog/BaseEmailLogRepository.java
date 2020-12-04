package de.immomio.model.repository.core.propertysearcher.emaillog;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.email.EmailLog;
import de.immomio.data.propertysearcher.entity.user.email.MailEventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

public interface BaseEmailLogRepository extends JpaRepository<EmailLog, Long> {

    @Override
    @RestResource(exported = false)
    EmailLog save(@Param("email_log") EmailLog emailLog);

    @Override
    @RestResource(exported = false)
    void deleteById(Long userId);

    @RestResource(exported = false)
    Optional<EmailLog> findByUserAndEventType(PropertySearcherUser user, MailEventType type);

}
