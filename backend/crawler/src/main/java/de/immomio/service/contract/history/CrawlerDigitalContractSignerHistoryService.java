package de.immomio.service.contract.history;

import de.immomio.model.repository.core.shared.contract.signer.BaseDigitalContractSignerHistoryRepository;
import de.immomio.model.repository.core.shared.contract.signer.BaseDigitalContractSignerRepository;
import de.immomio.service.shared.contract.AbstractDigitalContractSignerHistoryService;
import org.springframework.stereotype.Service;

@Service
public class CrawlerDigitalContractSignerHistoryService extends AbstractDigitalContractSignerHistoryService<BaseDigitalContractSignerHistoryRepository, BaseDigitalContractSignerRepository> {

    public CrawlerDigitalContractSignerHistoryService(
            BaseDigitalContractSignerHistoryRepository signerHistoryRepository,
            BaseDigitalContractSignerRepository signerRepository
    ) {
       super(signerHistoryRepository, signerRepository);
    }

}
