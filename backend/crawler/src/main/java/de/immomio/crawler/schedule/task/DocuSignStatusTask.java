package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.service.contract.DigitalContractStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */
@Component
public class DocuSignStatusTask extends BaseTask {

    private final DigitalContractStatusService digitalContractStatusService;

    @Autowired
    public DocuSignStatusTask(DigitalContractStatusService digitalContractStatusService) {
        this.digitalContractStatusService = digitalContractStatusService;
    }

    @Override
    public boolean run() {
        digitalContractStatusService.handleEnvelopeStatusChanges();
        digitalContractStatusService.updateCompletedContractsWithMissingSignedDocuments();

        return true;
    }
}
