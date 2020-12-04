package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.service.application.ArchiveApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */

@Component
public class ArchiveApplicationTask extends BaseTask {

    private final ArchiveApplicationService archiveApplicationService;

    @Autowired
    public ArchiveApplicationTask(ArchiveApplicationService archiveApplicationService) {
        this.archiveApplicationService = archiveApplicationService;
    }

    @Override
    public boolean run()  {
        archiveApplicationService.archiveApplications();
        return true;
    }
}
