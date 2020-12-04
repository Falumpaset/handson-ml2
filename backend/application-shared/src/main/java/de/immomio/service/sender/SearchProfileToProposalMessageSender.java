package de.immomio.service.sender;

import de.immomio.messaging.container.proposal.PSSearchProfileMessageContainer;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static de.immomio.messaging.config.QueueConfigUtils.PropertyProposalPropertysearcherConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertyProposalPropertysearcherConfig.ROUTING_KEY;


/**
 * @author Niklas Lindemann
 */

@Component
public class SearchProfileToProposalMessageSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public SearchProfileToProposalMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private void sendMessage(String exchangeName, String routingKey, PSSearchProfileMessageContainer body) {
        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, body);
    }

    public void sendProposalUpdateMessage(PSSearchProfileMessageContainer container) {
        this.sendMessage(EXCHANGE_NAME, ROUTING_KEY, container);
    }
}
