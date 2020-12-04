package de.immomio.broker.queue.config;

import de.immomio.broker.queue.errorhandler.PropertyUpdateErrorHandler;
import de.immomio.broker.queue.listener.SelfDisclosureResponseListener;
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

import static de.immomio.messaging.config.QueueConfigUtils.SelfDisclosureResponseConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.SelfDisclosureResponseConfig.QUEUE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.SelfDisclosureResponseConfig.ROUTING_KEY;

@Configuration
public class SelfDisclosureResponseQueueConfigurer {

    private static final int CONCURRENT_CONSUMERS = 3;

    private static final int MAX_CONCURRENT_CONSUMERS = 10;

    private final ConnectionFactory connectionFactory;

    private final PropertyUpdateErrorHandler errorHandler;

    private final SelfDisclosureResponseListener selfDisclosureResponseListener;

    private final AmqpAdmin amqpAdmin;

    @Autowired
    public SelfDisclosureResponseQueueConfigurer(
            ConnectionFactory connectionFactory,
            PropertyUpdateErrorHandler errorHandler,
            SelfDisclosureResponseListener selfDisclosureResponseListener,
            AmqpAdmin amqpAdmin
    ) {
        this.connectionFactory = connectionFactory;
        this.errorHandler = errorHandler;
        this.selfDisclosureResponseListener = selfDisclosureResponseListener;
        this.amqpAdmin = amqpAdmin;
    }

    @Bean
    public SimpleMessageListenerContainer selDisclosureResponseListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(selfDisclosureResponseUpdateQueue());
        container.setMessageListener(selfDisclosureResponseListener);
        container.setErrorHandler(errorHandler);
        container.setAutoStartup(true);
        container.setConcurrentConsumers(CONCURRENT_CONSUMERS);
        container.setMaxConcurrentConsumers(MAX_CONCURRENT_CONSUMERS);

        return container;
    }

    @Bean
    public Queue selfDisclosureResponseUpdateQueue() {
        Queue queue = new Queue(QUEUE_NAME, true);
        amqpAdmin.declareQueue(queue);

        return queue;
    }

    @Bean
    public DirectExchange selfDisclosureResponseExchange() {
        DirectExchange directExchange = new DirectExchange(EXCHANGE_NAME, true, true);
        amqpAdmin.declareExchange(directExchange);

        return directExchange;
    }

    @Bean
    public Binding selDisclosureResponseBinding() {
        Binding binding = BindingBuilder
                .bind(selfDisclosureResponseUpdateQueue())
                .to(selfDisclosureResponseExchange())
                .with(ROUTING_KEY);
        amqpAdmin.declareBinding(binding);

        return binding;
    }
}
