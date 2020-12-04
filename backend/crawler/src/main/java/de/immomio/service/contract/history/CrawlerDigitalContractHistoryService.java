package de.immomio.service.contract.history;

import de.immomio.model.repository.core.shared.contract.BaseDigitalContractHistoryRepository;
import de.immomio.model.repository.core.shared.contract.BaseDigitalContractRepository;
import de.immomio.service.shared.contract.AbstractDigitalContractHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Service
public class CrawlerDigitalContractHistoryService
        extends AbstractDigitalContractHistoryService<
        BaseDigitalContractHistoryRepository,
        BaseDigitalContractRepository> {

    private final BaseDigitalContractHistoryRepository historyRepository;

    @Autowired
    public CrawlerDigitalContractHistoryService(BaseDigitalContractHistoryRepository historyRepository, BaseDigitalContractRepository digitalContractRepository) {
        super(historyRepository, digitalContractRepository);

        this.historyRepository = historyRepository;
    }


}
