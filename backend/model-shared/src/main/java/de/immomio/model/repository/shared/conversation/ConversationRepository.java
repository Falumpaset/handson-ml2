package de.immomio.model.repository.shared.conversation;

import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.model.repository.core.shared.conversation.BaseConversationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;

import java.util.List;
import java.util.Optional;

/**
 * @author Freddy Sawma
 */

@RepositoryRestResource(path = "conversations")
public interface ConversationRepository extends BaseConversationRepository {

    @RestResource(exported = false)
    @Query("SELECT (count(m) > 0) from Conversation c join c.messages m where c = :conversation and m.sender = 'LANDLORD'")
    Boolean hasMessagesByLandlord(@Param("conversation") Conversation conversation);

    @Override
    @PostAuthorize("returnObject?.isPresent() ? (returnObject?.get()?.application?.property?.customer?.id == principal?.customer?.id " +
            "|| returnObject?.get()?.application?.userProfile?.user?.id == principal?.id ) : true")
    @RestResource(exported = false)
    Optional<Conversation> findById(Long id);

    @Override
    @RestResource(exported = false)
    Page<Conversation> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    <S extends Conversation> S save(S entity);

    @Override
    @RestResource(exported = false)
    void delete(Conversation entity);

    @RestResource(exported = false)
    @Query("SELECT o from Conversation o where o.customer.id = ?#{principal.customer.id} AND " +
            "((lower(function('jsonb_extract_path_text', o.application.userProfile.data, 'firstname')) || ' ' || " +
            "(lower(function('jsonb_extract_path_text', o.application.userProfile.data, 'name')) ) " +
            " like lower('%' || :search || '%')) OR" +
            "(o.application.userProfile.user.email like lower('%' || :search || '%')) OR " +
            "(lower(coalesce(function('jsonb_extract_path_text', o.application.property.data, 'name'),'')) like lower('%' || :search || '%')) OR  " +
            "(lower(coalesce(function('jsonb_extract_path_text', o.application.property.data, 'externalId'), '')) like lower('%' || :search || '%'))) " +
            "AND (COALESCE(:agents, NULL) IS NULL OR o.application.property.user.id in (:agents)) " +
            "AND o.application.archived is null " +
            "order by o.lastMessageDate desc")
    List<Conversation> findAllForLandlord(@Param("search") String search, @Param("agents") List<Long> agents);

    @RestResource(exported = false)
    @Query("SELECT o from Conversation o where o.application.userProfile.user.id = ?#{principal.id} AND " +
            "(lower(function('jsonb_extract_path_text', o.application.property.data, 'address', 'street')) || ' ' || " +
            "(lower(function('jsonb_extract_path_text', o.application.property.data, 'address', 'houseNumber')) )  || ' ' || " +
            "(lower(function('jsonb_extract_path_text', o.application.property.data, 'address', 'zipCode')) )  || ' ' || " +
            "(lower(function('jsonb_extract_path_text', o.application.property.data, 'address', 'city')) )  || ' ' || " +
            "(lower(coalesce(function('jsonb_extract_path_text', o.application.property.data, 'name'),'')))) " +
            "like lower('%' || :search || '%') AND o.application.archived is null " +
            "and o.application.property.type in :propertyTypes " +
            "order by o.lastMessageDate desc")
    List<Conversation> findAllWherePropertyTypeInForPs(@Param("search") String search, @Param("propertyTypes") List<PropertyType> propertyTypes);

    @RestResource(exported = false)
    @Query("SELECT o from Conversation o where o.application.property = :property AND o.application.archived is null order by o.lastMessageDate desc ")
    List<Conversation> findByProperty(@Param("property") Property property);

}
