package de.immomio.landlord.service.contract;

import de.immomio.docusign.service.DocuSignService;
import de.immomio.docusign.service.DocuSignSignerService;
import de.immomio.docusign.service.DocuSignStatusService;
import de.immomio.landlord.service.contract.history.DigitalContractHistoryService;
import de.immomio.landlord.service.contract.history.DigitalContractSignerHistoryService;
import de.immomio.model.repository.shared.contract.DigitalContractRepository;
import de.immomio.model.repository.shared.contract.signer.DigitalContractSignerRepository;
import de.immomio.service.shared.contract.status.AbstractDigitalContractStatusService;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Service
public class DigitalContractStatusService
        extends AbstractDigitalContractStatusService<DigitalContractHistoryService,
        DigitalContractRepository,
        DigitalContractSignerRepository,
        DigitalContractService,
        DigitalContractSignerHistoryService,
        LandlordDigitalContractStatusNotificationService> {

    public DigitalContractStatusService(
            DigitalContractRepository contractRepository,
            DigitalContractSignerRepository contractSignerRepository,
            DigitalContractHistoryService contractHistoryService,
            DigitalContractSignerHistoryService crawlerContractSignerHistoryService,
            DocuSignService docuSignService,
            DocuSignSignerService docuSignSignerService,
            DocuSignStatusService docuSignStatusService,
            LandlordDigitalContractStatusNotificationService contractNotificationService,
            DigitalContractService digitalContractService
    ) {
        super(contractRepository, contractSignerRepository, contractHistoryService, crawlerContractSignerHistoryService, docuSignService, docuSignSignerService, docuSignStatusService, contractNotificationService, digitalContractService);
    }
}
