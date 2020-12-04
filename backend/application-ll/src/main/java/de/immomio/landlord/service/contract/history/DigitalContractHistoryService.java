package de.immomio.landlord.service.contract.history;

import de.immomio.model.repository.shared.contract.DigitalContractHistoryRepository;
import de.immomio.model.repository.shared.contract.DigitalContractRepository;
import de.immomio.service.shared.contract.AbstractDigitalContractHistoryService;
import org.springframework.stereotype.Service;

@Service
public class DigitalContractHistoryService extends AbstractDigitalContractHistoryService<DigitalContractHistoryRepository, DigitalContractRepository> {

    public DigitalContractHistoryService(
            DigitalContractHistoryRepository digitalContractHistoryRepository,
            DigitalContractRepository digitalContractRepository
    ) {
        super(digitalContractHistoryRepository, digitalContractRepository);
    }

}
