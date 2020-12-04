package de.immomio.model.repository.service.shared.right;

import de.immomio.model.repository.core.shared.right.BaseRightRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "rights")
public interface RightRepository extends BaseRightRepository {

}
