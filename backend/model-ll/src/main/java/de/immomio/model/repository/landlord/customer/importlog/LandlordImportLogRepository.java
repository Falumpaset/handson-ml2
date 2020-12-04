package de.immomio.model.repository.landlord.customer.importlog;

import de.immomio.data.landlord.entity.importlog.ImportLog;
import de.immomio.model.repository.core.landlord.customer.importlog.BaseImportLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister, Maik Kingma
 */
public interface LandlordImportLogRepository extends BaseImportLogRepository, LandlordImportLogRepositoryCustom {

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = :id AND (o.customer.id = ?#{principal.customer.id})")
    Optional<ImportLog> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.customer.id = ?#{principal.customer.id}")
    Page<ImportLog> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("#importLog.customer?.id == principal?.customer?.id")
    void delete(@Param("ftpAccess") ImportLog importLog);

    @Override
    @PreAuthorize("#importLog.customer?.id == principal?.customer?.id")
    ImportLog save(@Param("ftpAccess") ImportLog importLog);
}
