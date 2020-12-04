package de.immomio.importer.worker.service;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.messaging.config.QueueConfigUtils;
import de.immomio.messaging.container.PropertyBrokerContainer;
import de.immomio.messaging.container.proposal.LandlordPropertyMessageContainer;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static de.immomio.messaging.config.QueueConfigUtils.PropertyProposalLandlordConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertyProposalLandlordConfig.ROUTING_KEY;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Component
public class ImporterPropertyMessageSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ImporterPropertyMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private void sendMessage(LandlordPropertyMessageContainer body) {
        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, body);
    }

    public void updatePropertyOnPortals(Property property) {
        var message = new PropertyBrokerContainer(property.getId());

        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(QueueConfigUtils.PropertyConfig.EXCHANGE_NAME,
                QueueConfigUtils.PropertyConfig.ROUTING_KEY, message);
    }

    public void createGeoCodesAndSendProposalUpdateMessage(Property property, boolean calculateCoordinates) {
        log.info("create proposals -> " + property.getId().toString() + " generate coordinates -> " + calculateCoordinates);
        LandlordPropertyMessageContainer container = new LandlordPropertyMessageContainer();
        container.setCalculateCoordinates(calculateCoordinates);
        container.getPropertyIds().add(property.getId());

        this.sendMessage(container);
    }


}
