package de.immomio.service.landlord;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.config.QueueConfigUtils;
import de.immomio.messaging.container.mail.LandlordMailBrokerContainer;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static de.immomio.mail.sender.templates.MailTemplate.APPLICATION_REJECTED_V1;
import static de.immomio.mail.sender.templates.MailTemplate.APPLICATION_REJECTED_V2;
import static de.immomio.mail.sender.templates.MailTemplate.APPLICATION_REJECTED_V3;
import static de.immomio.mail.sender.templates.MailTemplate.INVITE_ANONYMOUS_USER_TO_REGISTER;
import static de.immomio.mail.sender.templates.MailTemplate.MESSENGER_PS_NEW_MESSAGE_DIRECT;
import static de.immomio.mail.sender.templates.MailTemplate.PRE_TENANT_APPOINTMENT;
import static de.immomio.messaging.config.QueueConfigUtils.MailPropertySearcherBrokerConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.MailPropertySearcherBrokerConfig.ROUTING_KEY;

public abstract class AbstractNotificationService {


    private RabbitTemplate rabbitTemplate;

    public AbstractNotificationService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    protected void sendPropertySearcherEmailMessage(
            PropertySearcherProfileMailBrokerContainer messageBean,
            PropertySearcherUserProfile user
    ) {
        if (user.isAnonymous() && !isEmailAllowedToSend(messageBean.getTemplate())) {
            return;
        }

        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, messageBean);
    }

    protected void sendPropertySearcherEmailMessage(
            PropertySearcherProfileMailBrokerContainer messageBean
    ) {
        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, messageBean);
    }


    protected void sendLandlordEmailMessage(
            LandlordMailBrokerContainer messageBean
    ) {
        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(QueueConfigUtils.MailLandlordBrokerConfig.EXCHANGE_NAME, QueueConfigUtils.MailLandlordBrokerConfig.ROUTING_KEY, messageBean);
    }

    private boolean isEmailAllowedToSend(MailTemplate template) {
        return template == APPLICATION_REJECTED_V1 || template == APPLICATION_REJECTED_V2 ||
                template == APPLICATION_REJECTED_V3 || template == INVITE_ANONYMOUS_USER_TO_REGISTER || template == PRE_TENANT_APPOINTMENT
                || template == MESSENGER_PS_NEW_MESSAGE_DIRECT;
    }


}
