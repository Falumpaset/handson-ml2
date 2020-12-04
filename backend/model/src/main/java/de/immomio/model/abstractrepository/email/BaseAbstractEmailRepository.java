package de.immomio.model.abstractrepository.email;

import de.immomio.data.base.entity.email.AbstractEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "emails")
public interface BaseAbstractEmailRepository<E extends AbstractEmail<?, ?>> extends JpaRepository<E, Long> {

}
