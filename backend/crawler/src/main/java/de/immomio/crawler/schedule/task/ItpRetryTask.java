package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.service.contract.DigitalContractItpRetryService;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */
@Component
public class ItpRetryTask extends BaseTask {

    private DigitalContractItpRetryService retryService;

    public ItpRetryTask(DigitalContractItpRetryService retryService) {
        this.retryService = retryService;
    }

    @Override
    public boolean run() {
        retryService.retryFailedItpRequests();

        return true;
    }
}
