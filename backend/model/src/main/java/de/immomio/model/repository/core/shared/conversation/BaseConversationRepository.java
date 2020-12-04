package de.immomio.model.repository.core.shared.conversation;

import de.immomio.data.base.type.conversation.ConversationMessageSender;
import de.immomio.data.shared.entity.conversation.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;
import java.util.List;

/**
 * @author Niklas Lindemann
 */
@RepositoryRestResource(path = "conversations")
public interface BaseConversationRepository extends JpaRepository<Conversation, Long> {

    @RestResource(exported = false)
    @Query("SELECT c from Conversation c inner join c.messages m where m.sender = :sender and m.read = false and m.created between :from and :to GROUP BY c")
    List<Conversation> getHavingUnreadMessagesBySenderBetween(@Param("sender") ConversationMessageSender sender, @Param("from") Date from, @Param("to") Date to);

    @RestResource(exported = false)
    Conversation getFirstByExternalId(String externalId);

}
