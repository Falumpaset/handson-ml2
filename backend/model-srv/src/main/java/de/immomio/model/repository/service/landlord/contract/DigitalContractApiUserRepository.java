package de.immomio.model.repository.service.landlord.contract;

import de.immomio.model.repository.core.shared.contract.BaseDigitalContractApiUserRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface DigitalContractApiUserRepository extends BaseDigitalContractApiUserRepository {
}
