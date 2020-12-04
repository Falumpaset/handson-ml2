package de.immomio.controller.reporting;

import de.immomio.controller.BaseController;
import de.immomio.landlord.service.reporting.aggregation.ReportFilterAggregationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Niklas Lindemann
 */
@Slf4j
@RepositoryRestController
@RequestMapping(value = "/reports/filter")
public class ReportingFilterController extends BaseController {
    private ReportFilterAggregationService filterAggregationService;

    public ReportingFilterController(ReportFilterAggregationService filterAggregationService) {
        this.filterAggregationService = filterAggregationService;
    }

    @GetMapping("/cities")
    public ResponseEntity getCities(@RequestParam(value = "agent", required = false) List<Long> agents) {
        return ResponseEntity.ok(filterAggregationService.getCities(agents));
    }

    @GetMapping("/zipCodes")
    public ResponseEntity getZipCodes(
            @RequestParam(value = "city", required = false) List<String> cities,
            @RequestParam(value = "agent", required = false) List<Long> agents) {
        return ResponseEntity.ok(filterAggregationService.getZipCodes(cities, agents));
    }
}
