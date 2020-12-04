package de.immomio.model.repository.shared.contract.signer;

import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.model.repository.core.shared.contract.signer.BaseDigitalContractSignerRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface DigitalContractSignerRepository extends BaseDigitalContractSignerRepository {

    List<DigitalContractSigner> findByDigitalContract(@Param("digitalContract") DigitalContract digitalContract);

    Optional<DigitalContractSigner> findByInternalSignerId(@Param("internalSignerId") UUID internalSignerId);

}
