package de.immomio.broker.queue.config;

import de.immomio.broker.queue.errorhandler.PropertyUpdateErrorHandler;
import de.immomio.broker.queue.listener.PropertyProposalLandlordUpdateListener;
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

import static de.immomio.messaging.config.QueueConfigUtils.PropertyProposalLandlordConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertyProposalLandlordConfig.QUEUE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertyProposalLandlordConfig.ROUTING_KEY;


/**
 * @author Niklas Lindemann
 */

@Configuration
public class PropertyProposalLandlordQueueConfigurer {

    private final ConnectionFactory connectionFactory;

    private final PropertyUpdateErrorHandler errorHandler;

    private final PropertyProposalLandlordUpdateListener propertyProposalLandlordUpdateListener;

    private final AmqpAdmin amqpAdmin;

    @Autowired
    public PropertyProposalLandlordQueueConfigurer(
            ConnectionFactory connectionFactory,
            PropertyUpdateErrorHandler errorHandler,
            PropertyProposalLandlordUpdateListener propertyProposalLandlordUpdateListener,
            AmqpAdmin amqpAdmin
    ) {
        this.connectionFactory = connectionFactory;
        this.errorHandler = errorHandler;
        this.propertyProposalLandlordUpdateListener = propertyProposalLandlordUpdateListener;
        this.amqpAdmin = amqpAdmin;
    }

    @Bean
    public SimpleMessageListenerContainer llPropertyProposalListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(llPropertyProposalUpdateQueue());
        container.setMessageListener(propertyProposalLandlordUpdateListener);
        container.setErrorHandler(errorHandler);
        container.setAutoStartup(true);
        container.setConcurrentConsumers(2);
        container.setMaxConcurrentConsumers(6);
        container.setDefaultRequeueRejected(false);

        return container;
    }

    @Bean
    public Queue llPropertyProposalUpdateQueue() {
        Queue queue = new Queue(QUEUE_NAME, true);
        amqpAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    public DirectExchange llPropertyProposalExchange() {
        DirectExchange directExchange = new DirectExchange(EXCHANGE_NAME, true, true);
        amqpAdmin.declareExchange(directExchange);
        return directExchange;
    }

    @Bean
    public Binding llPropertyProposalBinding() {
        Binding binding = BindingBuilder
                .bind(llPropertyProposalUpdateQueue())
                .to(llPropertyProposalExchange())
                .with(ROUTING_KEY);
        amqpAdmin.declareBinding(binding);
        return binding;
    }
}
