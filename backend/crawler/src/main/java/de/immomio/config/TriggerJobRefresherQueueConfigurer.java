package de.immomio.config;

import de.immomio.crawler.schedule.task.UserSearchingInquiryEmailTask;
import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.messaging.config.QueueConfigUtils;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.MessagingException;

/**
 * @author Niklas Lindemann
 */

@Configuration
public class TriggerJobRefresherQueueConfigurer implements MessageListener {
    private static final String QUEUE_NAME = QueueConfigUtils.TriggerJobConfig.QUEUE_NAME;

    private static final String EXCHANGE_NAME = QueueConfigUtils.TriggerJobConfig.EXCHANGE_NAME;

    private static final String ROUTING_KEY = QueueConfigUtils.TriggerJobConfig.ROUTING_KEY;

    private final ConnectionFactory connectionFactory;

    private final AmqpAdmin amqpAdmin;

    private final UserSearchingInquiryEmailTask inquiryEmailTask;

    private ApplicationContext applicationContext;

    @Autowired
    public TriggerJobRefresherQueueConfigurer(
            ConnectionFactory connectionFactory,
            AmqpAdmin amqpAdmin,
            UserSearchingInquiryEmailTask inquiryEmailTask,
            ApplicationContext applicationContext) {
        this.connectionFactory = connectionFactory;
        this.amqpAdmin = amqpAdmin;
        this.inquiryEmailTask = inquiryEmailTask;
        this.applicationContext = applicationContext;
    }

    @Bean
    public SimpleMessageListenerContainer triggerJobListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(triggerJobRefresherQueue());
        container.setAutoStartup(true);
        container.setConcurrentConsumers(1);
        container.setDefaultRequeueRejected(false);
        container.setMessageListener(this);
        return container;
    }

    @Bean
    public Queue triggerJobRefresherQueue() {
        Queue queue = new Queue(QUEUE_NAME, true);
        amqpAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    public DirectExchange triggerJobRefresherDirectExchange() {
        DirectExchange directExchange = new DirectExchange(EXCHANGE_NAME, true, true);
        amqpAdmin.declareExchange(directExchange);
        return directExchange;
    }

    @Bean
    public Binding triggerJobRefresherBinding() {
        Binding binding = BindingBuilder.bind(triggerJobRefresherQueue()).to(triggerJobRefresherDirectExchange()).with(ROUTING_KEY);
        amqpAdmin.declareBinding(binding);
        return binding;
    }

    @Override
    public void onMessage(Message message) {
        String jobName = new String(message.getBody()).replace("\"","").replace("'","");
        BaseTask baseTask = applicationContext.getBean(jobName, BaseTask.class);
        System.out.println(baseTask);
        if (baseTask != null) {
            try {
                baseTask.run();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
}
