package de.immomio.service.contract;

import de.immomio.model.repository.shared.contract.signer.DigitalContractSignerHistoryRepository;
import de.immomio.model.repository.shared.contract.signer.DigitalContractSignerRepository;
import de.immomio.service.shared.contract.AbstractDigitalContractSignerHistoryService;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
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
