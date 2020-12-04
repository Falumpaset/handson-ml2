package de.immomio.model.repository.shared.contract;

import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.history.DigitalContractHistoryState;
import de.immomio.model.repository.core.shared.contract.BaseDigitalContractRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface DigitalContractRepository extends BaseDigitalContractRepository {

    @PostAuthorize("returnObject?.customer?.id == principal?.customer?.id")
    DigitalContract findByInternalContractId(@Param(("internalContractId")) UUID internalContractId);

    @Query("SELECT d from DigitalContract d where d.customer = ?#{principal.customer} and d.externalId = :externalId")
    Optional<DigitalContract> findFirstByExternalId(@Param("externalId") String externalId);

    @Query("SELECT d from DigitalContract d inner join d.histories h where h.state = 'DOCUSIGN_COMPLETED' and h.created > :timestamp and d.customer.id = ?#{principal.customer.id} ")
    List<DigitalContract> findFinishedAfter(@Param("timestamp") Date date);

    @Query("SELECT d from DigitalContract d where d.updated > :timestamp and d.customer.id = ?#{principal.customer.id}")
    List<DigitalContract> findModifiedAfter(@Param("timestamp") Date date);

    Long countAllByCustomerAndCurrentStateIn(LandlordCustomer customer, List<DigitalContractHistoryState> states);

    @Query("SELECT count(distinct dc.id) "
            + "FROM DigitalContract dc "
            + "         INNER JOIN dc.signers dch "
            + "WHERE dc.customer = :customer "
            + "  AND dch.type = :signerType "
            + "  AND dc.currentState in (:contractStates) "
            + "  AND ((dc.firstSignerType <> :signerType "
            + "        AND function('jsonb_extract_path_text', dch.currentState, 'signerState') IN (:inProgessSignerStates))"
            + "      OR "
            + "       (dc.firstSignerType = :signerType "
            + "        AND function('jsonb_extract_path_text', dch.currentState, 'signerState') IN (:inProgessAndWaitingSignerStates))) ")
    Long countAllByStateForCustomer(@Param("customer") LandlordCustomer customer,
            @Param("signerType") DigitalContractSignerType signerType,
            @Param("inProgessAndWaitingSignerStates") List<String> inProgessAndWaitingSignerStates,
            @Param("inProgessSignerStates") List<String> inProgessSignerStates,
            @Param("contractStates") List<DigitalContractHistoryState> contractStates);

    @Query("SELECT count(distinct dc.id) "
            + "FROM DigitalContract dc "
            + "WHERE dc.customer = :customer "
            + "  AND dc.currentState IN (:states)"
            + "  AND function('jsonb_extract_path_text', dc.agentInfo, 'id') = (:agentId)")
    Long countyByStateForAgent(@Param("customer") LandlordCustomer customer,
            @Param("states") List<DigitalContractHistoryState> states,
            @Param("agentId") String agentId);

    @Query("SELECT count(distinct dc.id) "
            + "FROM DigitalContract dc "
            + "         INNER JOIN dc.signers dch "
            + "WHERE dc.customer = :customer "
            + "  AND dch.type = :signerType "
            + "  AND function('jsonb_extract_path_text', dc.agentInfo, 'id') = (:agentId) "
            + "  AND dc.currentState in (:contractStates) "
            + "  AND ((dc.firstSignerType <> :signerType "
            + "        AND function('jsonb_extract_path_text', dch.currentState, 'signerState') IN (:inProgessSignerStates))"
            + "      OR "
            + "       (dc.firstSignerType = :signerType "
            + "        AND function('jsonb_extract_path_text', dch.currentState, 'signerState') IN (:inProgessAndWaitingSignerStates))) ")
    Long countBySignerTypeAndStateForAgent(@Param("customer") LandlordCustomer customer,
            @Param("signerType") DigitalContractSignerType signerType,
            @Param("agentId") String agentId,
            @Param("inProgessAndWaitingSignerStates") List<String> inProgessAndWaitingSignerStates,
            @Param("inProgessSignerStates") List<String> inProgessSignerStates,
            @Param("contractStates") List<DigitalContractHistoryState> contractStates);
}