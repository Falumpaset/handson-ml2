package de.immomio.model.repository.core.admin.report;

import de.immomio.model.entity.admin.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "adm-reports")
public interface BaseReportRepository extends JpaRepository<Report, Long> {

}
