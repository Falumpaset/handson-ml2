package de.immomio.model.repository.landlord.customer.ftpaccess;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.ftpaccess.FtpAccess;
import de.immomio.model.repository.core.landlord.customer.ftpaccess.BaseFtpAccessRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister.
 */

@RepositoryRestResource(path = "ftpAccesses")
public interface LandlordFtpAccessRepository extends BaseFtpAccessRepository {

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = :id AND "
            + "o.customer.id = ?#{principal.customer.id}")
    Optional<FtpAccess> findById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    @Query("SELECT o FROM #{#entityName} o WHERE o.customer.id = ?#{principal.customer.id}")
    Page<FtpAccess> findAll(Pageable pageable);

    @PreAuthorize("#customer.id == principal?.customer?.id")
    Page<FtpAccess> findByCustomer(@Param("customer") LandlordCustomer customer, Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("#ftpAccess.customer?.id == principal?.customer?.id")
    void delete(@Param("ftpAccess") FtpAccess ftpAccess);

    @Override
    @PreAuthorize("#ftpAccess.customer?.id == principal?.customer?.id")
    FtpAccess save(@Param("ftpAccess") FtpAccess ftpAccess);
}
