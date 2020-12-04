package de.immomio.landlord.service.reporting;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.landlord.entity.user.reporting.ChartSetting;
import de.immomio.data.landlord.entity.user.reporting.enums.ReportChart;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.landlord.customer.user.report.ChartSettingRepository;
import de.immomio.service.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import utils.TestHelper;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Niklas Lindemann
 */
public class LandlordReportSettingsServiceTest extends AbstractTest {

    @Mock
    private ChartSettingRepository chartSettingRepository;

    @Mock
    private UserSecurityService securityService;

    @InjectMocks
    private LandlordReportSettingsService reportSettingsService;

    @Test
    public void makeFavouriteExisting() {
        LandlordUser user = TestHelper.generateLandlordUser(LandlordUsertype.COMPANYADMIN, 1L);
        ChartSetting chartSetting = new ChartSetting();
        chartSetting.setChart(ReportChart.APPLICATIONS_BY_PORTAL);
        chartSetting.setUser(user);

        when(securityService.getPrincipalUser()).thenReturn(user);
        when(chartSettingRepository.findByUserAndChart(any(), any())).thenReturn(Optional.of(chartSetting));
        when(chartSettingRepository.save(any(ChartSetting.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        ChartSetting savedSetting = reportSettingsService.makeFavourite(ReportChart.APPLICATIONS_BY_PORTAL, true);
        Assert.assertTrue(savedSetting.getFavourite());
        verify(chartSettingRepository, times(1)).save(any(ChartSetting.class));
    }

    @Test
    public void makeFavouriteNew() {
        LandlordUser user = TestHelper.generateLandlordUser(LandlordUsertype.COMPANYADMIN, 1L);

        when(securityService.getPrincipalUser()).thenReturn(user);
        when(chartSettingRepository.findByUserAndChart(any(), any())).thenReturn(Optional.empty());
        when(chartSettingRepository.save(any(ChartSetting.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        ChartSetting savedSetting = reportSettingsService.makeFavourite(ReportChart.APPLICATIONS_BY_PORTAL, true);

        Assert.assertEquals(ReportChart.APPLICATIONS_BY_PORTAL, savedSetting.getChart());
        Assert.assertTrue(savedSetting.getFavourite());
        verify(chartSettingRepository, times(1)).save(any(ChartSetting.class));
    }
}