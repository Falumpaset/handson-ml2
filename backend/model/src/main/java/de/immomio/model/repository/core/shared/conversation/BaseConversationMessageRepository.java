package de.immomio.model.repository.core.shared.conversation;

import de.immomio.data.landlord.bean.user.AgentInfo;
import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.data.shared.entity.conversation.ConversationMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * @author Niklas Lindemann
 */
@RepositoryRestResource(path = "conversationMessages")
public interface BaseConversationMessageRepository extends JpaRepository<ConversationMessage, Long> {

    @RestResource(exported = false)
    Page<ConversationMessage> findByConversation(@Param("conversation") Conversation conversation, Pageable pageable);

    @RestResource(exported = false)
    @Query("SELECT o.agentInfo from ConversationMessage o where o.conversation.id = :conversation and o.agentInfo is not null group by o.agentInfo")
    List<AgentInfo> getAgentsByConversation(@Param("conversation") Long conversation);
}
