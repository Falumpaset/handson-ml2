package de.immomio.landlord.service.reporting;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.reporting.model.filter.ReportingFilterBean;
import de.immomio.service.AbstractTest;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author Niklas Lindemann
 */
public class LandlordReportValidationServiceTest extends AbstractTest {

    @InjectMocks
    private LandlordReportValidationService landlordReportValidationService;

    @Test
    public void validateReportingBeanValid() {
        ReportingFilterBean reportingFilterBean = new ReportingFilterBean();
        Date from = Date.from(LocalDate.of(2017, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date to = Date.from(LocalDate.of(2017, 2, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        reportingFilterBean.setInterval("1h");
        reportingFilterBean.setStart(from);
        reportingFilterBean.setEnd(to);
        landlordReportValidationService.validateReportingBean(reportingFilterBean);
    }

    @Test(expected = ApiValidationException.class)
    public void validateReportingBeanInvalidDateRange() {
        ReportingFilterBean reportingFilterBean = new ReportingFilterBean();
        Date from = Date.from(LocalDate.of(2017, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date to = Date.from(LocalDate.of(2017, 12, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        reportingFilterBean.setInterval("1h");
        reportingFilterBean.setStart(from);
        reportingFilterBean.setEnd(to);
        landlordReportValidationService.validateReportingBean(reportingFilterBean);
    }
    @Test(expected = ApiValidationException.class)
    public void validateReportingBeanInvalidNull() {
        ReportingFilterBean reportingFilterBean = new ReportingFilterBean();
        Date from = Date.from(LocalDate.of(2017, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date to = Date.from(LocalDate.of(2017, 2, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        reportingFilterBean.setStart(from);
        reportingFilterBean.setEnd(to);
        landlordReportValidationService.validateReportingBean(reportingFilterBean);
    }
}