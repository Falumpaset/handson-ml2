package de.immomio.broker.queue.listener;

import de.immomio.broker.service.OnMessageService;
import de.immomio.messaging.container.user.PSUserProfileMessageContainer;
import de.immomio.messaging.converter.BaseJsonConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class PropertySearcherUpdateListener implements MessageListener {

    private final OnMessageService onMessageService;

    private final BaseJsonConverter baseJsonConverter;

    @Autowired
    public PropertySearcherUpdateListener(OnMessageService onMessageService, BaseJsonConverter baseJsonConverter) {
        this.onMessageService = onMessageService;
        this.baseJsonConverter = baseJsonConverter;
    }

    @Override
    public void onMessage(Message message) {
        PSUserProfileMessageContainer container =
                (PSUserProfileMessageContainer) baseJsonConverter.fromMessage(message);
        onMessageService.onMessage(container);
    }
}
