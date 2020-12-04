package de.immomio.broker.queue.listener;

import de.immomio.broker.error.CustomBrokerErrorHandler;
import de.immomio.broker.service.ApplicationMessageService;
import de.immomio.messaging.container.property.PropertyMakeTenantContainer;
import de.immomio.messaging.converter.BaseJsonConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PropertyMakeTenantMessageListener implements MessageListener {

    private final BaseJsonConverter baseJsonConverter;

    private final CustomBrokerErrorHandler executeErrorHandler;

    private final ApplicationMessageService applicationMessageService;

    @Autowired
    public PropertyMakeTenantMessageListener(
            BaseJsonConverter baseJsonConverter,
            CustomBrokerErrorHandler executeErrorHandler,
            ApplicationMessageService applicationMessageService
    ) {
        this.baseJsonConverter = baseJsonConverter;
        this.executeErrorHandler = executeErrorHandler;
        this.applicationMessageService = applicationMessageService;
    }

    @Override
    public void onMessage(Message message) {
        try {
            PropertyMakeTenantContainer propertyMakeTenantContainer =
                    (PropertyMakeTenantContainer) baseJsonConverter.fromMessage(message);

            applicationMessageService.onMessage(propertyMakeTenantContainer);
        } catch (Exception ex) {
            executeErrorHandler.handleError(ex);
        }
    }
}
