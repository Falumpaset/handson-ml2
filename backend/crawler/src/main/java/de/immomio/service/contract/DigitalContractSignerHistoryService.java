package de.immomio.service.contract;

import de.immomio.model.repository.core.shared.contract.signer.BaseDigitalContractSignerHistoryRepository;
import de.immomio.model.repository.core.shared.contract.signer.BaseDigitalContractSignerRepository;
import de.immomio.service.shared.contract.AbstractDigitalContractSignerHistoryService;
import org.springframework.stereotype.Service;

/**
 * @author Andreas Hansen
 */
@Service
public class DigitalContractSignerHistoryService
        extends AbstractDigitalContractSignerHistoryService<BaseDigitalContractSignerHistoryRepository, BaseDigitalContractSignerRepository> {

    public DigitalContractSignerHistoryService(
            BaseDigitalContractSignerHistoryRepository signerHistoryRepository,
            BaseDigitalContractSignerRepository signerRepository
    ) {
        super(signerHistoryRepository, signerRepository);
    }

}
