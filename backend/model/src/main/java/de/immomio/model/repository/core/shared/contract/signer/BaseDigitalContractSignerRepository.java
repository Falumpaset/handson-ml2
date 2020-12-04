package de.immomio.model.repository.core.shared.contract.signer;

import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author Andreas Hansen
 */

@RepositoryRestResource(exported = false)
public interface BaseDigitalContractSignerRepository extends JpaRepository<DigitalContractSigner, Long> {

    List<DigitalContractSigner> findByDigitalContract(DigitalContract digitalContract);

    @Query("select s from DigitalContractSigner s " +
            "where function('jsonb_extract_path_text', s.currentState, 'itpState') in (:itpStates) and s.digitalContract.currentState <> 'CONTRACT_DELETED'")
    List<DigitalContractSigner> findSignersWithAesItpState(List<String> itpStates);

}
