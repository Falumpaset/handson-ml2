package de.immomio.model.repository.core.landlord.customer.importlog;

import de.immomio.data.landlord.entity.importlog.ImportLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister, Maik Kingma
 */

@RepositoryRestResource(path = "importLogs")
public interface BaseImportLogRepository extends JpaRepository<ImportLog, Long> {

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = :id")
    Optional<ImportLog> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o")
    Page<ImportLog> findAll(Pageable pageable);

    @Override
    void deleteById(@Param("id") Long id);

    @Override
    void delete(@Param("importLog") ImportLog importLog);

    @SuppressWarnings("unchecked")
    @Override
    ImportLog save(@Param("importLog") ImportLog importLog);
}
