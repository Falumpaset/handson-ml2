package de.immomio.recipient.service.conversation;

import de.immomio.beans.shared.conversation.ConversationMessageCreateBean;
import de.immomio.data.base.type.conversation.ConversationMessageSender;
import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.model.repository.core.shared.conversation.BaseConversationMessageRepository;
import de.immomio.model.repository.core.shared.conversation.BaseConversationRepository;
import de.immomio.service.shared.conversation.AbstractConversationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Fabian Beck.
 */

@Slf4j
@Service
public class ConversationMessageService
        extends AbstractConversationService<BaseConversationRepository, BaseConversationMessageRepository> {

    @Autowired
    public ConversationMessageService(BaseConversationRepository conversationRepository,
            BaseConversationMessageRepository conversationMessageRepository) {
        super(conversationRepository, conversationMessageRepository);
    }

    public void addNewMessage(Conversation conversation, ConversationMessageCreateBean conversationMessageCreateBean) {
        createMessage(conversationMessageCreateBean, conversation.getApplication(),
                ConversationMessageSender.PROPERTYSEARCHER);
    }
}
