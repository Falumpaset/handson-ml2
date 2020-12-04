package de.immomio.model.repository.landlord.customer;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.model.abstractrepository.customer.AbstractCustomerRepository;
import de.immomio.model.repository.core.landlord.customer.BaseLandlordCustomerRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Bastian Bliemeister
 */
public interface LandlordCustomerRepository
        extends AbstractCustomerRepository<LandlordCustomer>, BaseLandlordCustomerRepository {

    @Override
    @Query("SELECT c FROM LandlordCustomer c LEFT JOIN c.ftpAccesses f WHERE f.id = :id"
            + " AND (c.id = ?#{principal.customer.id})")
    LandlordCustomer findByCustomerByFtpAccess(@Param("id") long id);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("customer") LandlordCustomer customer);

}
