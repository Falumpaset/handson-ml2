package de.immomio.model.repository.shared.property;

import de.immomio.data.base.type.property.PropertyStatus;
import de.immomio.data.landlord.bean.property.EntityCountBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.portal.PropertyPortalState;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Bastian Bliemeister
 */

public interface PropertyRepository extends BasePropertyRepository, PropertyRepositoryCustom {

    @Override
    @PreAuthorize("#property.customer == principal?.customer")
    Property save(@Param("property") Property property);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    @PreAuthorize("#property.customer == principal?.customer")
    void delete(@Param("property") Property property);

    @Override
    @RestResource(exported = false)
    @Query(nativeQuery = true,
            value = "SELECT o.* FROM landlord.property o " +
                    " LEFT JOIN shared.application pa ON pa.property_id = o.id" +
                    " LEFT JOIN propertysearcher.user_profile up ON up.id = pa.user_profile_id" +
                    " WHERE ((o.customer_id = ?#{principal.customer.id} OR (up.user_id = ?#{principal.id})))" +
                    " GROUP BY o.id " +
                    " ORDER BY ?#{#pageable}",
            countQuery = "SELECT count(*) FROM (SELECT DISTINCT o FROM landlord.property o " +
                    "LEFT JOIN shared.application pa ON pa.property_id=o.id " +
                    "LEFT JOIN propertysearcher.user_profile up ON up.id = pa.user_profile_id " +
                    "WHERE ((o.customer_id = ?#{principal.customer.id} OR (up.user_id = ?#{principal.id})))" +
                    ") as cnt "
    )
    Page<Property> findAll(Pageable pageable);

    @Override
    @PostAuthorize("returnObject?.present ? (returnObject?.get()?.customer?.id == principal?.customer?.id " +
            "|| @userSecurityService.allowUserToReadProperty(returnObject.get(), principal?.id)) : true")
    Optional<Property> findById(@Param("id") Long id);

    @RestResource(exported = false)
    @Query("select p from Property p where p.id = :id")
    Optional<Property> customFindById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    Page<Property> findByCustomer(
            @Param("customer") LandlordCustomer customer,
            Pageable pageable
    );

    @Override
    @RestResource(exported = false)
    List<Property> findByCustomerIdAndExternalId(
            @Param("customerId") Long customerId,
            @Param("externalId") String externalId
    );

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM landlord.property p " +
                    "INNER JOIN shared.propertytenant t " +
                    "ON p.id = t.property_id " +
                    "WHERE p.customer_id = ?#{principal.customer.id} " +
                    "ORDER BY ?#{#pageable}",
            countQuery = "SELECT count(*) FROM landlord.property p " +
                    "INNER JOIN shared.propertytenant t " +
                    "ON p.id = t.property_id " +
                    "WHERE p.customer_id = ?#{principal.customer.id}")
    Page<Property> findAllRentedProperties(Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM landlord.property p " +
                    "INNER JOIN shared.propertytenant t " +
                    "ON p.id = t.property_id " +
                    "WHERE p.customer_id = :#{principal.customer.id} " +
                    "AND p.user_id IN :userIDs " +
                    "ORDER BY :#{#pageable}",
            countQuery = "SELECT count(*) FROM landlord.property p " +
                    "INNER JOIN shared.propertytenant t " +
                    "ON p.id = t.property_id " +
                    "WHERE p.customer_id = :#{principal.customer.id} " +
                    "AND p.user_id IN :userIDs")
    Page<Property> findAllRentedPropertiesByUsers(@Param("userIDs") Long[] userIDs, Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM landlord.property p " +
                    "WHERE p.customer_id = :#{principal.customer.id} " +
                    "AND LOWER(data->>'name') like LOWER('%' || :name || '%') " +
                    "ORDER BY :#{#pageable}",
            countQuery = "SELECT count(*) FROM landlord.property p " +
                    "WHERE p.customer_id = :#{principal.customer.id} " +
                    "AND LOWER(data->>'name') like LOWER('%' || :name || '%')")
    Page<Property> findByNameIgnoreCaseLike(@Param("name") String name, Pageable pageable);

    @Query("SELECT o FROM #{#entityName} o " +
            "WHERE o.user IN (:users) " +
            "AND o.customer = ?#{principal.customer}")
    Page<Property> findByUsers(
            @Param("users") LandlordUser[] users,
            Pageable pageable
    );

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} p set p.status = :propertyStatus " +
            "WHERE p.id IN (:propertyIds) " +
            "AND p.customer = :customer")
    void updateStatesByIdsAndUser(@Param("propertyIds") List<Long> propertyIds, @Param("customer") LandlordCustomer customer, @Param("propertyStatus") PropertyStatus propertyStatus);

    @RestResource(exported = false)
    boolean existsByCustomerAndExternalId(LandlordCustomer customerId, String externalId);

    @Query("SELECT o FROM #{#entityName} o " +
            "INNER JOIN o.portals p " +
            "WHERE o.customer = ?#{principal.customer} " +
            "AND p.state = :state ")
    Page<Property> findByState(
            @Param("state") PropertyPortalState state,
            Pageable pageable
    );

    /*
    This endpoint will return those flats where all portal states are deactivated
    If only one state is != 'DEACTIVATED' then the porperty will not appear in the list
    */
    @Query(nativeQuery = true,
            value = "SELECT * FROM landlord.property p " +
                    "WHERE (SELECT CASE WHEN EXISTS(" +
                    "SELECT * FROM landlord.propertyportal pp " +
                    "WHERE p.id = pp.property_id " +
                    "AND pp.state != 'DEACTIVATED') " +
                    "THEN false ELSE true END AS your_result) " +
                    "AND p.customer_id = ?#{principal.customer.id} " +
                    "GROUP BY p.id " +
                    "ORDER BY ?#{#pageable}",
            countQuery = "SELECT DISTINCT count(*) FROM landlord.property p " +
                    "WHERE (SELECT CASE WHEN EXISTS(" +
                    "SELECT * FROM landlord.propertyportal pp " +
                    "WHERE p.id = pp.property_id " +
                    "AND pp.state != 'DEACTIVATED') " +
                    "THEN false ELSE true END AS your_result) " +
                    "AND p.customer_id = ?#{principal.customer.id}")
    Page<Property> findAllOffline(
            Pageable pageable
    );

    @Query(nativeQuery = true,
            value = "SELECT * FROM landlord.property p " +
                    "WHERE (SELECT CASE WHEN EXISTS(" +
                    "SELECT * FROM landlord.propertyportal pp " +
                    "WHERE p.id = pp.property_id " +
                    "AND pp.state != 'DEACTIVATED') " +
                    "THEN false ELSE true END AS your_result) " +
                    "AND p.customer_id = :#{principal.customer.id} " +
                    "AND p.user_id IN :userIDs " +
                    "GROUP BY p.id " +
                    "ORDER BY :#{#pageable}",
            countQuery = "SELECT DISTINCT count(*) FROM landlord.property p " +
                    "WHERE (SELECT CASE WHEN EXISTS(" +
                    "SELECT * FROM landlord.propertyportal pp " +
                    "WHERE p.id = pp.property_id " +
                    "AND pp.state != 'DEACTIVATED') " +
                    "THEN false ELSE true END AS your_result) " +
                    "AND p.customer_id = :#{principal.customer.id} " +
                    "AND p.user_id IN :userIDs")
    Page<Property> findAllOfflineByUsers(
            @Param("userIDs") Long[] userIDs,
            Pageable pageable
    );

    @Query(nativeQuery = true,
            value = "SELECT * FROM landlord.property p " +
                    "WHERE (NOT (SELECT CASE WHEN EXISTS(" +
                    "SELECT * FROM landlord.propertyportal pp " +
                    "WHERE p.id = pp.property_id " +
                    "AND pp.state != 'DEACTIVATED') " +
                    "THEN false ELSE true END AS your_result)) " +
                    "AND p.customer_id = ?#{principal.customer.id} " +
                    "GROUP BY p.id " +
                    "ORDER BY ?#{#pageable}",
            countQuery = "SELECT DISTINCT count(*) FROM landlord.property p " +
                    "WHERE (NOT (SELECT CASE WHEN EXISTS(" +
                    "SELECT * FROM landlord.propertyportal pp " +
                    "WHERE p.id = pp.property_id " +
                    "AND pp.state != 'DEACTIVATED') " +
                    "THEN false ELSE true END AS your_result)) " +
                    "AND p.customer_id = ?#{principal.customer.id}")
    Page<Property> findAllOnline(
            Pageable pageable
    );

    @Query(nativeQuery = true,
            value = "SELECT * FROM landlord.property p " +
                    "WHERE (SELECT CASE WHEN EXISTS(" +
                    "SELECT * FROM landlord.propertyportal pp " +
                    "WHERE p.id = pp.property_id " +
                    "AND pp.state = cast(:state as landlord.propertystate) " +
                    "AND pp.state != 'DEACTIVATED') " +
                    "THEN true ELSE false END AS your_result) " +
                    "AND p.customer_id = :#{principal.customer.id} " +
                    "GROUP BY p.id " +
                    "ORDER BY :#{#pageable}",
            countQuery = "SELECT * FROM landlord.property p " +
                    "WHERE (SELECT CASE WHEN EXISTS(" +
                    "SELECT * FROM landlord.propertyportal pp " +
                    "WHERE p.id = pp.property_id " +
                    "AND pp.state = cast(:state as landlord.propertystate) " +
                    "AND pp.state != 'DEACTIVATED') " +
                    "THEN true ELSE false END AS your_result) " +
                    "AND p.customer_id = :#{principal.customer.id}")
    Page<Property> findAllOnlineByState(@Param("state") String state, Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT * FROM landlord.property p " +
                    "WHERE (NOT (SELECT CASE WHEN EXISTS(" +
                    "SELECT * FROM landlord.propertyportal pp " +
                    "WHERE p.id = pp.property_id " +
                    "AND pp.state != 'DEACTIVATED') " +
                    "THEN false ELSE true END AS your_result)) " +
                    "AND p.customer_id = :#{principal.customer.id} " +
                    "AND p.user_id IN :userIDs " +
                    "GROUP BY p.id " +
                    "ORDER BY :#{#pageable}",
            countQuery = "SELECT DISTINCT count(*) FROM landlord.property p " +
                    "WHERE (NOT (SELECT CASE WHEN EXISTS(" +
                    "SELECT * FROM landlord.propertyportal pp " +
                    "WHERE p.id = pp.property_id " +
                    "AND pp.state != 'DEACTIVATED') " +
                    "THEN false ELSE true END AS your_result)) " +
                    "AND p.customer_id = :#{principal.customer.id} " +
                    "AND p.user_id IN :userIDs")
    Page<Property> findAllOnlineByUsers(@Param("userIDs") Long[] userIDs, Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT * FROM landlord.property p " +
                    "WHERE (SELECT CASE WHEN EXISTS(" +
                    "SELECT * FROM landlord.propertyportal pp " +
                    "WHERE p.id = pp.property_id " +
                    "AND pp.state = cast(:state as landlord.propertystate) " +
                    "AND pp.state != 'DEACTIVATED') " +
                    "THEN true ELSE false END AS your_result) " +
                    "AND p.customer_id = :#{principal.customer.id} " +
                    "AND p.user_id IN :userIDs " +
                    "GROUP BY p.id " +
                    "ORDER BY :#{#pageable}",
            countQuery = "SELECT * FROM landlord.property p " +
                    "WHERE (SELECT CASE WHEN EXISTS(" +
                    "SELECT * FROM landlord.propertyportal pp " +
                    "WHERE p.id = pp.property_id " +
                    "AND pp.state = cast(:state as landlord.propertystate) " +
                    "AND pp.state != 'DEACTIVATED') " +
                    "THEN true ELSE false END AS your_result) " +
                    "AND p.customer_id = :#{principal.customer.id} " +
                    "AND p.user_id IN :userIDs")
    Page<Property> findAllOnlineByUsersAndState(@Param("userIDs") Long[] userIDs,
                                                @Param("state") String state,
                                                Pageable pageable);

    @Override
    @Query("SELECT o FROM #{#entityName} o " +
            "INNER JOIN o.portals p " +
            "WHERE o.user IN (:users) " +
            "AND p.state = :state " +
            "AND o.customer = ?#{principal.customer}")
    Page<Property> findByUsersAndState(
            @Param("users") LandlordUser[] users,
            @Param("state") PropertyPortalState state,
            Pageable pageable
    );


    @RestResource(exported = false)
    @Query(value = "select * from landlord.property p where" +
            "  (customer_id || ' ' ||" +
            "        COALESCE(data -> 'address' ->> 'street', '') ||  ' ' ||" +
            "        COALESCE(external_id, '') ||  ' ' ||" +
            "       COALESCE(data -> 'address' ->> 'houseNumber', '') ||  ' ' ||" +
            "       COALESCE(data -> 'address' ->> 'zipCode', '') ||  ' ' ||" +
            "       COALESCE(data -> 'address' ->> 'region', '') ||  ' ' ||" +
            "       COALESCE(data -> 'address' ->> 'city', '') ||  ' ' ||" +
            "       COALESCE(data -> 'address' ->> 'country', '') ||  ' ' ||" +
            "       COALESCE(data ->> 'name','')) ilike  :#{principal.customer.id} || '%' || :search || '%'", nativeQuery = true)
    List<Property> findAllContaining(@Param("search") String search);

    @Override
    @PreAuthorize("#property.customer == principal?.customer")
    Long countByCustomer(@Param("customer") LandlordCustomer customer);

    @RestResource(exported = false)
    List<Property> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT new de.immomio.data.landlord.bean.property.EntityCountBean(pr.prioset.id, COUNT(pr)) " +
            "FROM Property as pr " +
            "WHERE pr.prioset in :priosets GROUP BY pr.prioset.id")
    @RestResource(exported = false)
    List<EntityCountBean> getSizeOfProperties(@Param("priosets") List<Prioset> priosets);

    @Modifying
    @Transactional
    @Query(value = "UPDATE landlord.property set user_id = :user_id_new  WHERE user_id = :user_id_old", nativeQuery = true)
    @RestResource(exported = false)
    void customSetUserId(@Param("user_id_old") Long oldUserId, @Param("user_id_new") Long newUserId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE landlord.property set user_id = :user_id_new WHERE user_id IN :user_ids_old_list", nativeQuery = true)
    @RestResource(exported = false)
    void updateOwner(@Param("user_ids_old_list") List<LandlordUser> listOfIds, @Param("user_id_new") Long newUserId);

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} p set p.showSelfDisclosureQuestions=true " +
            "WHERE p.customer.id = :customerId AND p.customer = ?#{principal.customer}")
    @RestResource(exported = false)
    void setShowSelfDisclosureQuestionsFlag(@Param("customerId") Long customerId);

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} p set p.showSelfDisclosureQuestions=false " +
            "WHERE p.customer.id = :customerId AND p.customer = ?#{principal.customer}")
    @RestResource(exported = false)
    void unsetShowSelfDisclosureQuestionsFlag(@Param("customerId") Long customerId);

    @Transactional
    @Modifying
    @RestResource(exported = false)
    @Query("DELETE FROM #{#entityName} p where p.customer = ?#{principal.customer}")
    void deleteFromPrincipal();

    @RestResource(exported = false)
    @Query("SELECT o FROM #{#entityName} o where o.id = :id and o.customer = :customer")
    Optional<Property> findByIdAndCustomer(@Param("id") Long id, @Param("customer") LandlordCustomer customer);

    @RestResource(exported = false)
    @Query("SELECT o.id FROM #{#entityName} o where o.customer = :customer")
    List<Long> findAllIdsByCustomer(@Param("customer") LandlordCustomer customer);

    @RestResource(exported = false)
    @Query("SELECT o.id FROM #{#entityName} o where o.prioset = :prioset")
    List<Long> findAllIdsByPrioset(@Param("prioset") Prioset prioset);

    @RestResource(exported = false)
    @Query("SELECT o.id FROM #{#entityName} o where o.prioset in :priosets")
    List<Long> findAllIdsByPriosets(@Param("priosets") List<Prioset> priosets);
}
