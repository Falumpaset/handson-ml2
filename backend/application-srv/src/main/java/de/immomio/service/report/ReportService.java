package de.immomio.service.report;

import de.immomio.model.entity.admin.report.Report;
import de.immomio.model.repository.service.admin.report.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Niklas Lindemann
 */

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Optional<Report> findById(Long id) {
        return reportRepository.findById(id);
    }
}
