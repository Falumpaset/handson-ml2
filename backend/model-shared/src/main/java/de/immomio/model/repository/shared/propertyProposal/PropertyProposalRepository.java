package de.immomio.model.repository.shared.propertyProposal;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.model.repository.core.shared.propertyProposal.BasePropertyProposalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Maik Kingma
 */

@Repository
public interface PropertyProposalRepository extends BasePropertyProposalRepository, PropertyProposalRepositoryCustom {

    String SEARCHVALUE_PARAM = "searchvalue";

    @Query("SELECT o FROM #{#entityName} o " +
            "WHERE o.userProfile.user = :user AND o.state = 'OFFERED' AND NOT EXISTS (select t from o.property.tenant t)")
    Page<PropertyProposal> findOfferedForPs(PropertySearcherUser user, Pageable pageable);

    @Override
    List<PropertyProposal> findAll();

    @Query(value = "SELECT * FROM shared.propertyproposal AS pp " +
            "JOIN propertysearcher.user_profile as u on pp.user_profile_id = u.id " +
            "INNER JOIN landlord.property AS p on p.id = pp.property_id " +
            "AND p.customer_id = :#{principal.customer.id} " +
            "and u.id in (select u1.id from propertysearcher.user_profile u1 " +
            "where u1.email || ' ' || (u1.data ->> 'firstname') || ' ' || (u1.data ->> 'name') " +
            "ilike '%' || :searchvalue || '%') " +
            "AND pp.state in ('PROSPECT','OFFERED','DENIEDBYPS')",
            nativeQuery = true)
    List<PropertyProposal> findByPsNameOrEmail(@Param(SEARCHVALUE_PARAM) String searchValue);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM shared.propertyproposal pp where pp.searchprofile_id in (:searchProfileIds) AND pp.state = 'PROSPECT'", nativeQuery = true)
    void customDeleteBySearchProfile(@Param("searchProfileIds") List<Long> searchProfileIds);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM shared.propertyproposal pp where pp.user_profile_id = :userProfileId AND pp.state = 'PROSPECT'", nativeQuery = true)
    void customDeleteByUser(@Param("userProfileId") Long userProfileId);

    @Override
    void delete(PropertyProposal propertyProposal);

    @Override
    @Query("SELECT pp FROM #{#entityName} pp WHERE pp.userProfile = :userProfile AND pp.property = :property")
    PropertyProposal findByUserProfileAndProperty(@Param("userProfile") PropertySearcherUserProfile userProfile, @Param("property") Property property);
}