package de.immomio.model.repository.service.propertysearcher.email;

import de.immomio.model.repository.core.propertysearcher.email.BasePropertySearcherEmailRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ps-emails")
public interface PropertySearcherEmailRepository extends BasePropertySearcherEmailRepository {

}
