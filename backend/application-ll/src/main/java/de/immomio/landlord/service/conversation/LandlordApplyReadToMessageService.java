package de.immomio.landlord.service.conversation;

import de.immomio.data.base.type.conversation.ConversationMessageSender;
import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.data.shared.entity.conversation.ConversationMessage;
import de.immomio.model.repository.shared.conversation.ConversationMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */
@Component
@Slf4j
public class LandlordApplyReadToMessageService {
    private ConversationMessageRepository conversationMessageRepository;

    @Autowired
    public LandlordApplyReadToMessageService(ConversationMessageRepository conversationMessageRepository) {
        this.conversationMessageRepository = conversationMessageRepository;
    }

    @Async
    public void applyReadStatus(Conversation conversation) {
        conversationMessageRepository.updateReadStatus(ConversationMessageSender.PROPERTYSEARCHER, conversation);
    }
    private boolean messageGotRead(ConversationMessage conversationMessage) {
        return conversationMessage.getSender() == ConversationMessageSender.PROPERTYSEARCHER && !conversationMessage.isRead();
    }
}
