package de.immomio.model.repository.core.shared.contract;

import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.history.DigitalContractHistory;
import de.immomio.data.shared.entity.contract.history.DigitalContractHistoryState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Andreas Hansen
 */

@RepositoryRestResource(exported = false)
public interface BaseDigitalContractHistoryRepository extends JpaRepository<DigitalContractHistory, Long> {

    boolean existsDigitalContractHistoryByDigitalContractAndState(DigitalContract digitalContract, DigitalContractHistoryState state);

}
