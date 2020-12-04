package de.immomio.broker.queue.config;

import de.immomio.broker.queue.errorhandler.PropertyUpdateErrorHandler;
import de.immomio.broker.queue.listener.PropertyProposalPropertySearcherUpdateListener;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static de.immomio.messaging.config.QueueConfigUtils.PropertyProposalPropertysearcherConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertyProposalPropertysearcherConfig.QUEUE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertyProposalPropertysearcherConfig.ROUTING_KEY;

/**
 * @author Niklas Lindemann
 */

@Configuration
public class PropertyProposalPropertySearcherQueueConfigurer {

    private final ConnectionFactory connectionFactory;

    private final PropertyUpdateErrorHandler errorHandler;

    private final PropertyProposalPropertySearcherUpdateListener propertySearcherUpdateListener;

    private final AmqpAdmin amqpAdmin;

    @Autowired
    public PropertyProposalPropertySearcherQueueConfigurer(
            ConnectionFactory connectionFactory,
            PropertyUpdateErrorHandler errorHandler,
            PropertyProposalPropertySearcherUpdateListener propertySearcherUpdateListener,
            AmqpAdmin amqpAdmin
    ) {
        this.connectionFactory = connectionFactory;
        this.errorHandler = errorHandler;
        this.propertySearcherUpdateListener = propertySearcherUpdateListener;
        this.amqpAdmin = amqpAdmin;
    }

    @Bean
    public SimpleMessageListenerContainer psPropertyProposalListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(psPropertyProposalUpdateQueue());
        container.setMessageListener(propertySearcherUpdateListener);
        container.setErrorHandler(errorHandler);
        container.setAutoStartup(true);
        container.setConcurrentConsumers(1);
        container.setDefaultRequeueRejected(false);
        return container;
    }

    @Bean
    public Queue psPropertyProposalUpdateQueue() {
        Queue queue = new Queue(QUEUE_NAME, true);
        amqpAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    public DirectExchange psPropertyProposalExchange() {
        DirectExchange directExchange = new DirectExchange(EXCHANGE_NAME, true, true);
        amqpAdmin.declareExchange(directExchange);
        return directExchange;
    }

    @Bean
    public Binding psPropertyProposalBinding() {
        Binding binding = BindingBuilder
                .bind(psPropertyProposalUpdateQueue())
                .to(psPropertyProposalExchange())
                .with(ROUTING_KEY);
        amqpAdmin.declareBinding(binding);
        return binding;
    }
}
