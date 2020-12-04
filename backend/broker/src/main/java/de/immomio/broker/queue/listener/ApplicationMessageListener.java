package de.immomio.broker.queue.listener;

import de.immomio.broker.error.CustomBrokerErrorHandler;
import de.immomio.broker.service.ApplicationMessageService;
import de.immomio.messaging.container.PropertyApplicationBrokerContainer;
import de.immomio.messaging.converter.BaseJsonConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Johannes Hiemer, Maik Kingma.
 */

@Slf4j
@Component
public class ApplicationMessageListener implements MessageListener {

    private final BaseJsonConverter baseJsonConverter;

    private final CustomBrokerErrorHandler executeErrorHandler;

    private final ApplicationMessageService applicationMessageService;

    @Autowired
    public ApplicationMessageListener(
            BaseJsonConverter baseJsonConverter,
            CustomBrokerErrorHandler executeErrorHandler,
            ApplicationMessageService applicationMessageService
    ) {
        this.baseJsonConverter = baseJsonConverter;
        this.executeErrorHandler = executeErrorHandler;
        this.applicationMessageService = applicationMessageService;
    }

    /* (non-Javadoc)
     * @see org.springframework.amqp.core.MessageListener#onMessage(org.springframework.amqp.core.Message)
     */
    @Override
    public void onMessage(Message message) {
        try {
            PropertyApplicationBrokerContainer propertyApplicationBrokerContainer =
                    (PropertyApplicationBrokerContainer) baseJsonConverter.fromMessage(message);

            applicationMessageService.onMessage(propertyApplicationBrokerContainer);
        } catch (Exception ex) {
            executeErrorHandler.handleError(ex);
        }
    }
}
