package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.service.contract.DigitalContractItpSignerStatusCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */
@Component
public class ItpNotificationTask extends BaseTask {

    private final DigitalContractItpSignerStatusCheckService notificationService;

    @Autowired
    public ItpNotificationTask(DigitalContractItpSignerStatusCheckService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public boolean run() {
        notificationService.handleItpSignerNotification();

        return true;
    }
}
