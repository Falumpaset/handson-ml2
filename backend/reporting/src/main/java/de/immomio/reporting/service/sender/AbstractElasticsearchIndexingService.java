package de.immomio.reporting.service.sender;

import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import de.immomio.reporting.model.event.AbstractEvent;
import de.immomio.reporting.model.messaging.IndexingEvent;
import de.immomio.reporting.model.messaging.IndexingMessageContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static de.immomio.messaging.config.QueueConfigUtils.ElasticsearchIndexingConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.ElasticsearchIndexingConfig.ROUTING_KEY;

/**
 * @author Fabian Beck
 */

@Slf4j
public abstract class AbstractElasticsearchIndexingService {

    private RabbitTemplate rabbitTemplate;

    protected AbstractElasticsearchIndexingService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    protected void processIndexing(List<AbstractEvent> events, String prefix) {
        List<IndexingEvent> indexingEvents = events.stream()
                .map(event -> new IndexingEvent(event.writeJson()))
                .collect(Collectors.toList());

        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new IndexingMessageContainer(indexingEvents, prefix));
    }
}
