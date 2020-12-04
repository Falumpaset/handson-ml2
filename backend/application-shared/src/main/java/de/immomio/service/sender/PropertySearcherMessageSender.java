package de.immomio.service.sender;

import de.immomio.messaging.container.user.PSUserProfileMessageContainer;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static de.immomio.messaging.config.QueueConfigUtils.PropertySearcherConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertySearcherConfig.ROUTING_KEY;


/**
 * @author Niklas Lindemann
 */

@Component
public class PropertySearcherMessageSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PropertySearcherMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private void sendMessage(String exchangeName, String routingKey, PSUserProfileMessageContainer body) {
        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, body);
    }

    public void sendUserUpdateMessage(PSUserProfileMessageContainer container) {
        this.sendMessage(EXCHANGE_NAME, ROUTING_KEY, container);
    }
}
