package de.immomio.model.repository.core.shared.contract.signer;

import de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Andreas Hansen
 */

@RepositoryRestResource(exported = false)
public interface BaseDigitalContractSignerHistoryRepository extends JpaRepository<DigitalContractSignerHistory, Long> {

}
