package de.immomio.model.repository.landlord.customer.user;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.model.abstractrepository.customer.user.AbstractUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

/**
 * @author Bastian Bliemeister
 */
public interface LandlordUserRepository extends AbstractUserRepository<LandlordUser> {

    @RestResource(exported = false)
    Page<LandlordUser> findByCustomerAndUsertype(@Param("customer") LandlordCustomer customer,
                                                 @Param("type") LandlordUsertype type, Pageable pageable);

    @RestResource(exported = false)
    Page<LandlordUser> findByUsertype(@Param("usertype") LandlordUsertype usertype, Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("user") LandlordUser user);

    @RestResource(exported = false)
    Optional<LandlordUser> findByIdAndCustomerAndUsertype(Long id, LandlordCustomer customer, LandlordUsertype landlordUsertype);
}
