package de.immomio.service.conversation;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.conversation.ConversationMessageSender;
import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.data.shared.entity.conversation.ConversationBean;
import de.immomio.model.repository.shared.conversation.ConversationMessageRepository;
import de.immomio.model.repository.shared.conversation.ConversationRepository;
import de.immomio.service.shared.conversation.AbstractConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Niklas Lindemann
 */
@Service
public class PropertysearcherConversationService extends AbstractConversationService<ConversationRepository, ConversationMessageRepository> {

    public static final List<PropertyType> CONVERSATION_PROPERTY_TYPES = List.of(PropertyType.FLAT);
    public static final String EMPTY_STRING = "";

    private ConversationConfigService validationService;

    @Autowired
    public PropertysearcherConversationService(
            ConversationRepository conversationRepository,
            ConversationMessageRepository conversationMessageRepository,
            ConversationConfigService validationService
    ) {
        super(conversationRepository, conversationMessageRepository);
        this.validationService = validationService;
    }

    public List<ConversationBean> search(String searchTerm) {
        if (searchTerm == null) {
            searchTerm = EMPTY_STRING;
        }
        List<Conversation> conversations = conversationRepository.findAllWherePropertyTypeInForPs(searchTerm, CONVERSATION_PROPERTY_TYPES);
        List<ConversationBean> conversationBeans = convertConversations(conversations);
        conversationBeans.forEach(this::appendMissedData);

        return conversationBeans;
    }

    public void validateBlockedConversationFromApplication(Long applicationId) {
        Optional<Conversation> conversationOpt = conversationRepository.findById(applicationId);
        conversationOpt.ifPresent(conversation -> {
            if (conversation.isBlocked()) {
                throw new ApiValidationException("CONVERSATION_IS_BLOCKED_L");
            }
        });
    }

    private void appendMissedData(ConversationBean conversation) {
        boolean allowedToSendMessage = validationService.isAllowedToSendMessage(conversation.getApplication().getId());
        conversation.setUserAllowedToSendMessage(allowedToSendMessage);
        Long unread = conversationMessageRepository.countUnreadByConversation(conversation.getId(), ConversationMessageSender.LANDLORD);
        conversation.setUnreadMessages(unread);
    }

}
