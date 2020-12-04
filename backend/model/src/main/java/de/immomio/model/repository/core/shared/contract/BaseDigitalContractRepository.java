package de.immomio.model.repository.core.shared.contract;

import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.history.DigitalContractHistoryState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author Andreas Hansen
 */

@RepositoryRestResource(exported = false)
public interface BaseDigitalContractRepository extends JpaRepository<DigitalContract, Long> {

    @Query("select dc from DigitalContract as dc where dc.docuSignEnvelopeId = :envelopeId")
    DigitalContract customFindByDocuSignEnvelopeId(@Param(("envelopeId")) UUID envelopeId);

    List<DigitalContract> findByCurrentStateIn(Set<DigitalContractHistoryState> states);

    @Query("select dc from DigitalContract as dc where dc.currentState = 'DOCUSIGN_COMPLETED' and dc.signedDocumentArchiveFile is null and dc.created >= :creationDateTime")
    Page<DigitalContract> customFindByCurrentStateAndSignedDocumentArchiveFileIsNull(@Param("creationDateTime") Date creationDateTime, Pageable pageable);

}
