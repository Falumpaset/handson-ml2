package de.immomio.model.repository.core.landlord.conversation;

import de.immomio.data.landlord.entity.conversation.ConversationMessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseConversationMessageTemplateRepository extends JpaRepository<ConversationMessageTemplate, Long> {
}
