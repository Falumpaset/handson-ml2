package de.immomio.landlord.service.conversation;

import de.immomio.data.base.type.conversation.ConversationMessageSender;
import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.data.shared.entity.conversation.ConversationMessage;
import de.immomio.data.shared.entity.conversation.ConversationMessageBean;
import de.immomio.model.repository.shared.conversation.ConversationMessageRepository;
import de.immomio.service.shared.conversation.AbstractConversationMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Service
public class LandlordConversationMessageService extends AbstractConversationMessageService<ConversationMessageRepository> {

    private LandlordApplyReadToMessageService applyReadToMessageService;

    @Autowired
    public LandlordConversationMessageService(
            ConversationMessageRepository conversationMessageRepository,
            LandlordApplyReadToMessageService applyReadToMessageService
    ) {
        super(conversationMessageRepository);
        this.applyReadToMessageService = applyReadToMessageService;
    }

    public Page<ConversationMessageBean> findByConversation(Conversation conversation, Pageable pageable) {
        Page<ConversationMessage> messages = conversationMessageRepository.findByConversation(conversation, pageable);
        List<ConversationMessageBean> sortedMessages = messages.getContent().stream()
                .map(ConversationMessageBean::new)
                .sorted(Comparator.comparing(ConversationMessageBean::getCreated))
                .collect(Collectors.toList());
        applyReadToMessageService.applyReadStatus(conversation);

        return new PageImpl<>(sortedMessages, pageable, messages.getTotalElements());
    }

    public List<ConversationMessageBean> getUnreadMessages(Conversation conversation) {
        List<ConversationMessage> messages = conversationMessageRepository.getUnreadMessages(conversation.getId(), ConversationMessageSender.PROPERTYSEARCHER);
        List<ConversationMessageBean> sortedMessages = messages.stream()
                .map(ConversationMessageBean::new)
                .sorted(Comparator.comparing(ConversationMessageBean::getCreated))
                .collect(Collectors.toList());
        applyReadToMessageService.applyReadStatus(conversation);

        return sortedMessages;
    }

    public Long countUnread(List<String> agents) {
        return conversationMessageRepository.countUnreadForLandlord(agents);
    }
}
