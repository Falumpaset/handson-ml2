package de.immomio.model.repository.core.shared.propertyProposal;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposalState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Maik Kingma
 */

@Repository
public interface BasePropertyProposalRepository extends JpaRepository<PropertyProposal, Long> {

    String PROPERTY_ID_PARAM = "propertyId";

    @RestResource(exported = false)
    List<PropertyProposal> findAllByPropertyId(@Param(PROPERTY_ID_PARAM) Long propertyId);

    @RestResource(exported = false)
    List<PropertyProposal> findAllByUserProfile(PropertySearcherUserProfile userProfile);

    @RestResource(exported = false)
    @Query(value = "SELECT pp FROM PropertyProposal pp WHERE pp.userProfile = :userProfile AND pp.property = :property")
    PropertyProposal findByUserProfileAndProperty(@Param("userProfile") PropertySearcherUserProfile userProfile, @Param("property") Property property);

    @RestResource(exported = false)
    Long countByCreatedBetween(Date dateFrom, Date dateTo);

    @RestResource(exported = false)
    Long countByAcceptedBetweenAndState(Date fromDate, Date toDate, PropertyProposalState state);

    @RestResource(exported = false)
    Long countByOfferedBetween(Date fromDate, Date toDate);

    @RestResource(exported = false)
    @Query(value = "SELECT COUNT(pp) FROM PropertyProposal pp where pp.userProfile.searchUntil > CURRENT_TIMESTAMP")
    Long countExcludingInactiveUsers();

    @Transactional
    @RestResource(exported = false)
    @Modifying
    @Query(value = "UPDATE shared.propertyproposal pp SET score = :score, updated = NOW(), custom_question_score = CAST(:customQuestionScore as jsonb) where pp.id = :id", nativeQuery = true)
    void customUpdateScore(@Param("id") Long id, @Param("score") Double score, @Param("customQuestionScore") String customQuestionScore);

    @Transactional
    @RestResource(exported = false)
    @Modifying
    @Query(value = "UPDATE PropertyProposal pp SET state = 'OFFERED' , updated = CURRENT_TIMESTAMP, offered = CURRENT_TIMESTAMP where pp.id = :id")
    void customSetOffered(@Param("id") Long id);

    @Modifying
    @Transactional
    @RestResource(exported = false)
    @Query(value = "DELETE FROM shared.propertyproposal pp where pp.id IN (:ids)", nativeQuery = true)
    void customDelete(@Param("ids") List<Long> ids);

    @RestResource(exported = false)
    List<PropertyProposal> findAllByStateAndPropertyIn(PropertyProposalState state, List<Property> properties);

    @RestResource(exported = false)
    @Query("SELECT COUNT(o) from PropertyProposal o where o.property.id = :propertyId and o.state in ('DENIEDBYPS', 'OFFERED', 'PROSPECT')")
    Long countByProperty(@Param("propertyId") Long propertyId);

    @RestResource(exported = false)
    @Query(value = "SELECT DISTINCT pp.property_id FROM shared.propertyproposal pp WHERE pp.id IN(:ids)", nativeQuery = true)
    List<Long> findAllPropertyIdsForProposals(@Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "UPDATE shared.propertyproposal set state = 'PROSPECT' where property_id = :property AND state != 'DENIEDBYPS'", nativeQuery = true)
    @RestResource(exported = false)
    void resetProposals(@Param("property") Long propertyId);

    @RestResource(exported = false)
    List<PropertyProposal> findByUserProfileAndPropertyCustomer(PropertySearcherUserProfile userProfile, LandlordCustomer customer);

}
