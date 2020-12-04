package de.immomio.model.repository.service.admin.report;

import de.immomio.model.entity.admin.report.Report;
import de.immomio.model.entity.admin.report.ReportType;
import de.immomio.model.repository.core.admin.report.BaseReportRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author Niklas Lindemann
 */

@RepositoryRestResource(path = "adm-reports")
public interface ReportRepository extends BaseReportRepository {

    List<Report> findAllByType(@Param("type") ReportType type);
}
