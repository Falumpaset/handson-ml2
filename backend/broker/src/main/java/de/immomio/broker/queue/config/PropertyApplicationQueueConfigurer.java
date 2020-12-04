package de.immomio.broker.queue.config;

import de.immomio.broker.error.CustomBrokerErrorHandler;
import de.immomio.broker.queue.listener.ApplicationMessageListener;
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
public class PropertyApplicationQueueConfigurer {

    private final static String QUEUE_NAME = QueueConfigUtils.PropertyApplicationConfig.QUEUE_NAME;

    private static final String EXCHANGE_NAME = QueueConfigUtils.PropertyApplicationConfig.EXCHANGE_NAME;

    private static final String ROUTING_KEY = QueueConfigUtils.PropertyApplicationConfig.ROUTING_KEY;

    private final ConnectionFactory connectionFactory;

    private final CustomBrokerErrorHandler executeErrorHandler;

    private final ApplicationMessageListener applicationMessageListener;

    private final AmqpAdmin amqpAdmin;

    @Autowired
    public PropertyApplicationQueueConfigurer(
            ConnectionFactory connectionFactory,
            CustomBrokerErrorHandler executeErrorHandler,
            ApplicationMessageListener applicationMessageListener,
            AmqpAdmin amqpAdmin
    ) {
        this.connectionFactory = connectionFactory;
        this.executeErrorHandler = executeErrorHandler;
        this.applicationMessageListener = applicationMessageListener;
        this.amqpAdmin = amqpAdmin;
    }

    @Bean
    public SimpleMessageListenerContainer applicationMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(applicationQueue());
        container.setMessageListener(applicationMessageListener);
        container.setErrorHandler(executeErrorHandler);
        container.setAutoStartup(true);
        container.setConcurrentConsumers(1);
        container.setDefaultRequeueRejected(false);
        return container;
    }

    @Bean
    public Queue applicationQueue() {
        Queue queue = new Queue(QUEUE_NAME, true);
        amqpAdmin.declareQueue(queue);

        return queue;
    }

    @Bean
    public DirectExchange applicationExchange() {
        DirectExchange directExchange = new DirectExchange(EXCHANGE_NAME, true, true);
        amqpAdmin.declareExchange(directExchange);

        return directExchange;
    }

    @Bean
    public Binding applicationBinding() {
        Binding binding = BindingBuilder.bind(applicationQueue()).to(applicationExchange()).with(ROUTING_KEY);
        amqpAdmin.declareBinding(binding);

        return binding;
    }

}
