package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.service.contract.DigitalContractItpStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */
@Component
public class ItpStatusTask extends BaseTask {

    private final DigitalContractItpStatusService digitalContractItpStatusService;

    @Autowired
    public ItpStatusTask(DigitalContractItpStatusService digitalContractItpStatusService) {
        this.digitalContractItpStatusService = digitalContractItpStatusService;
    }

    @Override
    public boolean run() {
        digitalContractItpStatusService.handleItpStatusChanges();

        return true;
    }
}
