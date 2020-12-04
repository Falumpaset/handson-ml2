package de.immomio.broker.queue.config;

import de.immomio.broker.queue.errorhandler.PropertyUpdateErrorHandler;
import de.immomio.broker.queue.listener.PropertySearcherUpdateListener;
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

import static de.immomio.messaging.config.QueueConfigUtils.PropertySearcherConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertySearcherConfig.QUEUE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertySearcherConfig.ROUTING_KEY;

/**
 * @author Niklas Lindemann
 */

@Configuration
public class PropertyScorePropertySearcherQueueConfigurer {

    private final ConnectionFactory connectionFactory;

    private final PropertyUpdateErrorHandler errorHandler;

    private final PropertySearcherUpdateListener scorePropertySearcherUpdateListener;

    private final AmqpAdmin amqpAdmin;

    private static final int CONCURRENT_CONSUMERS = 5;

    @Autowired
    public PropertyScorePropertySearcherQueueConfigurer(
            ConnectionFactory connectionFactory,
            PropertyUpdateErrorHandler errorHandler,
            PropertySearcherUpdateListener scorePropertySearcherUpdateListener,
            AmqpAdmin amqpAdmin
    ) {
        this.connectionFactory = connectionFactory;
        this.errorHandler = errorHandler;
        this.scorePropertySearcherUpdateListener = scorePropertySearcherUpdateListener;
        this.amqpAdmin = amqpAdmin;
    }

    @Bean
    public SimpleMessageListenerContainer psPropertyScoreListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(psPropertyScorePropertyUpdateQueue());
        container.setMessageListener(scorePropertySearcherUpdateListener);
        container.setErrorHandler(errorHandler);
        container.setAutoStartup(true);
        container.setConcurrentConsumers(CONCURRENT_CONSUMERS);
        return container;
    }

    @Bean
    public Queue psPropertyScorePropertyUpdateQueue() {
        Queue queue = new Queue(QUEUE_NAME, true);
        amqpAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    public DirectExchange psPropertyScoreExchange() {
        DirectExchange directExchange = new DirectExchange(EXCHANGE_NAME, true, true);
        amqpAdmin.declareExchange(directExchange);
        return directExchange;
    }

    @Bean
    public Binding psPropertyScoreBinding() {
        Binding binding = BindingBuilder
                .bind(psPropertyScorePropertyUpdateQueue())
                .to(psPropertyScoreExchange())
                .with(ROUTING_KEY);
        amqpAdmin.declareBinding(binding);
        return binding;
    }
}
