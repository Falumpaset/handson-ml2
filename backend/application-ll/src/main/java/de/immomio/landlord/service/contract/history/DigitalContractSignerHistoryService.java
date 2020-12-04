package de.immomio.landlord.service.contract.history;

import de.immomio.model.repository.shared.contract.signer.DigitalContractSignerHistoryRepository;
import de.immomio.model.repository.shared.contract.signer.DigitalContractSignerRepository;
import de.immomio.service.shared.contract.AbstractDigitalContractSignerHistoryService;
import org.springframework.stereotype.Service;

/**
 * @author Andreas Hansen
 */
@Service
public class DigitalContractSignerHistoryService
        extends AbstractDigitalContractSignerHistoryService<DigitalContractSignerHistoryRepository, DigitalContractSignerRepository> {

    public DigitalContractSignerHistoryService(
            DigitalContractSignerHistoryRepository signerHistoryRepository,
            DigitalContractSignerRepository signerRepository
    ) {
        super(signerHistoryRepository, signerRepository);
    }

}
