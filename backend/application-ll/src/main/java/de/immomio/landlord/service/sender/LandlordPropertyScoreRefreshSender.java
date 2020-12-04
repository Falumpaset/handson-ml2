package de.immomio.landlord.service.sender;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.messaging.container.score.LandlordPropertyScoreMessageContainer;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import de.immomio.model.repository.shared.property.PropertyRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static de.immomio.messaging.config.QueueConfigUtils.PropertyScoreConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertyScoreConfig.ROUTING_KEY;

/**
 * @author Niklas Lindemann
 */

@Component
public class LandlordPropertyScoreRefreshSender {
    private final RabbitTemplate rabbitTemplate;

    private final PropertyRepository propertyRepository;

    @Autowired
    public LandlordPropertyScoreRefreshSender(RabbitTemplate rabbitTemplate, PropertyRepository propertyRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.propertyRepository = propertyRepository;
    }

    private void sendMessage(LandlordPropertyScoreMessageContainer body) {
        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, body);
    }

    private void sendUpdateMessage(List<Long> propertyIds) {
        LandlordPropertyScoreMessageContainer container = new LandlordPropertyScoreMessageContainer();
        container.setPropertyIds(propertyIds);
        sendMessage(container);
    }

    public void sendUpdateMessage(LandlordCustomer customer) {
        sendUpdateMessage(propertyRepository.findAllIdsByCustomer(customer));
    }

    public void sendUpdateMessage(Prioset prioset) {
        sendUpdateMessage(propertyRepository.findAllIdsByPrioset(prioset));
    }

    public void sendUpdateMessageForPriosets(List<Prioset> priosets) {
        if (!priosets.isEmpty()) {
            sendUpdateMessage(propertyRepository.findAllIdsByPriosets(priosets));
        }
    }

}
