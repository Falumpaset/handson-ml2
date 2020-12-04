package de.immomio.service.conversation;

import de.immomio.data.base.type.conversation.ConversationMessageSender;
import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.model.repository.shared.conversation.ConversationMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */
@Slf4j
@Component
public class PropertysearcherApplyReadToMessageService {
    private ConversationMessageRepository conversationMessageRepository;

    @Autowired
    public PropertysearcherApplyReadToMessageService(ConversationMessageRepository conversationMessageRepository) {
        this.conversationMessageRepository = conversationMessageRepository;
    }

    @Async
    public void applyReadStatus(Conversation conversation) {
        conversationMessageRepository.updateReadStatus(ConversationMessageSender.LANDLORD, conversation);
    }
}
