package de.immomio.model.repository.core.propertysearcher.user;

import de.immomio.data.base.type.user.PropertySearcherUserType;
import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;
import java.util.List;

public interface BasePropertySearcherUserProfileRepository extends JpaRepository<PropertySearcherUserProfile, Long> {

    @Query("SELECT up from PropertySearcherUserProfile up where up.user.type = 'REGISTERED' and up.type = 'MAIN' and up.user.lastLogin is not null")
    Page<PropertySearcherUserProfile> findRegisteredByTypeAndLastLoginNotNull(Pageable pageable);

    @Query("SELECT o from PropertySearcherUserProfile o where o.user.type = 'REGISTERED' and o.type = 'MAIN' and o.user.lastLogin is not null and o.created > :after")
    Page<PropertySearcherUserProfile> findRegisteredByTypeAndLastLoginNotNullAndCreatedAfter(Date after, Pageable pageable);

    @Query("SELECT o from PropertySearcherUserProfile o where o.searchUntil between :from and :to and o.searchProfiles IS NOT EMPTY and o.tenantPoolCustomer IS NULL")
    List<PropertySearcherUserProfile> findAllBySearchUntilBetween(@Param("from") Date from, @Param("to") Date to);

    @Query("SELECT o FROM PropertySearcherUserProfile o where o.tenantPoolCustomer IS NOT NULL AND o.searchProfiles IS NOT EMPTY and o.searchUntil between :from and :to ")
    List<PropertySearcherUserProfile> findAllInInternalPool(@Param("from") Date from, @Param("to") Date to);

    List<PropertySearcherUserProfile> findAllBySearchUntilBefore(Date date);

    List<PropertySearcherUserProfile> findByTypeAndCreatedBetween(PropertySearcherUserProfileType type, Date from, Date to);

    @Query("SELECT COUNT(up) from PropertySearcherUserProfile up WHERE up.user.type = 'REGISTERED' AND up.type = 'MAIN' AND up.created > :date AND up.user.lastLogin IS NOT NULL")
    Long countRegisteredByCreatedAfterAndLastLoginNotNull(@Param("date") Date after);

    @Query("SELECT count(up) from PropertySearcherUserProfile up where up.user.type = 'REGISTERED' and up.type = 'MAIN' and up.user.lastLogin is not null")
    Long countRegisteredByLastLoginNotNull(PropertySearcherUserType type);

    @RestResource(exported = false)
    List<PropertySearcherUserProfile> findAllByCreatedBetween(Date dateFrom, Date dateTo);

    @RestResource(exported = false)
    PropertySearcherUserProfile findFirstByUserAndTypeOrderByCreatedDesc(PropertySearcherUser user, PropertySearcherUserProfileType profileType);
}
