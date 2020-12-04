package de.immomio.broker.queue.config;

import de.immomio.broker.queue.errorhandler.PropertyUpdateErrorHandler;
import de.immomio.broker.queue.listener.CustomQuestionResponseListener;
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

import static de.immomio.messaging.config.QueueConfigUtils.CustomQuestionResponseConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.CustomQuestionResponseConfig.QUEUE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.CustomQuestionResponseConfig.ROUTING_KEY;


/**
 * @author Niklas Lindemann
 */

@Configuration
public class CustomQuestionResponseQueueConfigurer {

    private static final int CONCURRENT_CONSUMERS = 3;
    private static final int MAX_CONCURRENT_CONSUMERS = 10;

    private final ConnectionFactory connectionFactory;

    private final PropertyUpdateErrorHandler errorHandler;

    private final CustomQuestionResponseListener customQuestionResponseListener;

    private final AmqpAdmin amqpAdmin;

    @Autowired
    public CustomQuestionResponseQueueConfigurer(
            ConnectionFactory connectionFactory,
            PropertyUpdateErrorHandler errorHandler,
            CustomQuestionResponseListener customQuestionResponseListener,
            AmqpAdmin amqpAdmin
    ) {
        this.connectionFactory = connectionFactory;
        this.errorHandler = errorHandler;
        this.customQuestionResponseListener = customQuestionResponseListener;
        this.amqpAdmin = amqpAdmin;
    }

    @Bean
    public SimpleMessageListenerContainer customQuestionResponseListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(customQuestionResponseUpdateQueue());
        container.setMessageListener(customQuestionResponseListener);
        container.setErrorHandler(errorHandler);
        container.setAutoStartup(true);
        container.setConcurrentConsumers(CONCURRENT_CONSUMERS);
        container.setMaxConcurrentConsumers(MAX_CONCURRENT_CONSUMERS);

        return container;
    }

    @Bean
    public Queue customQuestionResponseUpdateQueue() {
        Queue queue = new Queue(QUEUE_NAME, true);
        amqpAdmin.declareQueue(queue);

        return queue;
    }

    @Bean
    public DirectExchange customQuestionResponseExchange() {
        DirectExchange directExchange = new DirectExchange(EXCHANGE_NAME, true, true);
        amqpAdmin.declareExchange(directExchange);

        return directExchange;
    }

    @Bean
    public Binding customQuestionResponseBinding() {
        Binding binding = BindingBuilder
                .bind(customQuestionResponseUpdateQueue())
                .to(customQuestionResponseExchange())
                .with(ROUTING_KEY);
        amqpAdmin.declareBinding(binding);

        return binding;
    }
}
