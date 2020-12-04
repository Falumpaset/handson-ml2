package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.data.landlord.bean.customer.LandlordCustomerBean;
import de.immomio.data.landlord.bean.property.PropertyMailBean;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.portal.PropertyPortalState;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserBean;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.PropertyBrokerContainer;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static de.immomio.messaging.config.QueueConfigUtils.PropertyApplicationConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertyApplicationConfig.ROUTING_KEY;

/**
 * @author Johannes Hiemer.
 */
@Slf4j
@Component
public class DeactivatePropertyTask extends BaseTask {

    private static final String PROPERTY_DEACTIVATE_SUBJECT = "flat.unpublished.subject";

    private final BasePropertyRepository propertyRepository;

    private final RabbitTemplate rabbitTemplate;

    private final LandlordMailSender mailSender;

    @Autowired
    public DeactivatePropertyTask(
            BasePropertyRepository propertyRepository,
            RabbitTemplate rabbitTemplate,
            LandlordMailSender mailSender
    ) {
        this.propertyRepository = propertyRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.mailSender = mailSender;
    }

    @Override
    public boolean run() {
        deactivateFlats();

        return false;
    }

    private void deactivateFlats() {
        propertyRepository.findByStateAndValidUntilBefore(PropertyPortalState.ACTIVE, new Date()).forEach(property -> {
            property.setValidUntil(null);
            property.setRuntimeInDays(0);

            propertyRepository.save(property);

            try {
                PropertyBrokerContainer broker = new PropertyBrokerContainer(property.getId());

                rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
                rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, broker);
            } catch (Exception ex) {
                log.error("Could not process property", ex);
            }

            sendDeactivateNotification(property, getPropertyUser(property));
        });
    }

    private void sendDeactivateNotification(Property property, LandlordUser user) {
        Map<String, Object> model = getDeactivatePropertyModel(property, user);
        mailSender.send(user.getEmail(), MailTemplate.FLAT_UNPUBLISHED, PROPERTY_DEACTIVATE_SUBJECT, model);
    }

    private Map<String, Object> getDeactivatePropertyModel(Property property, LandlordUser user) {
        Map<String, Object> model = new HashMap<>();
        model.put(ModelParams.MODEL_USER, new LandlordUserBean(user));
        model.put(ModelParams.MODEL_FLAT, new PropertyMailBean(property));
        model.put(ModelParams.MODEL_CUSTOMER, new LandlordCustomerBean(user.getCustomer(), user.fullName()));

        return model;
    }
}
