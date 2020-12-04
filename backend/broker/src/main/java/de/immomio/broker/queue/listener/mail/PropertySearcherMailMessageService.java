package de.immomio.broker.queue.listener.mail;

import de.immomio.broker.service.MailMessageService;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.messaging.converter.BaseJsonConverter;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PropertySearcherMailMessageService implements MessageListener {

    @Autowired
    private BaseJsonConverter baseJsonConverter;

    @Autowired
    private MailMessageService mailMessageService;

    @Override
    public void onMessage(Message message) {
        PropertySearcherProfileMailBrokerContainer container = (PropertySearcherProfileMailBrokerContainer) baseJsonConverter.fromMessage(
                message);
        mailMessageService.onMessage(container);
    }
}
