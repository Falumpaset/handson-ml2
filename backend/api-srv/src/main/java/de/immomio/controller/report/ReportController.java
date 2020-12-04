package de.immomio.controller.report;

import de.immomio.model.entity.admin.report.Report;
import de.immomio.service.report.CustomerOverviewReportService;
import de.immomio.service.report.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/reports")
public class ReportController {

    @Value("${timezone.europe}")
    private String TIMEZONE;

    private static final String CONTENT_DISPOSITION = "Content-Disposition";

    private static final String ATTACHMENT_FILENAME = "attachment; filename=";

    private static final String FILENAME_PATTERN = "report_%s_%s.csv";

    private final ReportService reportService;

    private final CustomerOverviewReportService customerOverviewReportService;

    @Autowired
    public ReportController(
            ReportService reportService,
            CustomerOverviewReportService customerOverviewReportService
    ) {
        this.reportService = reportService;
        this.customerOverviewReportService = customerOverviewReportService;
    }

    @GetMapping(value = "/download/{id}", produces = "text/csv")
    public ResponseEntity downloadReport(@PathVariable("id") Long id, HttpServletResponse response) {
        try {
            Optional<Report> reportOpt = reportService.findById(id);

            if (reportOpt.isPresent()) {
                Report report = reportOpt.get();
                String filename = getFilename(report);

                response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + filename);
                response.getWriter().write(report.getReport());

                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/create/customers")
    public ResponseEntity generateCustomerReport(HttpServletResponse response) throws IOException {
        String customerReport = customerOverviewReportService.getCustomerReport();
        response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + "report.csv");
        response.getWriter().write(customerReport);
        return ResponseEntity.ok().build();
    }

    private String getFilename(Report report) {
        LocalDate date = report.getStartdate().toInstant().atZone(ZoneId.of(TIMEZONE)).toLocalDate();
        String formattedDate = date.format(DateTimeFormatter.BASIC_ISO_DATE);
        String type = report.getType().toString().toLowerCase();
        return String.format(FILENAME_PATTERN, type, formattedDate);
    }
}
