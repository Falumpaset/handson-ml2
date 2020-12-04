package de.immomio.mail.sender;

import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author Bastian Bliemeister, Maik Kingma
 */

@Slf4j
public abstract class AbstractMailSender {

    private final RabbitTemplate rabbitTemplate;

    AbstractMailSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    protected void sendMessage(String exchangeName, String routingKey, SenderMessageBean senderMessageBean) {
        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, senderMessageBean);
    }
}
