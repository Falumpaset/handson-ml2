package de.immomio.model.repository.landlord.conversation;

import de.immomio.data.landlord.entity.conversation.ConversationMessageTemplate;
import de.immomio.model.repository.core.landlord.conversation.BaseConversationMessageTemplateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface ConversationMessageTemplateRepository extends BaseConversationMessageTemplateRepository {

    @Query("SELECT t from ConversationMessageTemplate t where t.customer = ?#{principal.customer}")
    Page<ConversationMessageTemplate> findAllForCustomer(Pageable pageable);


    @Override
    @PostAuthorize("returnObject?.get()?.customer?.id == principal?.customer?.id")
    Optional<ConversationMessageTemplate> findById(@Param("id") Long id);
}
