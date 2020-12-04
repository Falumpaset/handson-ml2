package de.immomio.broker.queue.listener.mail;

import de.immomio.broker.service.MailMessageService;
import de.immomio.messaging.container.mail.LandlordMailBrokerContainer;
import de.immomio.messaging.converter.BaseJsonConverter;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LandlordMailMessageService implements MessageListener {

    private final BaseJsonConverter baseJsonConverter;

    private final MailMessageService mailMessageService;

    @Autowired
    public LandlordMailMessageService(BaseJsonConverter baseJsonConverter, MailMessageService mailMessageService) {
        this.baseJsonConverter = baseJsonConverter;
        this.mailMessageService = mailMessageService;
    }

    @Override
    public void onMessage(Message message) {
        LandlordMailBrokerContainer container = (LandlordMailBrokerContainer) baseJsonConverter.fromMessage(message);
        mailMessageService.onMessage(container);
    }
}
