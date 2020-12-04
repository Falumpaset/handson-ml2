package de.immomio.controller.reporting;

import de.immomio.data.landlord.entity.user.reporting.ChartSetting;
import de.immomio.data.landlord.entity.user.reporting.enums.ReportChart;
import de.immomio.landlord.service.reporting.LandlordReportSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/reports/settings")
public class ReportingSettingsController {
    private LandlordReportSettingsService reportSettingsService;

    @Autowired
    public ReportingSettingsController(LandlordReportSettingsService reportSettingsService) {
        this.reportSettingsService = reportSettingsService;
    }

    @PostMapping("/tagFavourite")
    public ResponseEntity tagFavourite(@RequestParam("reportChart") ReportChart reportChart, @RequestParam("favourite")  Boolean favourite) {
        ChartSetting chartSetting = reportSettingsService.makeFavourite(reportChart, favourite);
        return ResponseEntity.ok().build();
    }
}
