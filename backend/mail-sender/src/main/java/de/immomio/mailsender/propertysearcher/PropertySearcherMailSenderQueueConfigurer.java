package de.immomio.mailsender.propertysearcher;

import de.immomio.mailsender.MailSenderErrorHandler;
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

import static de.immomio.messaging.config.QueueConfigUtils.MailConfig.PROPERTY_SEARCHER_EXCHANGE_NAME;

/**
 * @author Johannes Hiemer.
 */
@Component
public class PropertySearcherMailSenderQueueConfigurer {

    private static final int INITIAL_INTERVAL = 10000;
    private static final double MULTIPLIER = 5.0;
    private static final int MAX_INTERVAL = 3600000;
    private static final int MAX_ATTEMPTS = 500;

    private final ConnectionFactory connectionFactory;

    private final MailSenderErrorHandler executeErrorHandler;

    private final PropertySearcherMailSenderListener mailSenderListener;

    private final AmqpAdmin amqpAdmin;

    @Autowired
    public PropertySearcherMailSenderQueueConfigurer(
            ConnectionFactory connectionFactory,
            MailSenderErrorHandler executeErrorHandler,
            PropertySearcherMailSenderListener mailSenderListener,
            AmqpAdmin amqpAdmin
    ) {
        this.connectionFactory = connectionFactory;
        this.executeErrorHandler = executeErrorHandler;
        this.mailSenderListener = mailSenderListener;
        this.amqpAdmin = amqpAdmin;
    }

    @Bean
    public SimpleMessageListenerContainer psMailSenderListenerContainer() {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setQueues(psMailSenderQueue());
        container.setMessageListener(mailSenderListener);
        container.setErrorHandler(executeErrorHandler);
        container.setAutoStartup(true);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(10);
        container.setDefaultRequeueRejected(true);
        return container;
    }

    @Bean
    public Queue psMailSenderQueue() {
        Queue queue = new Queue(QueueConfigUtils.MailConfig.PROPERTY_SEARCHER_QUEUE_NAME, true);
        amqpAdmin.declareQueue(queue);

        return queue;
    }

    @Bean
    public DirectExchange psMailSenderExchange() {
        DirectExchange directExchange = new DirectExchange(
                PROPERTY_SEARCHER_EXCHANGE_NAME,
                true,
                true);
        amqpAdmin.declareExchange(directExchange);

        return directExchange;
    }

    @Bean
    public Binding psMailSenderBinding() {
        Binding binding =
                BindingBuilder.bind(psMailSenderQueue())
                        .to(psMailSenderExchange())
                        .with(QueueConfigUtils.MailConfig.ROUTING_KEY);
        amqpAdmin.declareBinding(binding);

        return binding;
    }

}
