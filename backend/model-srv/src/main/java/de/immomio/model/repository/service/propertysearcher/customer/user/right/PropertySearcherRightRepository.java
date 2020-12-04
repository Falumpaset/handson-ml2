package de.immomio.model.repository.service.propertysearcher.customer.user.right;

import de.immomio.model.repository.core.propertysearcher.user.right.BasePropertySearcherRightRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-rights")
public interface PropertySearcherRightRepository extends BasePropertySearcherRightRepository {

}
