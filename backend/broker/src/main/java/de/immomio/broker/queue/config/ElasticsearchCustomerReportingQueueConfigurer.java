package de.immomio.broker.queue.config;

import de.immomio.broker.queue.errorhandler.PropertyUpdateErrorHandler;
import de.immomio.messaging.converter.BaseJsonConverter;
import de.immomio.reporting.model.messaging.IndexingCustomerMessageContainer;
import de.immomio.reporting.service.indexing.ElasticsearchCustomerIndexingService;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static de.immomio.messaging.config.QueueConfigUtils.ElasticsearchCustomerIndexingConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.ElasticsearchCustomerIndexingConfig.QUEUE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.ElasticsearchCustomerIndexingConfig.ROUTING_KEY;

@Configuration
public class ElasticsearchCustomerReportingQueueConfigurer implements MessageListener {

    private final ConnectionFactory connectionFactory;

    private final PropertyUpdateErrorHandler errorHandler;

    private final AmqpAdmin amqpAdmin;

    private BaseJsonConverter baseJsonConverter;

    private ElasticsearchCustomerIndexingService indexingService;

    @Autowired
    public ElasticsearchCustomerReportingQueueConfigurer(
            ConnectionFactory connectionFactory,
            PropertyUpdateErrorHandler errorHandler,
            AmqpAdmin amqpAdmin,
            BaseJsonConverter baseJsonConverter,
            ElasticsearchCustomerIndexingService indexingService
    ) {
        this.connectionFactory = connectionFactory;
        this.errorHandler = errorHandler;
        this.amqpAdmin = amqpAdmin;
        this.baseJsonConverter = baseJsonConverter;
        this.indexingService = indexingService;
    }

    @Bean
    public SimpleMessageListenerContainer elasticsearchCustomerIndexingListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(elasticsearchCustomerIndexingUpdateQueue());
        container.setMessageListener(this);
        container.setErrorHandler(errorHandler);
        container.setAutoStartup(true);
        container.setConcurrentConsumers(1);
        return container;
    }

    @Bean
    public Queue elasticsearchCustomerIndexingUpdateQueue() {
        Queue queue = new Queue(QUEUE_NAME, true);
        amqpAdmin.declareQueue(queue);

        return queue;
    }

    @Bean
    public DirectExchange elasticsearchCustomerIndexingExchange() {
        DirectExchange directExchange = new DirectExchange(EXCHANGE_NAME, true, true);
        amqpAdmin.declareExchange(directExchange);

        return directExchange;
    }

    @Bean
    public Binding elasticsearchCustomerIndexingBinding() {
        Binding binding = BindingBuilder
                .bind(elasticsearchCustomerIndexingUpdateQueue())
                .to(elasticsearchCustomerIndexingExchange())
                .with(ROUTING_KEY);
        amqpAdmin.declareBinding(binding);

        return binding;
    }

    @Override
    public void onMessage(Message message) {
        IndexingCustomerMessageContainer indexingMessageContainer = (IndexingCustomerMessageContainer) baseJsonConverter.fromMessage(
                message);
        indexingService.processIndexing(indexingMessageContainer);
    }
}
