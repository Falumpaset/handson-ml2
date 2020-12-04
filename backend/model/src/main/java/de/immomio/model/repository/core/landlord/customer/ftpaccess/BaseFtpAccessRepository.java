package de.immomio.model.repository.core.landlord.customer.ftpaccess;

import de.immomio.data.landlord.entity.ftpaccess.FtpAccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister.
 */

public interface BaseFtpAccessRepository extends JpaRepository<FtpAccess, Long> {

    @Override
    Optional<FtpAccess> findById(@Param("id") Long id);

    @Override
    Page<FtpAccess> findAll(Pageable pageable);

    @Override
    void deleteById(@Param("id") Long id);

    @Override
    void delete(@Param("ftpAccess") FtpAccess ftpAccess);

    @Override <R extends FtpAccess> R save(@Param("ftpAccess") R ftpAccess);
}
