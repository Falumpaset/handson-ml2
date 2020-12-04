package de.immomio.broker.queue.config;

import de.immomio.broker.queue.errorhandler.PropertyUpdateErrorHandler;
import de.immomio.broker.queue.listener.PropertyScoreListener;
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

import static de.immomio.messaging.config.QueueConfigUtils.PropertyScoreConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertyScoreConfig.QUEUE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertyScoreConfig.ROUTING_KEY;


/**
 * @author Niklas Lindemann
 */

@Configuration
public class LandlordPropertyScoreQueueConfigurer {

    private static final int CONCURRENT_CONSUMERS = 3;
    private static final int MAX_CONCURRENT_CONSUMERS = 10;

    private final ConnectionFactory connectionFactory;

    private final PropertyUpdateErrorHandler errorHandler;

    private final PropertyScoreListener propertyScoreListener;

    private final AmqpAdmin amqpAdmin;

    @Autowired
    public LandlordPropertyScoreQueueConfigurer(
            ConnectionFactory connectionFactory,
            PropertyUpdateErrorHandler errorHandler,
            PropertyScoreListener propertyScoreListener,
            AmqpAdmin amqpAdmin
    ) {
        this.connectionFactory = connectionFactory;
        this.errorHandler = errorHandler;
        this.propertyScoreListener = propertyScoreListener;
        this.amqpAdmin = amqpAdmin;
    }

    @Bean
    public SimpleMessageListenerContainer propertyScoreListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(propertyScoreQueue());
        container.setMessageListener(propertyScoreListener);
        container.setErrorHandler(errorHandler);
        container.setAutoStartup(true);
        container.setConcurrentConsumers(CONCURRENT_CONSUMERS);
        container.setMaxConcurrentConsumers(MAX_CONCURRENT_CONSUMERS);

        return container;
    }

    @Bean
    public Queue propertyScoreQueue() {
        Queue queue = new Queue(QUEUE_NAME, true);
        amqpAdmin.declareQueue(queue);

        return queue;
    }

    @Bean
    public DirectExchange propertyScoreExchange() {
        DirectExchange directExchange = new DirectExchange(EXCHANGE_NAME, true, true);
        amqpAdmin.declareExchange(directExchange);

        return directExchange;
    }

    @Bean
    public Binding propertyScoreBinding() {
        Binding binding = BindingBuilder
                .bind(propertyScoreQueue())
                .to(propertyScoreExchange())
                .with(ROUTING_KEY);
        amqpAdmin.declareBinding(binding);

        return binding;
    }
}
