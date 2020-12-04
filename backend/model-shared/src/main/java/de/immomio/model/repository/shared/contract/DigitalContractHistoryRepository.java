package de.immomio.model.repository.shared.contract;

import de.immomio.model.repository.core.shared.contract.BaseDigitalContractHistoryRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface DigitalContractHistoryRepository extends BaseDigitalContractHistoryRepository {
}
