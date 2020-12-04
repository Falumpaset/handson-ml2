package de.immomio.model.repository.core.shared.contract.signer.aes;

import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpHistory;
import de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseDigitalContractItpHistoryRepository extends JpaRepository<DigitalContractItpHistory, Long> {

    Long countBySignerAndState(DigitalContractSigner signer, DigitalContractItpState state);
}
