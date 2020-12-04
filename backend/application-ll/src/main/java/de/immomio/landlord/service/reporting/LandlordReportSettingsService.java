package de.immomio.landlord.service.reporting;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.reporting.ChartSetting;
import de.immomio.data.landlord.entity.user.reporting.enums.ReportChart;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.landlord.customer.user.report.ChartSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Niklas Lindemann
 */

@Service
public class LandlordReportSettingsService {

    private ChartSettingRepository chartSettingRepository;

    private UserSecurityService securityService;

    @Autowired
    public LandlordReportSettingsService(
            ChartSettingRepository chartSettingRepository,
            UserSecurityService securityService
    ) {
        this.chartSettingRepository = chartSettingRepository;
        this.securityService = securityService;
    }

    public ChartSetting makeFavourite(ReportChart reportChart, Boolean favourite) {
        LandlordUser principalUser = securityService.getPrincipalUser();
        Optional<ChartSetting> chartSettingOpt = chartSettingRepository.findByUserAndChart(principalUser, reportChart);

        ChartSetting chartSetting = chartSettingOpt.orElseGet(() -> {
            ChartSetting newChartSetting = new ChartSetting();
            newChartSetting.setUser(principalUser);
            newChartSetting.setChart(reportChart);
            return newChartSetting;
        });
        chartSetting.setFavourite(favourite);

        return chartSettingRepository.save(chartSetting);
    }

}
