package de.immomio.service.shared.conversation;

import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.data.shared.entity.conversation.ConversationMessageBean;
import de.immomio.model.repository.core.shared.conversation.BaseConversationMessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Niklas Lindemann
 */
public abstract class AbstractConversationMessageService<CMR extends BaseConversationMessageRepository>  {

    protected CMR conversationMessageRepository;

    public AbstractConversationMessageService(CMR conversationMessageRepository) {
        this.conversationMessageRepository = conversationMessageRepository;
    }

    public abstract Page<ConversationMessageBean> findByConversation(Conversation conversation, Pageable pageable);
    public abstract List<ConversationMessageBean> getUnreadMessages(Conversation conversation);
}
