package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.service.followup.FollowupNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */

@Component
public class FollowupNotificationTask extends BaseTask {
    private final FollowupNotificationService notificationService;

    @Autowired
    public FollowupNotificationTask(FollowupNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public boolean run()  {
        notificationService.notifyUsers();
        return true;
    }
}
