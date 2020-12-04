package de.immomio.model.repository.core.landlord.customer.user;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.model.abstractrepository.customer.user.BaseAbstractUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Bastian Bliemeister
 */

public interface BaseLandlordUserRepository extends BaseAbstractUserRepository<LandlordUser> {

    Page<LandlordUser> findByCustomerAndUsertype(@Param("customer") LandlordCustomer customer,
                                                 @Param("type") LandlordUsertype type, Pageable pageable);

    Page<LandlordUser> findByUsertype(@Param("usertype") LandlordUsertype usertype, Pageable pageable);

    @Query("SELECT o FROM #{#entityName} o WHERE o.enabled = true AND o.emailVerified IS NULL " +
            "AND o.created BETWEEN :startDate AND :endDate")
    List<LandlordUser> findAllNotEmailVerified(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    LandlordUser findByEmailIgnoreCase(@Param("email") String email);

    @RestResource(exported = false)
    Optional<LandlordUser> findFirstByCustomerAndUsertype(
            @Param("customer") LandlordCustomer customer,
            @Param("type") LandlordUsertype type);

}
