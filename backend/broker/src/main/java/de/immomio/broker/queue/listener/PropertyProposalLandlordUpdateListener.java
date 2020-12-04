package de.immomio.broker.queue.listener;

import de.immomio.broker.service.OnMessageService;
import de.immomio.messaging.container.proposal.LandlordPropertyMessageContainer;
import de.immomio.messaging.converter.BaseJsonConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * @author Niklas Lindemann
 */

@Service
@Slf4j
public class PropertyProposalLandlordUpdateListener implements MessageListener {

    private final OnMessageService messageService;

    private final BaseJsonConverter baseJsonConverter;

    @Autowired
    public PropertyProposalLandlordUpdateListener(
            OnMessageService messageService,
            BaseJsonConverter baseJsonConverter
    ) {
        this.messageService = messageService;
        this.baseJsonConverter = baseJsonConverter;
    }

    @Override
    public void onMessage(Message message) {
        LandlordPropertyMessageContainer container =
                (LandlordPropertyMessageContainer) baseJsonConverter.fromMessage(message);
        messageService.onMessage(container);
    }
}
