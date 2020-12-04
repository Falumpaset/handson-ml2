package de.immomio.service.property;

import de.immomio.messaging.container.proposal.LandlordPropertyMessageContainer;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import de.immomio.model.repository.service.landlord.customer.property.PropertyRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static de.immomio.messaging.config.QueueConfigUtils.PropertyProposalLandlordConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertyProposalLandlordConfig.ROUTING_KEY;

/**
 * @author Niklas Lindemann
 */
@Service
public class AdminPropertyProposalService {
    private RabbitTemplate rabbitTemplate;
    private final PropertyRepository propertyRepository;

    @Autowired
    public AdminPropertyProposalService(RabbitTemplate rabbitTemplate, PropertyRepository propertyRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.propertyRepository = propertyRepository;
    }

    public void refreshProposals() {
        List<Long> allIds = propertyRepository.findAllIds();
        allIds.forEach(id -> {
            sendMessage(new LandlordPropertyMessageContainer(Collections.singletonList(id), false));
        });
    }

    private void sendMessage(LandlordPropertyMessageContainer body) {
        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, body);
    }
}
