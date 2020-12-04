/**
 *
 */
package de.immomio.importer.worker.listener.amqp;

import de.immomio.importer.worker.service.WorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**

 */
@Service
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImporterMessageListener implements MessageListener {

    private final WorkerService workerService;

    @Autowired
    public ImporterMessageListener(
            WorkerService workerService
    ) {
        this.workerService = workerService;

    }

    @Override
    public void onMessage(Message message) {
        log.info(message.toString());
        workerService.work(message);
    }

}
