package de.immomio.landlord.service.conversation;

import de.immomio.data.landlord.bean.property.PropertyMailBean;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileEmailBean;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.data.shared.entity.conversation.ConversationMessageBean;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.service.conversation.ConversationConfigService;
import de.immomio.service.landlord.AbstractNotificationService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Fabian Beck.
 */

@Service
public class LandlordConversationMessageAsEmailNotificationService extends AbstractNotificationService {

    public static final String EMAIL_SUBJECT = "Neue Nachricht";

    public static final String SYMBOL_AT = "@";
    public static final String SYMBOL_PLUS = "+";

    private final ConversationConfigService configService;

    @Value("${conversation.email}")
    private String email;

    @Autowired
    public LandlordConversationMessageAsEmailNotificationService(RabbitTemplate rabbitTemplate,
            ConversationConfigService configService) {
        super(rabbitTemplate);
        this.configService = configService;
    }

    public void sendMessageAsEmail(Conversation conversation, ConversationMessageBean conversationMessageBean) {

        Map<String, Object> model = new HashMap<>();
        model.put(ModelParams.MODEL_MESSAGE, conversationMessageBean.getMessage());
        model.put(ModelParams.MODEL_USER, new PropertySearcherUserProfileEmailBean(conversation.getApplication().getUserProfile()));
        model.put(ModelParams.MODEL_FLAT, new PropertyMailBean(conversation.getApplication().getProperty()));

        Map<String, S3File> attachments = new HashMap<>();
        if (conversationMessageBean.getAttachments() != null) {
            attachments.putAll(conversationMessageBean.getAttachments()
                    .stream()
                    .collect(Collectors.toMap(S3File::getName, Function.identity())));
        }

        String fromEmail = email;
        MailTemplate messengerPsNewMessageDirect = MailTemplate.MESSENGER_PS_NEW_MESSAGE_DIRECT_WITHOUT_ANSWER;

        if (configService.isPropertySearcherAbleToRespondViaMail(conversation.getApplication())) {
            fromEmail = addIdToEmail(email, conversation.getExternalId());
            messengerPsNewMessageDirect = MailTemplate.MESSENGER_PS_NEW_MESSAGE_DIRECT;
        }

        PropertySearcherProfileMailBrokerContainer container = new PropertySearcherProfileMailBrokerContainer(
                conversation.getApplication().getProperty().getCustomer().getId(),
                conversation.getApplication().getUserProfile().getId(), fromEmail,
                messengerPsNewMessageDirect, EMAIL_SUBJECT, model, attachments);

        sendPropertySearcherEmailMessage(container, conversation.getApplication().getUserProfile());
    }

    private String addIdToEmail(String email, String id) {
        return email.replaceFirst(SYMBOL_AT, SYMBOL_PLUS + id + SYMBOL_AT);
    }
}
