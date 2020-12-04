package de.immomio.landlord.service.conversation;

import de.immomio.beans.shared.conversation.ConversationMessageCreateBean;
import de.immomio.beans.shared.conversation.ConversationMessageCreateBulkBean;
import de.immomio.data.base.bean.MessengerFilterBean;
import de.immomio.data.base.type.conversation.ConversationMessageSender;
import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.landlord.bean.user.AgentInfo;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.data.shared.entity.conversation.ConversationBean;
import de.immomio.data.shared.entity.conversation.ConversationMessageBean;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.model.repository.shared.conversation.ConversationMessageRepository;
import de.immomio.model.repository.shared.conversation.ConversationRepository;
import de.immomio.service.conversation.ConversationConfigService;
import de.immomio.service.shared.conversation.AbstractConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Freddy Sawma
 */

@Service
public class LandlordConversationService
        extends AbstractConversationService<ConversationRepository, ConversationMessageRepository> {

    private final ConversationConfigService validationService;
    private final PropertyApplicationRepository applicationRepository;
    private final LandlordConversationMessageAsEmailNotificationService landlordConversationMessageAsEmailNotificationService;

    @Autowired
    public LandlordConversationService(ConversationRepository conversationRepository,
            ConversationMessageRepository conversationMessageRepository,
            LandlordConversationMessageAsEmailNotificationService landlordConversationMessageAsEmailNotificationService,
            ConversationConfigService validationService, PropertyApplicationRepository applicationRepository) {
        super(conversationRepository, conversationMessageRepository);
        this.landlordConversationMessageAsEmailNotificationService = landlordConversationMessageAsEmailNotificationService;
        this.validationService = validationService;
        this.applicationRepository = applicationRepository;
    }

    public List<ConversationBean> search(MessengerFilterBean messengerAgentFilterBean) {
        List<Long> users = messengerAgentFilterBean.getAgents();
        String searchTerm = messengerAgentFilterBean.getSearchTerm();
        searchTerm = prepareSearchTerm(searchTerm);
        users = prepareListQueryTerm(users);
        List<Conversation> conversations = conversationRepository.findAllForLandlord(searchTerm, users);
        List<ConversationBean> conversationBeans = convertConversations(conversations);
        conversationBeans.forEach(this::appendMissedData);

        return conversationBeans;
    }

    public List<ConversationBean> findByProperty(Property property) {
        List<Conversation> conversations = conversationRepository.findByProperty(property);
        List<ConversationBean> conversationBeans = convertConversations(conversations);
        conversationBeans.forEach(this::appendMissedData);

        return conversationBeans;
    }

    public void block(Conversation conversation, boolean blocked) {
        conversation.setBlocked(blocked);
        conversationRepository.save(conversation);
    }

    public void createMessages(ConversationMessageCreateBulkBean messageBean, LandlordUser user) {
        AgentInfo agent = new AgentInfo(user);
        applicationRepository.findAllByIdInAndPropertyCustomer(messageBean.getRecipientApplicationIds(), user.getCustomer())
                .forEach(application -> createMessage(messageBean, application, ConversationMessageSender.LANDLORD, agent));
    }

    @Override
    public ConversationMessageBean createMessage(ConversationMessageCreateBean messageBean,
            PropertyApplication application, ConversationMessageSender sender, AgentInfo agentInfo) {
        ConversationMessageBean conversationMessageBean = super.createMessage(messageBean, application, sender,
                agentInfo);

        if (sendMessageViaEmail(application)) {
            Conversation conversation = conversationRepository.findById(application.getId()).orElseThrow();
            landlordConversationMessageAsEmailNotificationService.sendMessageAsEmail(conversation,
                    conversationMessageBean);
        }

        return conversationMessageBean;
    }

    private boolean sendMessageViaEmail(PropertyApplication application) {
        PropertyType propertyType = application.getProperty().getType();
        if (propertyType == PropertyType.COMMERCIAL || propertyType == PropertyType.GARAGE) {
            return true;
        }

        PropertySearcherUserProfileType userProfileType = application.getUserProfile().getType();
        return userProfileType == PropertySearcherUserProfileType.GUEST || userProfileType == PropertySearcherUserProfileType.ANONYMOUS;
    }

    private void appendMissedData(ConversationBean conversation) {
        boolean allowedToSendMessage = validationService.isAllowedToSendMessage(conversation.getApplication().getId());
        conversation.setUserAllowedToSendMessage(allowedToSendMessage);
        Long unread = conversationMessageRepository.countUnreadByConversation(conversation.getId(),
                ConversationMessageSender.PROPERTYSEARCHER);
        conversation.setUnreadMessages(unread);
    }

    private List<Long> prepareListQueryTerm(List<Long> values) {
        if (values != null && values.isEmpty()) {
            return null;
        }
        return values;
    }

    private String prepareSearchTerm(String searchTerm) {
        if (searchTerm == null) {
            searchTerm = "";
        }
        return searchTerm;
    }
}
