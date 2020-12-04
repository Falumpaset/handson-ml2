package de.immomio.model.repository.shared.application;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.core.shared.application.BasePropertyApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyApplicationRepository extends BasePropertyApplicationRepository,
        PropertyApplicationRepositoryCustom {

    String APPLICATION_PARAM = "application";
    String PROPERTY_PARAM = "property";
    String SEARCHVALUE_PARAM = "searchvalue";
    String APPLICATION_LIST_PARAM = "applicationIds";
    String CUSTOMER_PARAM = "customer";

    @Override
    @RestResource(exported = false)
    Page<PropertyApplication> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    Optional<PropertyApplication> findById(@Param(ID_PARAM) Long id);

    @RestResource(exported = false)
    @Query("SELECT o FROM #{#entityName} o where o.id = :id and o.property.customer = :customer")
    Optional<PropertyApplication> findByIdAndCustomer(@Param("id") Long id, @Param("customer") LandlordCustomer customer);

    @Override
    @RestResource(exported = false)
    PropertyApplication save(@Param(APPLICATION_PARAM) PropertyApplication application);

    @RestResource(exported = false)
    List<PropertyApplication> findAllByIdInAndPropertyCustomer(List<Long> applicationIds, LandlordCustomer customer);

    @Override
    @PreAuthorize("#application.userProfile.user.id == principal?.id")
    void delete(@Param(APPLICATION_PARAM) PropertyApplication application);

    @Override
    @RestResource(exported = false)
    @PreAuthorize("#property.customer == principal?.customer")
    Page<PropertyApplication> findByProperty(@Param(PROPERTY_PARAM) Property property, Pageable pageable);

    @RestResource(exported = false)
    @PreAuthorize("#userProfile.user.id == principal?.id")
    @Query("SELECT a from PropertyApplication a where a.property.type = 'FLAT' and a.userProfile = :userProfile")
    Page<PropertyApplication> findFlatApplicationByUserProfile(@Param("userProfile") PropertySearcherUserProfile userProfile, Pageable pageable);

    @RestResource(exported = false)
    @PreAuthorize("#userProfile.user.id == principal?.id")
    @Query("SELECT a from PropertyApplication a where a.userProfile = :userProfile and ((SELECT count (c) FROM Conversation c where c.application = a) = 0)")
    List<PropertyApplication> findByUserProfileAndConversationIsNull(@Param("userProfile") PropertySearcherUserProfile userProfile);

    @RestResource(exported = false)
    @Query(value = "SELECT * FROM shared.application AS ap " +
            "JOIN propertysearcher.user_profile as u on ap.user_profile_id = u.id " +
            "INNER JOIN landlord.property AS p on p.id = ap.property_id " +
            "AND p.customer_id = :#{principal.customer.id} " +
            "and u.id in (select u1.id from propertysearcher.user_profile u1 " +
            "where u1.email || ' ' || (u1.data ->> 'firstname') || ' ' || (u1.data ->> 'name') " +
            "ilike '%' || :searchvalue || '%') and ap.archived is null",
            nativeQuery = true)
    List<PropertyApplication> findByPsNameOrEmail(@Param(SEARCHVALUE_PARAM) String searchValue);

    @RestResource(exported = false)
    @PostAuthorize("returnObject == null || returnObject?.userProfile.user.type != 'REGISTERED' || returnObject?.property?.customer?.id == principal?.customer?.id || returnObject?.userProfile.user.id == principal?.id")
    @Query(value = "SELECT app FROM #{#entityName} app where app.userProfile.id = :userProfileId and app.property.id = :propertyId")
    PropertyApplication findByUserProfileIdAndPropertyId(@Param("userProfileId") Long userProfileId, @Param("propertyId") Long propertyId);

    PropertyApplication findFirstByUserProfileAndProperty(PropertySearcherUserProfile userProfile, Property property);

    @Query(value = "SELECT app FROM #{#entityName} app WHERE app.property.tenant.userProfile.user.id = ?#{principal.id} " +
            "and app.userProfile.user.id = ?#{principal.id} " +
            "and (app.userProfile.user.rentedApplicationsFetched is null " +
            "or app.property.tenant.created > app.userProfile.user.rentedApplicationsFetched)")
    List<PropertyApplication> findApplicationsWithRentedFlatsById();

    @RestResource(exported = false)
    List<PropertyApplication> findByUserProfileId(Long userProfileId);

    @RestResource(exported = false)
    @Query("SELECT o FROM #{#entityName} o where o.property.customer = :customer and o.userProfile = :userProfile")
    List<PropertyApplication> findByLandlordAndUserProfile(
            @Param("customer") LandlordCustomer customer,
            @Param("userProfile") PropertySearcherUserProfile userProfile);

    @RestResource(exported = false)
    @PreAuthorize("#property.customer == principal?.customer")
    @Query("SELECT app FROM #{#entityName} app " +
            " WHERE app.property = :property and app.status = 'ACCEPTED'" +
            "       AND NOT EXISTS (SELECT aa.id FROM app.appointmentAcceptances aa WHERE aa.state = 'ACTIVE') ")
    Page<PropertyApplication> findAllWithoutAppointmentAcceptance(
            @Param("property") Property property,
            Pageable pageable
    );

    @Transactional
    @Modifying
    @RestResource(exported = false)
    @Query(value = "UPDATE shared.application SET status = 'UNANSWERED' WHERE property_id = :property AND status != 'NO_INTENT'", nativeQuery = true)
    void resetApplications(@Param("property") Long propertyId);

    @RestResource(exported = false)
    @Query("select count(a) from PropertyApplication a where a.property.id = :propertyId and a.created > :lastVisited")
    Long getCountUnseenApplications(@Param("propertyId") Long propertyId, @Param("lastVisited") Date lastVisited);

    @RestResource(exported = false)
    @Query("select a from PropertyApplication a where a.property.id = :propertyId and a.userProfile = :userProfile")
    Optional<PropertyApplication> getByUserProfileAndProperty(@Param("userProfile") PropertySearcherUserProfile userProfile, @Param("propertyId") Long propertyId);

    @Transactional
    @Modifying
    @RestResource(exported = false)
    @Query(value = "UPDATE PropertyApplication set seen = :date where id = :id")
    void updateSeen(@Param("id") Long id, @Param("date") Date date);

    @RestResource(exported = false)
    @Query(value = "select pa from PropertyApplication pa where pa.userProfile.email = :email and pa.property = :property")
    PropertyApplication findByEmailAndProperty(@Param("email") String email, @Param("property") Property property);

    @RestResource(exported = false)
    List<PropertyApplication> findByUserProfileIn(List<PropertySearcherUserProfile> profiles);

    @RestResource(exported = false)
    List<PropertyApplication> findAllByIdInAndPropertyOrderByScoreDesc(List<Long> applicationIds, Property property);
}
