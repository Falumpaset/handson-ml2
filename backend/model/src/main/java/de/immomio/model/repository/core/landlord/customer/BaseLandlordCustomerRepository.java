package de.immomio.model.repository.core.landlord.customer;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.model.abstractrepository.customer.BaseAbstractCustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Bastian Bliemeister
 */
public interface BaseLandlordCustomerRepository extends BaseAbstractCustomerRepository<LandlordCustomer> {

    @Query("SELECT c FROM LandlordCustomer c LEFT JOIN c.ftpAccesses f WHERE f.id = :id")
    LandlordCustomer findByCustomerByFtpAccess(@Param("id") long id);

    Page<LandlordCustomer> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    @Query("SELECT c From LandlordCustomer c where c.customerSettings is not null and c.customerSettings.applicationArchiveActive = true")
    List<LandlordCustomer> findArchiveApplicationsActive();

}
