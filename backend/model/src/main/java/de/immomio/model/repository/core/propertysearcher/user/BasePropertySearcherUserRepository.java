package de.immomio.model.repository.core.propertysearcher.user;

import de.immomio.data.base.type.user.PropertySearcherUserType;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.model.abstractrepository.customer.user.BaseAbstractUserRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface BasePropertySearcherUserRepository extends BaseAbstractUserRepository<PropertySearcherUser> {

    Set<PropertySearcherUser> findByCustomer(@Param("id") Long id);

    @Query("SELECT o FROM #{#entityName} o WHERE o.enabled = true AND o.emailVerified IS NULL "
            + "AND o.created BETWEEN :startDate AND :endDate AND o.type = 'REGISTERED' AND o.lastLogin is not null")
    List<PropertySearcherUser> findAllNotEmailVerified(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    Long countByCreatedAfter(Date date);

    @Query("SELECT COUNT(o) from #{#entityName} o WHERE o.type = 'UNREGISTERED' AND o.lastLogin IS NULL")
    Long countUnregistered();

    @Query("SELECT COUNT(o) from #{#entityName} o WHERE o.type = 'UNREGISTERED' AND o.lastLogin IS NULL AND o.created >= :date ")
    Long countUnregisteredCreatedAfter(@Param("date") Date date);

    boolean existsByEmail(String email);

    List<PropertySearcherUser> findByTypeAndCreatedBetween(PropertySearcherUserType type, Date from, Date to);
}

