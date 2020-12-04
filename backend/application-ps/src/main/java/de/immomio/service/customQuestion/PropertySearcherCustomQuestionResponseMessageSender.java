package de.immomio.service.customQuestion;

import de.immomio.messaging.container.customQuestion.CustomQuestionResponseContainer;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static de.immomio.messaging.config.QueueConfigUtils.CustomQuestionResponseConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.CustomQuestionResponseConfig.ROUTING_KEY;

/**
 * @author Niklas Lindemann
 */

@Component
public class PropertySearcherCustomQuestionResponseMessageSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PropertySearcherCustomQuestionResponseMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private void sendMessage(String exchangeName, String routingKey, CustomQuestionResponseContainer body) {
        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, body);
    }

    public void sendCustomQuestionResponseMessage(CustomQuestionResponseContainer container) {
        this.sendMessage(EXCHANGE_NAME, ROUTING_KEY, container);
    }
}
