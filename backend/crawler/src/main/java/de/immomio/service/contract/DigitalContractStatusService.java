package de.immomio.service.contract;

import de.immomio.docusign.service.DocuSignService;
import de.immomio.docusign.service.DocuSignSignerService;
import de.immomio.docusign.service.DocuSignStatusService;
import de.immomio.model.repository.core.shared.contract.BaseDigitalContractRepository;
import de.immomio.model.repository.core.shared.contract.signer.BaseDigitalContractSignerRepository;
import de.immomio.service.contract.history.CrawlerDigitalContractHistoryService;
import de.immomio.service.contract.history.CrawlerDigitalContractSignerHistoryService;
import de.immomio.service.contract.notification.CrawlerDigitalContractStatusNotificationService;
import de.immomio.service.shared.contract.status.AbstractDigitalContractStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DigitalContractStatusService
        extends AbstractDigitalContractStatusService<
        CrawlerDigitalContractHistoryService, BaseDigitalContractRepository,
        BaseDigitalContractSignerRepository, DigitalContractService, CrawlerDigitalContractSignerHistoryService, CrawlerDigitalContractStatusNotificationService> {

    @Autowired
    public DigitalContractStatusService(
            BaseDigitalContractRepository contractRepository,
            BaseDigitalContractSignerRepository contractSignerRepository,
            CrawlerDigitalContractHistoryService contractHistoryService,
            CrawlerDigitalContractSignerHistoryService crawlerContractSignerHistoryService,
            DocuSignService docuSignService,
            DocuSignSignerService docuSignSignerService,
            DocuSignStatusService docuSignStatusService,
            CrawlerDigitalContractStatusNotificationService contractNotificationService,
            DigitalContractService digitalContractService) {

        super(contractRepository,
                contractSignerRepository,
                contractHistoryService,
                crawlerContractSignerHistoryService,
                docuSignService,
                docuSignSignerService,
                docuSignStatusService,
                contractNotificationService,
                digitalContractService);
    }


}
