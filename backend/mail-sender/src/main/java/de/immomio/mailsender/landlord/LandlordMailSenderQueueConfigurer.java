package de.immomio.mailsender.landlord;

import de.immomio.mailsender.MailSenderErrorHandler;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static de.immomio.messaging.config.QueueConfigUtils.MailConfig.LANDLORD_EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.MailConfig.LANDLORD_QUEUE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.MailConfig.ROUTING_KEY;

/**
 * @author Johannes Hiemer.
 */

@Configuration
public class LandlordMailSenderQueueConfigurer {

    private static final int INITIAL_INTERVAL = 10000;
    private static final double MULTIPLIER = 5.0;
    private static final int MAX_INTERVAL = 3600000;
    private static final int MAX_ATTEMPTS = 500;

    private final ConnectionFactory connectionFactory;

    private final MailSenderErrorHandler executeErrorHandler;

    private final LandlordMailSenderListener mailSenderListener;

    private final AmqpAdmin amqpAdmin;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public LandlordMailSenderQueueConfigurer(
            ConnectionFactory connectionFactory,
            MailSenderErrorHandler executeErrorHandler,
            LandlordMailSenderListener mailSenderListener,
            AmqpAdmin amqpAdmin,
            RabbitTemplate rabbitTemplate
    ) {
        this.connectionFactory = connectionFactory;
        this.executeErrorHandler = executeErrorHandler;
        this.mailSenderListener = mailSenderListener;
        this.amqpAdmin = amqpAdmin;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    public SimpleMessageListenerContainer llMailSenderListenerContainer() {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setQueues(llMailSenderQueue());
        container.setMessageListener(mailSenderListener);
        container.setErrorHandler(executeErrorHandler);
        container.setAutoStartup(true);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(10);
        container.setDefaultRequeueRejected(true);

        return container;
    }

    @Bean
    public Queue llMailSenderQueue() {
        Queue queue = new Queue(LANDLORD_QUEUE_NAME, true);
        amqpAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    public DirectExchange llMailSenderExchange() {
        DirectExchange directExchange = new DirectExchange(LANDLORD_EXCHANGE_NAME, true, true);
        amqpAdmin.declareExchange(directExchange);

        return directExchange;
    }

    @Bean
    public Binding llMailSenderBinding() {
        Binding binding = BindingBuilder.bind(llMailSenderQueue()).to(llMailSenderExchange()).with(ROUTING_KEY);
        amqpAdmin.declareBinding(binding);

        return binding;
    }

}
