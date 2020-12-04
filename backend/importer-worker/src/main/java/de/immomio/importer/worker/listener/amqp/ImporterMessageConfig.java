/**
 *
 */
package de.immomio.importer.worker.listener.amqp;

import de.immomio.messages.InternalCommunicationErrorHandler;
import de.immomio.messaging.config.QueueConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @author Johannes Hiemer.
 */
@Service
@Slf4j
public class ImporterMessageConfig {

    // rabbitTemplate.convertAndSend(InternalCommunicationQueue.EXCHANGE_NAME,
    // "importer." + InternalCommunicationQueue.IMPORTER_WORKER_ROUTING_KEY,
    // importObject);

    private final ImporterMessageListener importerMessageListener;

    private final ConnectionFactory connectionFactory;

    private final AmqpAdmin amqpAdmin;

    @Autowired
    public ImporterMessageConfig(
            ImporterMessageListener importerMessageListener,
            ConnectionFactory connectionFactory,
            AmqpAdmin amqpAdmin
    ) {
        this.importerMessageListener = importerMessageListener;
        this.connectionFactory = connectionFactory;
        this.amqpAdmin = amqpAdmin;
    }

    public Queue internalCommunicationRecipientQueue() {
        Queue queue = new Queue(QueueConfigUtils.ImporterConfig.IMPORTER_WORKER, true);
        amqpAdmin.declareQueue(queue);

        return queue;
    }

    @Bean
    public Binding internalImporterMessageBinding() {
        Binding binding = BindingBuilder.bind(internalCommunicationRecipientQueue())
                .to(internalCommunicationWebExchange())
                .with(QueueConfigUtils.ImporterConfig.IMPORTER_WORKER_ROUTING_KEY);
        amqpAdmin.declareBinding(binding);

        return binding;
    }

    @Bean
    protected TopicExchange internalCommunicationWebExchange() {
        TopicExchange topicExchange = new TopicExchange(QueueConfigUtils.ImporterConfig.EXCHANGE_NAME, true, true);
        amqpAdmin.declareExchange(topicExchange);

        return topicExchange;
    }

    @Bean
    public SimpleMessageListenerContainer importMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(internalCommunicationRecipientQueue());
        container.setMessageListener(importerMessageListener);
        container.setErrorHandler(new InternalCommunicationErrorHandler());
        container.setAutoStartup(true);
        container.setConcurrentConsumers(5);
        container.setMaxConcurrentConsumers(10);
        container.setDefaultRequeueRejected(false);
        return container;
    }


}
