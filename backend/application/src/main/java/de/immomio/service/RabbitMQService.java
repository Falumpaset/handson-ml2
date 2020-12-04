package de.immomio.service;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.messaging.config.QueueConfigUtils;
import de.immomio.messaging.container.PropertyBrokerContainer;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Maik Kingma
 */

@Service
public class RabbitMQService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void queueProperty(Property property) {
        PropertyBrokerContainer message = new PropertyBrokerContainer(property.getId());

        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(QueueConfigUtils.PropertyConfig.EXCHANGE_NAME,
                QueueConfigUtils.PropertyConfig.ROUTING_KEY, message);
    }

}
