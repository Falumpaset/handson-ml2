package de.immomio.recipient.service.conversation;

import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.service.landlord.AbstractNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fabian Beck.
 */

@Slf4j
@Service
public class ConversationMessageNotificationService extends AbstractNotificationService {

    public static final String SUBJECT_NOT_PARSABLE_TO_PS_MAIL = "Email konnte nicht zugeordnet werden";

    @Autowired
    public ConversationMessageNotificationService(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    public void sendExternalIdNotAssignableEmail(String email) {
        Map<String, Object> model = new HashMap<>();

        PropertySearcherProfileMailBrokerContainer container = new PropertySearcherProfileMailBrokerContainer(email,
                MailTemplate.INCOMING_MESSAGE_ID_NOT_PARSEABLE_TO_PS, SUBJECT_NOT_PARSABLE_TO_PS_MAIL, model);
        sendPropertySearcherEmailMessage(container);
    }
}
