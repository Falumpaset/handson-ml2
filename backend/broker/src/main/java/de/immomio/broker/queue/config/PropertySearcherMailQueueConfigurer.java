package de.immomio.broker.queue.config;

import de.immomio.broker.error.CustomBrokerErrorHandler;
import de.immomio.broker.queue.listener.mail.PropertySearcherMailMessageService;
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

import static de.immomio.messaging.config.QueueConfigUtils.MailPropertySearcherBrokerConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.MailPropertySearcherBrokerConfig.QUEUE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.MailPropertySearcherBrokerConfig.ROUTING_KEY;

@Component
public class PropertySearcherMailQueueConfigurer {

    private final ConnectionFactory connectionFactory;

    private final CustomBrokerErrorHandler executeErrorHandler;

    private final PropertySearcherMailMessageService propertySearcherMailMessageService;

    private final AmqpAdmin amqpAdmin;

    @Autowired
    public PropertySearcherMailQueueConfigurer(
            ConnectionFactory connectionFactory,
            CustomBrokerErrorHandler executeErrorHandler,
            PropertySearcherMailMessageService propertySearcherMailMessageService,
            AmqpAdmin amqpAdmin
    ) {
        this.connectionFactory = connectionFactory;
        this.executeErrorHandler = executeErrorHandler;
        this.propertySearcherMailMessageService = propertySearcherMailMessageService;
        this.amqpAdmin = amqpAdmin;
    }

    @Bean
    public SimpleMessageListenerContainer propertySearcherMailMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(propertySearcherMailQueue());
        container.setMessageListener(propertySearcherMailMessageService);
        container.setErrorHandler(executeErrorHandler);
        container.setAutoStartup(true);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(10);
        container.setDefaultRequeueRejected(true);
        return container;
    }

    @Bean
    public Queue propertySearcherMailQueue() {
        Queue queue = new Queue(QUEUE_NAME, true);
        amqpAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    public DirectExchange propertySearcherMailDirectExchange() {
        DirectExchange directExchange = new DirectExchange(EXCHANGE_NAME, true, true);
        amqpAdmin.declareExchange(directExchange);
        return directExchange;
    }

    @Bean
    public Binding propertySearcherMailBinding() {
        Binding binding = BindingBuilder.bind(propertySearcherMailQueue())
                .to(propertySearcherMailDirectExchange())
                .with(ROUTING_KEY);
        amqpAdmin.declareBinding(binding);
        return binding;
    }

}
