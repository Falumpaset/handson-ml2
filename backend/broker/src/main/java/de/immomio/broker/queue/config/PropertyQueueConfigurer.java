package de.immomio.broker.queue.config;

import de.immomio.broker.error.CustomBrokerErrorHandler;
import de.immomio.broker.queue.listener.PropertyPortalMessageListener;
import de.immomio.messaging.config.QueueConfigUtils;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Johannes Hiemer.
 */
@Component
public class PropertyQueueConfigurer {

    private static final String QUEUE_NAME = QueueConfigUtils.PropertyConfig.QUEUE_NAME;

    private static final String EXCHANGE_NAME = QueueConfigUtils.PropertyConfig.EXCHANGE_NAME;

    private static final String ROUTING_KEY = QueueConfigUtils.PropertyConfig.ROUTING_KEY;

    private final ConnectionFactory connectionFactory;

    private final CustomBrokerErrorHandler executeErrorHandler;

    private final PropertyPortalMessageListener propertyPortalMessageListener;

    private final AmqpAdmin amqpAdmin;

    @Autowired
    public PropertyQueueConfigurer(
            ConnectionFactory connectionFactory,
            CustomBrokerErrorHandler executeErrorHandler,
            PropertyPortalMessageListener propertyPortalMessageListener,
            AmqpAdmin amqpAdmin
    ) {
        this.connectionFactory = connectionFactory;
        this.executeErrorHandler = executeErrorHandler;
        this.propertyPortalMessageListener = propertyPortalMessageListener;
        this.amqpAdmin = amqpAdmin;
    }

    @Bean
    public SimpleMessageListenerContainer flatMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(propertyQueue());
        container.setMessageListener(propertyPortalMessageListener);
        container.setErrorHandler(executeErrorHandler);
        container.setAutoStartup(true);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(10);
        container.setDefaultRequeueRejected(false);
        return container;
    }

    @Bean
    public Queue propertyQueue() {
        Queue queue = new Queue(QUEUE_NAME, true);
        amqpAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    public DirectExchange propertyDirectExchange() {
        DirectExchange directExchange = new DirectExchange(EXCHANGE_NAME, true, true);
        amqpAdmin.declareExchange(directExchange);
        return directExchange;
    }

    @Bean
    public Binding propertyBinding() {
        Binding binding = BindingBuilder.bind(propertyQueue()).to(propertyDirectExchange()).with(ROUTING_KEY);
        amqpAdmin.declareBinding(binding);
        return binding;
    }

}
