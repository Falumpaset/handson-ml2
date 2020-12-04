package de.immomio.controller.reporting;

import de.immomio.controller.BaseController;
import de.immomio.landlord.service.reporting.LandlordReportValidationService;
import de.immomio.landlord.service.reporting.aggregation.ReportDataService;
import de.immomio.reporting.model.filter.ReportingFilterBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Niklas Lindemann
 */
@Slf4j
@RepositoryRestController
@RequestMapping(value = "/reports")
public class ReportingExportController extends BaseController {

    private static final String CSV_CREATION_ERROR = "CSV_CREATION_ERROR_L";
    private static final String CSV_FILE_NAME = "_report.csv";
    private static final String CSV_CONTENT_TYPE = "text/csv";
    private static final String ZIP_CREATION_ERROR = "ZIP_CREATION_ERROR_L";
    private static final String ZIP_FILE_NAME = "_allReports.zip";
    private static final String ZIP_CONTENT_TYPE = "application/zip";
    private static final String REQUEST_PATH_EXPORT = "/export/";

    private final LandlordReportValidationService reportValidationService;
    private final ReportDataService reportDataService;

    private final DateTimeFormatter filenameDateformatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Autowired
    public ReportingExportController(
            LandlordReportValidationService reportValidationService,
            ReportDataService reportDataService,
            ApplicationContext appContext
    ) {
        this.reportValidationService = reportValidationService;
        this.reportDataService = reportDataService;
    }

    @PostMapping("/export/**")
    public ResponseEntity<Object> exportAsCsv(
            @RequestBody ReportingFilterBean filterBean,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        reportValidationService.validateReportingBean(filterBean);
        String endpoint = request.getRequestURI().split(request.getContextPath() + REQUEST_PATH_EXPORT)[1];

        return createCsvFileResponseForReportingChartData(filterBean, endpoint, response);
    }

    @PostMapping("/exportAll")
    public ResponseEntity<Object> exportAll(
            @RequestBody ReportingFilterBean filterBean,
            HttpServletResponse response
    ) {
        reportValidationService.validateReportingBean(filterBean);

        return createZipFileResponseForReportingChartData(filterBean, response);
    }

    private ResponseEntity<Object> createCsvFileResponseForReportingChartData(
            ReportingFilterBean filterBean,
            String endpoint,
            HttpServletResponse response
    ) {
        try {
            byte[] csvContent = reportDataService.getReportCsvContent(filterBean, endpoint);
            String filename = LocalDate.now().format(filenameDateformatter) + CSV_FILE_NAME;

            return createFileToResponse(
                    csvContent,
                    response,
                    filename,
                    CSV_CREATION_ERROR,
                    CSV_CONTENT_TYPE,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return new ResponseEntity<>(CSV_CREATION_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Object> createZipFileResponseForReportingChartData(
            ReportingFilterBean filterBean,
            HttpServletResponse response
    ) {
        try {
            byte[] zipContent = reportDataService.getAllReportsZipContent(filterBean);
            String filename = LocalDate.now().format(filenameDateformatter) + ZIP_FILE_NAME;

            return createFileToResponse(
                    zipContent,
                    response,
                    filename,
                    ZIP_CREATION_ERROR,
                    ZIP_CONTENT_TYPE,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return new ResponseEntity<>(ZIP_CREATION_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
