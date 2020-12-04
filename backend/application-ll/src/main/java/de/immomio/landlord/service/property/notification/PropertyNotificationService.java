package de.immomio.landlord.service.property.notification;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.landlord.bean.property.PropertySendExposeBean;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.landlord.service.reporting.indexing.delegate.LandlordPropertyIndexingDelegate;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.mail.LandlordMailBrokerContainer;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.service.landlord.AbstractNotificationService;
import de.immomio.service.shared.EmailModelProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class PropertyNotificationService extends AbstractNotificationService {

    public static final String MAIL_TEMPLATE_NOT_VALID_L = "MAIL_TEMPLATE_NOT_VALID_L";
    public static final List<MailTemplate> VALID_TEMPLATES = List.of(MailTemplate.PROPERTY_EXPOSE_NOTIFICATION_V1, MailTemplate.PROPERTY_EXPOSE_NOTIFICATION_V2, MailTemplate.PROPERTY_EXPOSE_NOTIFICATION_V3, MailTemplate.PROPERTY_EXPOSE_NOTIFICATION_V4, MailTemplate.PROPERTY_EXPOSE_NOTIFICATION_V5, MailTemplate.PROPERTY_EXPOSE_NOTIFICATION_V6);
    public static final String NEW_PROPERTY_MANAGER_SUBJECT = "new.propertymanager.assigned.subject";
    private final EmailModelProvider emailModelProvider;

    private LandlordPropertyIndexingDelegate indexingDelegate;

    @Autowired
    public PropertyNotificationService(EmailModelProvider emailModelProvider, LandlordPropertyIndexingDelegate indexingDelegate, RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
        this.emailModelProvider = emailModelProvider;
        this.indexingDelegate = indexingDelegate;
    }

    public void sendExpose(Property property, PropertySendExposeBean sendExposeBean) {
        if (!VALID_TEMPLATES.contains(sendExposeBean.getTemplate())) {
            throw new ApiValidationException(MAIL_TEMPLATE_NOT_VALID_L);
        }

        Map<String, S3File> attachments = sendExposeBean.getAttachments().stream().collect(Collectors.toMap(S3File::getName, Function.identity()));

        Map<String, Object> model = emailModelProvider.createPropertyModel(property);
        model.put(ModelParams.MODEL_MESSAGE, sendExposeBean.getBody());
        model.put(ModelParams.SUBJECT_PLACEHOLDER, property.getData().getName());

        sendExposeBean.getRecipients().forEach(recipient -> {
            PropertySearcherProfileMailBrokerContainer container = new PropertySearcherProfileMailBrokerContainer(recipient,
                    sendExposeBean.getTemplate(), model, attachments, property.getCustomer().getId());

            sendPropertySearcherEmailMessage(container);
            indexingDelegate.propertyExposeSent(property, recipient, sendExposeBean.getBody());
        });
    }

    public void notifyNewPropertyManager(Property property) {
        Map<String, Object> model = emailModelProvider.createPropertyModel(property);
        emailModelProvider.appendLandlordAppUrl(model);
        emailModelProvider.appendUser(model, property.getPropertyManager());
        sendLandlordEmailMessage(new LandlordMailBrokerContainer(property.getPropertyManager().getId(), MailTemplate.PROPERTY_MANAGER_NEW_PROPERTY_ASSIGNED, NEW_PROPERTY_MANAGER_SUBJECT, model));
    }

}
