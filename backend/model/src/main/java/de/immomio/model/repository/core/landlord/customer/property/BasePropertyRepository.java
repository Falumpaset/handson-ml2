package de.immomio.model.repository.core.landlord.customer.property;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.portal.PropertyPortalState;
import de.immomio.data.landlord.entity.user.LandlordUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Bastian Bliemeister, Maik Kingma
 */
@RepositoryRestResource(path = "properties")
public interface BasePropertyRepository extends JpaRepository<Property, Long> {

    @Override
    <S extends Property> S save(S entity);

    @Override
    void deleteById(@Param("id") Long id);

    @Override
    void delete(@Param("property") Property property);

    Page<Property> findByCustomer(@Param("customer") LandlordCustomer customer, Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM landlord.property p " +
                    "WHERE p.customer_id = :customerId " +
                    "AND LOWER (data->>'name') like LOWER('%' || :name || '%') " +
                    "ORDER BY ?#{#pageable}",
            countQuery = "SELECT * FROM landlord.property p " +
                    "WHERE p.customer_id = :customerId " +
                    "AND LOWER (data->>'name') like LOWER('%' || :name || '%')")
    Page<Property> findByCustomerAndNameIgnoreCaseLike(@Param("customerId") Long customerId,
                                                       @Param("name") String name, Pageable pageable);

    @Query("SELECT o FROM #{#entityName} o INNER JOIN o.portals p WHERE o.customer = :customer AND p.state = :state")
    Page<Property> findByCustomerAndState(@Param("customer") LandlordCustomer customer,
                                          @Param("state") PropertyPortalState state, Pageable pageable);

    @Query("SELECT o FROM #{#entityName} o INNER JOIN o.portals p WHERE o.user IN (:users) AND p.state = :state")
    Page<Property> findByUsersAndState(@Param("users") LandlordUser[] users, @Param("state") PropertyPortalState state,
                                       Pageable pageable);

    @Query("SELECT o FROM #{#entityName} o WHERE o.user IN (:users)")
    Page<Property> findByUsers(@Param("users") LandlordUser[] users, Pageable pageable);


    @Query( "SELECT p FROM Property p " +
            "WHERE p.customer.id = :customerId " +
            "AND lower(p.externalId) = :externalId ")
    List<Property> findByCustomerIdAndExternalId(
            @Param("customerId") Long customerId,
            @Param("externalId") String externalId
    );

    @Query("SELECT p FROM Property p " +
            "WHERE p.externalId = :externalId")
    @RestResource(exported = false)
    List<Property> findByExternalId(
            @Param("externalId") String externalId
    );

    Long countByCustomer(@Param("customer") LandlordCustomer customer);

    List<Property> findByCustomerId(@Param("customerId") Long customerId);

    @RestResource(exported = false)
    @Query("SELECT o FROM #{#entityName} o INNER JOIN o.portals p WHERE p.state = :state AND validUntil <= :validUntil")
    List<Property> findByStateAndValidUntilBefore(@Param("state") PropertyPortalState state,
                                                  @Param("validUntil") Date validUntil);

    @Query("SELECT o FROM #{#entityName} o INNER JOIN o.portals p WHERE p.state = :state")
    Page<Property> findAllByState(@Param("state") PropertyPortalState state, Pageable pageable);

    @RestResource(exported = false)
    @Query("SELECT o FROM #{#entityName} o WHERE o.user IN (:users)")
    List<Property> findByUsersReturnsList(LandlordUser[] users);

    @RestResource(exported = false)
    @Query(value = "SELECT DISTINCT ON " +
                        "(p.id) p.*, " +
                        "ST_Distance(p.location, ST_SetSRID(ST_MakePoint" +
                        "(:longitude, :latitude), 4326)) " +
                    "FROM landlord.property p " +
                    "WHERE ST_DWithin(p.location, cast(ST_SetSRID" +
                        "(ST_MakePoint(:longitude, :latitude), 4326) as geography), :radius) " +
                    "ORDER BY p.id, ST_Distance(p.location, " +
                        "ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326));", nativeQuery = true)
    List<Property> customFindNearestToPoint(@Param("latitude") Double latitude, @Param("longitude") Double longitude, @Param("radius") Long radius);

    @Query(value = " select * from landlord.property p where p.data -> 'address' ->> 'zipCode' in (:zipCodes)", nativeQuery = true)
    List<Property> findAllByZipCodeIn(@Param("zipCodes") List<String> zipCodes);

    @Modifying
    @Transactional
    @RestResource(exported = false)
    @Query(nativeQuery = true, value = "UPDATE landlord.property SET location = ST_SetSRID" +
            "(ST_PointFromText('POINT(' || CAST(COALESCE(NULLIF(regexp_replace(" +
            "data #>> '{address,coordinates,longitude}', '[^-0-9.]+', '', 'g'),''),'0.0') " +
            "AS DOUBLE PRECISION ) || ' ' || CAST(COALESCE(NULLIF(regexp_replace(" +
            "data #>> '{address,coordinates,latitude}', '[^-0-9.]+', '', 'g'),''),'0.0') " +
            "AS DOUBLE PRECISION) || ')', 4326),4326) WHERE id = :id")
    void customUpdatePropertyLocation(@Param("id") Long id);

    @RestResource(exported = false)
    List<Property> findAllByIdIn(@Param("ids") List<Long> ids);

    @Query(nativeQuery = true, value = "select *" +
            " from landlord.property p, json_array_elements(CAST(p.data AS json)#>'{attachments}') attachments" +
            " where p.customer_id = :customerId" +
            " and attachments ->> 'identifier' = :identifier")
    @RestResource(exported = false)
    List<Property> findAllUsingAttachment(@Param("customerId") Long customerId, @Param("identifier") String identifier);

    @RestResource(exported = false)
    @Query("SELECT p from Property p where p.customer = :customer and p.tenant is not null and p.tenant.created <= :archiveTimestamp")
    List<Property> findForApplicationArchiving(@Param("customer") LandlordCustomer customer, @Param("archiveTimestamp") Date archiveTimestamp);
}
