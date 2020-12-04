package de.immomio.broker.queue.config;

import de.immomio.broker.queue.errorhandler.PropertyUpdateErrorHandler;
import de.immomio.broker.queue.listener.PropertyMakeTenantMessageListener;
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

import static de.immomio.messaging.config.QueueConfigUtils.PropertyMakeTenantConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertyMakeTenantConfig.QUEUE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertyMakeTenantConfig.ROUTING_KEY;

@Configuration
public class PropertyMakeTenantQueueConfigurer {

    private static final int CONCURRENT_CONSUMERS = 3;

    private static final int MAX_CONCURRENT_CONSUMERS = 10;

    private final ConnectionFactory connectionFactory;

    private final PropertyUpdateErrorHandler errorHandler;

    private final PropertyMakeTenantMessageListener propertyMakeTenantMessageListener;

    private final AmqpAdmin amqpAdmin;

    @Autowired
    public PropertyMakeTenantQueueConfigurer(
            ConnectionFactory connectionFactory,
            PropertyUpdateErrorHandler errorHandler,
            PropertyMakeTenantMessageListener propertyMakeTenantMessageListener,
            AmqpAdmin amqpAdmin
    ) {
        this.connectionFactory = connectionFactory;
        this.errorHandler = errorHandler;
        this.propertyMakeTenantMessageListener = propertyMakeTenantMessageListener;
        this.amqpAdmin = amqpAdmin;
    }

    @Bean
    public SimpleMessageListenerContainer propertyMakeTenantListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(propertyMakeTenantUpdateQueue());
        container.setMessageListener(propertyMakeTenantMessageListener);
        container.setErrorHandler(errorHandler);
        container.setAutoStartup(true);
        container.setMaxConcurrentConsumers(MAX_CONCURRENT_CONSUMERS);
        container.setConcurrentConsumers(CONCURRENT_CONSUMERS);

        return container;
    }

    @Bean
    public Queue propertyMakeTenantUpdateQueue() {
        Queue queue = new Queue(QUEUE_NAME, true);
        amqpAdmin.declareQueue(queue);

        return queue;
    }

    @Bean
    public DirectExchange propertyMakeTenantExchange() {
        DirectExchange directExchange = new DirectExchange(EXCHANGE_NAME, true, true);
        amqpAdmin.declareExchange(directExchange);

        return directExchange;
    }

    @Bean
    public Binding propertyMakeTenantBinding() {
        Binding binding = BindingBuilder.bind(propertyMakeTenantUpdateQueue())
                .to(propertyMakeTenantExchange())
                .with(ROUTING_KEY);
        amqpAdmin.declareBinding(binding);

        return binding;
    }

}
