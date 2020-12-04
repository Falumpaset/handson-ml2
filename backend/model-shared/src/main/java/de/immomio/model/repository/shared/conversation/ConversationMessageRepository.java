package de.immomio.model.repository.shared.conversation;

import de.immomio.data.base.type.conversation.ConversationMessageSender;
import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.data.shared.entity.conversation.ConversationMessage;
import de.immomio.model.repository.core.shared.conversation.BaseConversationMessageRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Freddy Sawma
 */

@RepositoryRestResource(path = "conversationMessages")
public interface ConversationMessageRepository extends BaseConversationMessageRepository {

    @RestResource(exported = false)
    @PreAuthorize("#conversation.application?.property?.customer?.id == principal?.customer?.id " +
            "|| #conversation.application?.userProfile?.user?.id == principal?.id")
    @Query("SELECT o from ConversationMessage o where o.conversation = :conversation order by created desc")
    Page<ConversationMessage> findByConversation(@Param("conversation") Conversation conversation, Pageable pageable);

    @Modifying
    @Transactional
    @RestResource(exported = false)
    @Query("update ConversationMessage o set o.read = true where o.sender = :sender and o.conversation = :conversation")
    void updateReadStatus(@Param("sender") ConversationMessageSender sender, @Param("conversation") Conversation conversation);


    @RestResource(exported = false)
    @Query("SELECT COUNT(o) from ConversationMessage o where o.conversation.id = :conversationId AND o.sender = :sender and o.read = false")
    Long countUnreadByConversation(@Param("conversationId") Long conversationId, @Param("sender") ConversationMessageSender sender);

    @RestResource(exported = false)
    @Query("SELECT o from ConversationMessage o where o.conversation.id = :conversationId AND o.sender = :sender and o.read = false")
    List<ConversationMessage> getUnreadMessages(@Param("conversationId") Long conversationId, @Param("sender") ConversationMessageSender sender);

    @RestResource(exported = false)
    @Query("SELECT COUNT(o) from ConversationMessage o " +
            "where o.conversation.customer.id = ?#{principal.customer.id} " +
            "AND o.sender = 'PROPERTYSEARCHER' and o.read = false " +
            "AND ((:agents) is null or function('jsonb_extract_path_text', o.agentInfo, 'id') in (:agents))")
    Long countUnreadForLandlord(@Param("agents") List<String> agents);

    @RestResource(exported = false)
    @Query("SELECT COUNT(o) from ConversationMessage o where o.conversation.application.userProfile.user.id = ?#{principal.id} AND o.conversation.application.property.type = 'FLAT' " +
            "AND o.sender = 'LANDLORD' and o.read = false")
    Long countUnreadForPs();
}
