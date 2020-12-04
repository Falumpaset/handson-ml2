package de.immomio.model.repository.core.shared.contract.signer.aes;

import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.data.shared.entity.contract.signer.history.aes.schufa.DigitalContractSchufaHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseDigitalContractSchufaHistoryRepository extends JpaRepository<DigitalContractSchufaHistory, Long> {

    @Query("SELECT count(h) FROM DigitalContractSchufaHistory h WHERE h.signer = :signer and h.schufaRequest is not null and h.state <> 'ERROR'")
    Long countSchufaTries(@Param("signer") DigitalContractSigner signer);
}
