package de.immomio.service.conversation;

import de.immomio.data.base.type.conversation.ConversationMessageSender;
import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.core.shared.conversation.BaseConversationRepository;
import de.immomio.service.shared.EmailModelProvider;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Service
public class ConversationEmailService {

    private final BaseConversationRepository conversationRepository;
    private final PropertySearcherMailSender mailSender;
    private final EmailModelProvider emailModelProvider;

    public ConversationEmailService(BaseConversationRepository conversationRepository,
            PropertySearcherMailSender mailSender,
            EmailModelProvider emailModelProvider) {
        this.conversationRepository = conversationRepository;
        this.mailSender = mailSender;
        this.emailModelProvider = emailModelProvider;
    }

    public void sendNewMessageEmails() {
        Date to = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Date from = Date.from(LocalDateTime.now().minusMinutes(15).atZone(ZoneId.systemDefault()).toInstant());
        List<Conversation> conversations = conversationRepository.getHavingUnreadMessagesBySenderBetween(ConversationMessageSender.LANDLORD, from, to);

        conversations.stream()
                .map(Conversation::getApplication)
                .filter(propertyApplication -> propertyApplication.getProperty().getType() == PropertyType.FLAT)
                .map(PropertyApplication::getUserProfile)
                .distinct()
                .forEach(userProfile -> mailSender.send(userProfile, MailTemplate.MESSENGER_PS_NEW_MESSAGE_NOTIFICATION, "messenger.ps.new.message.subject",
                        emailModelProvider.createModelPsUserAndReturnUrl(userProfile)));
    }
}
