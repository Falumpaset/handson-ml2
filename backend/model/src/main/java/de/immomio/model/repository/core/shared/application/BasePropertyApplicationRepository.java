package de.immomio.model.repository.core.shared.application;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BasePropertyApplicationRepository extends JpaRepository<PropertyApplication, Long> {

    String PROPERTY_PARAM = "property";
    String DATE_PARAM = "date";
    String PROPERTY_ID_PARAM = "propertyId";
    String ID_PARAM = "id";
    String TENANT_USER_PARAM = "tenantUser";

    @RestResource(exported = false)
    List<PropertyApplication> findAllByPropertyAndCreatedBetween(
            @Param(PROPERTY_PARAM) Property property,
            @Param("after") Date after,
            @Param("before") Date before
    );

    @RestResource(exported = false)
    @Query("SELECT o FROM #{#entityName} o " +
            "WHERE o.property.customer.id = ?#{principal.customer.id} " +
            "AND o.created >= :after ")
    List<PropertyApplication> findAllByCreatedAfter(
            @Param("after") Date date
    );

    Page<PropertyApplication> findByProperty(@Param(PROPERTY_PARAM) Property property, Pageable pageable);

    @RestResource(exported = false)
    Long countByProperty(@Param(PROPERTY_PARAM) Property property);

    Page<PropertyApplication> findByUserProfile(@Param("userProfile") PropertySearcherUserProfile userProfile, Pageable pageable);

    @RestResource(exported = false)
    List<PropertyApplication> findAllByUserProfile(@Param("userProfile") PropertySearcherUserProfile userProfile);

    List<PropertyApplication> findAllByPropertyId(@Param(PROPERTY_ID_PARAM) Long propertyId);

    List<PropertyApplication> findByUserProfileAndProperty(PropertySearcherUserProfile userProfile, Property property);

    List<PropertyApplication> findByUserProfileAndPropertyIn(PropertySearcherUserProfile userProfile, List<Property> property);

    List<PropertyApplication> findByUserProfileAndPropertyCustomer(PropertySearcherUserProfile userProfile, LandlordCustomer customer);


    @RestResource(exported = false)
    @Query("SELECT COUNT(ap) " +
            "FROM PropertyApplication as ap " +
            "WHERE ap.property.id = :propertyId and ap.archived is null")
    Long getSizeOfApplications(@Param(PROPERTY_ID_PARAM) Long propertyId);

    @RestResource(exported = false)
    @Query("SELECT COUNT(ap) FROM PropertyApplication as ap " +
            "WHERE ap.property.customer.id = ?#{principal.customer.id} " +
            "AND ap.property.id = :propertyId " +
            "AND ap.created >= :date")
    Long countByPropertyAfterDate(@Param(PROPERTY_ID_PARAM) Long propertyId, @Param(DATE_PARAM) Date after);

    @RestResource(exported = false)
    @Query("SELECT COUNT(ap) " +
            "FROM PropertyApplication as ap " +
            "WHERE ap.property.id = :propertyId " +
            "AND ap.status = 'ACCEPTED' and ap.archived is null")
    Long getSizeOfInvitees(@Param(PROPERTY_ID_PARAM) Long propertyId);

    Optional<PropertyApplication> findFirstByUserProfileOrderByCreatedAsc(PropertySearcherUserProfile userProfile);

    @RestResource(exported = false)
    @Query("SELECT o FROM #{#entityName} o JOIN FETCH o.property p JOIN FETCH p.propertyApplications ap WHERE o.id = :id")
    Optional<PropertyApplication> findById(@Param("id") Long id);

    @Transactional
    @Modifying
    @RestResource(exported = false)
    @Query("UPDATE PropertyApplication o set o.archived = CURRENT_TIMESTAMP " +
            "where o.property = :property and o.archived is null")
    void archiveApplication(
            @Param(PROPERTY_PARAM) Property property
    );

    @Transactional
    @Modifying
    @RestResource(exported = false)
    @Query("UPDATE PropertyApplication o set o.archived = CURRENT_TIMESTAMP " +
            "where o.userProfile <> :tenantUserProfile and o.property = :property and o.archived is null")
    void archiveApplication(
            @Param("property") Property property,
            @Param("tenantUserProfile") PropertySearcherUserProfile tenantUserProfile
    );

    @RestResource(exported = false)
    boolean existsByPropertyAndUserProfile(Property property, PropertySearcherUserProfile userProfile);

    boolean existsByUserProfileUserEmailAndProperty(String email, Property property);
}
