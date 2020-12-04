package de.immomio.landlord.service.sender;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.messaging.container.proposal.LandlordPropertyMessageContainer;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static de.immomio.messaging.config.QueueConfigUtils.PropertyProposalLandlordConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertyProposalLandlordConfig.ROUTING_KEY;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Component
public class LandlordProposalMessageSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public LandlordProposalMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private void sendMessage(LandlordPropertyMessageContainer body) {
        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, body);
    }

    public void sendProposalUpdateMessage(Property property, boolean calculateCoordinates) {
        sendUpdateMessage(Collections.singletonList(property), calculateCoordinates);
    }

    private void sendUpdateMessage(
            List<Property> properties,
            boolean calculateCoordinates
    ) {
        LandlordPropertyMessageContainer container = new LandlordPropertyMessageContainer(calculateCoordinates);
        properties.forEach(property -> container.getPropertyIds().add(property.getId()));
        this.sendMessage(container);
    }
}
