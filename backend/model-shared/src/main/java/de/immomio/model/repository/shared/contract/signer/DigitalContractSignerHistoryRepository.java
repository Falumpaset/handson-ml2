package de.immomio.model.repository.shared.contract.signer;

import de.immomio.model.repository.core.shared.contract.signer.BaseDigitalContractSignerHistoryRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface DigitalContractSignerHistoryRepository extends BaseDigitalContractSignerHistoryRepository {

}
