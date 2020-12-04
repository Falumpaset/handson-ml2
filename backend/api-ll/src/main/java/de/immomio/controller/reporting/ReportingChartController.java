package de.immomio.controller.reporting;

import de.immomio.landlord.service.reporting.LandlordReportValidationService;
import de.immomio.landlord.service.reporting.aggregation.LandlordReportAggregationService;
import de.immomio.reporting.model.filter.ReportingFilterBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Niklas Lindemann
 */
@Slf4j
@RepositoryRestController
@RequestMapping(value = "/reports/chart")
public class ReportingChartController {

    private LandlordReportAggregationService reportAggregationService;

    private LandlordReportValidationService reportValidationService;

    @Autowired
    public ReportingChartController(
            LandlordReportAggregationService reportAggregationService,
            LandlordReportValidationService reportValidationService
    ) {
        this.reportAggregationService = reportAggregationService;
        this.reportValidationService = reportValidationService;
    }

    @PostMapping("/property/created")
    public ResponseEntity getPropertyCreatedData(@RequestBody ReportingFilterBean filterBean) {
        reportValidationService.validateReportingBean(filterBean);
        return ResponseEntity.ok(reportAggregationService.getPropertyCreatedSeries(filterBean));
    }

    @PostMapping("/property/published")
    public ResponseEntity getPropertyPublishedData(@RequestBody ReportingFilterBean filterBean) {
        reportValidationService.validateReportingBean(filterBean);
        return ResponseEntity.ok(reportAggregationService.getPropertyPublishedSeries(filterBean));
    }

    @PostMapping("/application/total")
    public ResponseEntity getTotalApplicationsData(@RequestBody ReportingFilterBean filterBean) {
        reportValidationService.validateReportingBean(filterBean);
        return ResponseEntity.ok(reportAggregationService.getApplicationsCountSeries(filterBean));
    }

    @PostMapping("/application/byPortal")
    public ResponseEntity getApplicationsByPortalData(@RequestBody ReportingFilterBean filterBean) {
        reportValidationService.validateReportingBean(filterBean);
        return ResponseEntity.ok(reportAggregationService.getApplicationsByPortalSeries(filterBean));
    }

    @PostMapping("/application/intentions")
    public ResponseEntity getApplicationIntentionsData(@RequestBody ReportingFilterBean filterBean) {
        reportValidationService.validateReportingBean(filterBean);
        return ResponseEntity.ok(reportAggregationService.getApplicationIntentionSeries(filterBean));
    }

    @PostMapping("/application/appointmentAcceptances")
    public ResponseEntity getApplicationAppointmentAcceptancesData(@RequestBody ReportingFilterBean filterBean) {
        reportValidationService.validateReportingBean(filterBean);
        return ResponseEntity.ok(reportAggregationService.getApplicationAppointmentAcceptanceSeries(filterBean));
    }

    @PostMapping("/proposal/offered")
    public ResponseEntity getProposalsOfferedData(@RequestBody ReportingFilterBean filterBean) {
        reportValidationService.validateReportingBean(filterBean);
        return ResponseEntity.ok(reportAggregationService.getProposalOfferedSeries(filterBean));
    }

    @PostMapping("/appointment/occurences")
    public ResponseEntity getViewingOccurences(@RequestBody ReportingFilterBean filterBean) {
        reportValidationService.validateReportingBean(filterBean);
        return ResponseEntity.ok(reportAggregationService.getAppointmentOccurencesSeries(filterBean));
    }

    @PostMapping("/appointment/slots")
    public ResponseEntity getViewingOSlots(@RequestBody ReportingFilterBean filterBean) {
        reportValidationService.validateReportingBean(filterBean);
        return ResponseEntity.ok(reportAggregationService.getAppointmentSlotsSeries(filterBean));
    }

    @PostMapping("/application/distribution/householdSize")
    public ResponseEntity getDistributionHouseHoldSize(@RequestBody ReportingFilterBean filterBean) {
        reportValidationService.validateReportingBean(filterBean);
        return ResponseEntity.ok(reportAggregationService.getDistributionHouseHoldSizeData(filterBean));
    }

    @PostMapping("/application/distribution/householdType")
    public ResponseEntity getDistributionHouseHoldType(@RequestBody ReportingFilterBean filterBean) {
        reportValidationService.validateReportingBean(filterBean);
        return ResponseEntity.ok(reportAggregationService.getDistributionHouseHoldTypeData(filterBean));
    }

    @PostMapping("/application/distribution/employmentType")
    public ResponseEntity getDistributionEmploymentType(@RequestBody ReportingFilterBean filterBean) {
        reportValidationService.validateReportingBean(filterBean);
        return ResponseEntity.ok(reportAggregationService.getDistributionEmploymentTypeData(filterBean));
    }

    @PostMapping("/application/distribution/wbs")
    public ResponseEntity getDistributionWbs(@RequestBody ReportingFilterBean filterBean) {
        reportValidationService.validateReportingBean(filterBean);
        return ResponseEntity.ok(reportAggregationService.getDistributionWbsData(filterBean));
    }

    @PostMapping("/application/distribution/animals")
    public ResponseEntity getDistributionAnimals(@RequestBody ReportingFilterBean filterBean) {
        reportValidationService.validateReportingBean(filterBean);
        return ResponseEntity.ok(reportAggregationService.getDistributionAnimalsData(filterBean));
    }

    @PostMapping("/application/distribution/city")
    public ResponseEntity getDistributionCity(@RequestBody ReportingFilterBean filterBean) {
        reportValidationService.validateReportingBean(filterBean);
        return ResponseEntity.ok(reportAggregationService.getDistributionCityData(filterBean));
    }

    @PostMapping("/application/distribution/zip")
    public ResponseEntity getDistributionZip(@RequestBody ReportingFilterBean filterBean) {
        reportValidationService.validateReportingBean(filterBean);
        return ResponseEntity.ok(reportAggregationService.getDistributionZipData(filterBean));
    }

    @PostMapping("/application/distribution/income")
    public ResponseEntity getDistributionIncome(@RequestBody ReportingFilterBean filterBean) {
        reportValidationService.validateReportingBean(filterBean);
        return ResponseEntity.ok(reportAggregationService.getDistributionIncomeData(filterBean));
    }
}
