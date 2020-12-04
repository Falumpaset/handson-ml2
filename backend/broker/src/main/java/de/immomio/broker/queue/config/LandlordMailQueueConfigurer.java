package de.immomio.broker.queue.config;

import de.immomio.broker.error.CustomBrokerErrorHandler;
import de.immomio.broker.queue.listener.mail.LandlordMailMessageService;
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

import static de.immomio.messaging.config.QueueConfigUtils.MailLandlordBrokerConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.MailLandlordBrokerConfig.QUEUE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.MailLandlordBrokerConfig.ROUTING_KEY;

@Component
public class LandlordMailQueueConfigurer {

    private final ConnectionFactory connectionFactory;

    private final CustomBrokerErrorHandler executeErrorHandler;

    private final LandlordMailMessageService landlordMailMessageService;

    private final AmqpAdmin amqpAdmin;

    @Autowired
    public LandlordMailQueueConfigurer(
            ConnectionFactory connectionFactory,
            CustomBrokerErrorHandler executeErrorHandler,
            LandlordMailMessageService landlordMailMessageService,
            AmqpAdmin amqpAdmin
    ) {
        this.connectionFactory = connectionFactory;
        this.executeErrorHandler = executeErrorHandler;
        this.landlordMailMessageService = landlordMailMessageService;
        this.amqpAdmin = amqpAdmin;
    }

    @Bean
    public SimpleMessageListenerContainer landlordMailMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(landlordMailQueue());
        container.setMessageListener(landlordMailMessageService);
        container.setErrorHandler(executeErrorHandler);
        container.setAutoStartup(true);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(10);
        container.setDefaultRequeueRejected(true);
        return container;
    }

    @Bean
    public Queue landlordMailQueue() {
        Queue queue = new Queue(QUEUE_NAME, true);
        amqpAdmin.declareQueue(queue);

        return queue;
    }

    @Bean
    public DirectExchange landlordMailDirectExchange() {
        DirectExchange directExchange = new DirectExchange(EXCHANGE_NAME, true, true);
        amqpAdmin.declareExchange(directExchange);

        return directExchange;
    }

    @Bean
    public Binding landlordMailBinding() {
        Binding binding = BindingBuilder.bind(landlordMailQueue())
                .to(landlordMailDirectExchange())
                .with(ROUTING_KEY);
        amqpAdmin.declareBinding(binding);

        return binding;
    }

}
