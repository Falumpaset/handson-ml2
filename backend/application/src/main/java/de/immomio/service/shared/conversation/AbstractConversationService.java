package de.immomio.service.shared.conversation;

import de.immomio.beans.shared.conversation.ConversationMessageCreateBean;
import de.immomio.data.base.type.conversation.ConversationMessageSender;
import de.immomio.data.base.type.conversation.ConversationMessageSource;
import de.immomio.data.landlord.bean.user.AgentInfo;
import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.data.shared.entity.conversation.ConversationBean;
import de.immomio.data.shared.entity.conversation.ConversationMessage;
import de.immomio.data.shared.entity.conversation.ConversationMessageBean;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.core.shared.conversation.BaseConversationMessageRepository;
import de.immomio.model.repository.core.shared.conversation.BaseConversationRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */
public abstract class AbstractConversationService<
        CR extends BaseConversationRepository,
        CMR extends BaseConversationMessageRepository>  {

    protected CR conversationRepository;
    protected CMR conversationMessageRepository;

    public AbstractConversationService(CR conversationRepository, CMR conversationMessageRepository) {
        this.conversationRepository = conversationRepository;
        this.conversationMessageRepository = conversationMessageRepository;
    }

    public ConversationMessageBean createMessage(
            ConversationMessageCreateBean messageBean,
            PropertyApplication application,
            ConversationMessageSender sender,
            AgentInfo agentInfo
    ) {

        Conversation conversation = conversationRepository.findById(application.getId()).orElseGet(() -> {
            Conversation newConversation = new Conversation();
            newConversation.setApplication(application);
            newConversation.setCustomer(application.getProperty().getCustomer());
            return conversationRepository.save(newConversation);
        });

        ConversationMessage conversationMessage = new ConversationMessage();
        conversationMessage.setMessage(messageBean.getMessage());
        conversationMessage.setAttachments(messageBean.getAttachments());
        conversationMessage.setConversation(conversation);
        conversationMessage.setSender(sender);
        conversationMessage.setSource(ConversationMessageSource.APP);
        conversationMessage.setAgentInfo(agentInfo);

        ConversationMessage saved = conversationMessageRepository.save(conversationMessage);
        return new ConversationMessageBean(saved);
    }

    public ConversationMessageBean createMessage(
            ConversationMessageCreateBean messageBean,
            PropertyApplication application,
            ConversationMessageSender sender
    ) {
        return createMessage(messageBean, application, sender, application.getProperty().getAgentInfo());
    }

    public List<AgentInfo> getParticipatedAgents(Conversation conversation) {
        return conversationMessageRepository.getAgentsByConversation(conversation.getId());
    }

    protected List<ConversationBean> convertConversations(List<Conversation> conversations) {
        List<ConversationBean> conversationBeans = conversations.stream()
                .map(ConversationBean::new)
                .collect(Collectors.toList());

        return conversationBeans;
    }

}
