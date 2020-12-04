package de.immomio.service.report;

import com.opencsv.CSVWriter;
import de.immomio.bean.report.LandlordReportBean;
import de.immomio.bean.report.PropertySearcherReportBean;
import de.immomio.bean.report.TotalUserReportBean;
import de.immomio.model.entity.admin.report.Report;
import de.immomio.model.entity.admin.report.ReportType;
import de.immomio.model.entity.admin.report.TimespanType;
import de.immomio.model.repository.core.admin.report.BaseReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class SaveReportService {

    @Value("${timezone.europe}")
    private String EUROPE_BERLIN;

    private final ReportCalculationService userReportService;

    private final DecimalFormat decimalFormatter = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.GERMAN);

    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.GERMANY);

    private BaseReportRepository reportRepository;

    private static final String[] COLUMNS_TOTAL_REPORT = {"LL in total", "Revenue of LL", "PS in total",
            "Proposals in total", "Prospects in total", "Objects in total", "Revenue per object"};

    private static final String[] COLUMNS_LL_REPORT = {"ID", "Name",
            "Created Date", "Flats under management", "Number of Addons",
            "Addons", "Email", "Revenue", "Payment cycle", "Payment method"};

    private static final String[] COLUMNS_PS_REPORT = {"First Name", "Last Name",
            "Email", "Opt-In prospect"};

    @Autowired
    public SaveReportService(
            ReportCalculationService userReportService,
            BaseReportRepository reportRepository
    ) {
        this.userReportService = userReportService;
        this.reportRepository = reportRepository;
    }

    public void saveTotalReport(LocalDate localStartDate, LocalDate localEndDate) {
        log.info("start total report generation");
        Date startDate = getStartDate(localStartDate);
        Date endDate = getEndDate(localEndDate);
        TotalUserReportBean totalUserReport = userReportService.fillTotalUserReport(startDate, endDate);
        StringWriter sw = new StringWriter();

        try {
            CSVWriter csvWriter = getCsvWriter(sw);

            csvWriter.writeNext(COLUMNS_TOTAL_REPORT);

            String[] data = {decimalFormatter.format(totalUserReport.getLandlordCount()),
                    currencyFormatter.format(totalUserReport.getRevenueLL()),
                    decimalFormatter.format(totalUserReport.getPropertySearcherCount()),
                    decimalFormatter.format(totalUserReport.getProposalsInTotal()),
                    decimalFormatter.format(totalUserReport.getProspectsInTotal()),
                    decimalFormatter.format(totalUserReport.getObjectsInTotal()),
                    currencyFormatter.format(totalUserReport.getRevenuePerObject())};

            csvWriter.writeNext(data);
            saveReport(sw.toString(), ReportType.TOTAL, startDate, endDate);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("end total report generation");
    }

    public void saveLandlordReport(LocalDate localStartDate, LocalDate localEndDate) {
        log.info("start landlord report generation");
        Date startDate = getStartDate(localStartDate);
        Date endDate = getEndDate(localEndDate);
        List<LandlordReportBean> landlordReports =
                userReportService.fillLandlordReportList(getStartDate(localStartDate), getEndDate(localEndDate));
        StringWriter sw = new StringWriter();

        try {
            CSVWriter csvWriter = getCsvWriter(sw);

            csvWriter.writeNext(COLUMNS_LL_REPORT);

            landlordReports.forEach(landlordReport -> {
                String[] data = {landlordReport.getId().toString(),
                        landlordReport.getName(),
                        landlordReport.getCreatedDate(),
                        landlordReport.getFlatsUnderManagement().toString(),
                        landlordReport.getAddonCount().toString(),
                        landlordReport.getLandlordAddonProducts(),
                        landlordReport.getEmail(),
                        currencyFormatter.format(landlordReport.getRevenue()),
                        landlordReport.getSubscriptionPeriod(),
                        landlordReport.getPaymentMethod().getMethod().name()
                };
                csvWriter.writeNext(data);
            });
            saveReport(sw.toString(), ReportType.LANDLORD, startDate, endDate);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("end landlord report generation");
    }

    public void savePropertySearcherReport(LocalDate localStartDate, LocalDate localEndDate) {
        log.info("start ps report generation");
        Date startDate = getStartDate(localStartDate);
        Date endDate = getEndDate(localEndDate);
        List<PropertySearcherReportBean> propertySearcherReports = userReportService.fillPropertySearcherReport();
        StringWriter sw = new StringWriter();
        try {
            CSVWriter csvWriter = getCsvWriter(sw);

            csvWriter.writeNext(COLUMNS_PS_REPORT);
            propertySearcherReports.forEach(propertySearcherReport -> {
                String[] data = {propertySearcherReport.getFirstName(),
                        propertySearcherReport.getLastName(),
                        propertySearcherReport.getEmail(),
                        Boolean.toString(propertySearcherReport.isOptInProspect())
                };
                csvWriter.writeNext(data);
            });
            saveReport(sw.toString(), ReportType.PROPERTYSEARCHER, startDate, endDate);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("end ps report generation");
    }

    private void saveReport(String csvReport, ReportType reportType, Date startDate, Date endDate) {
        Report report = new Report();
        report.setType(reportType);
        report.setTimespan(TimespanType.MONTHLY);
        report.setReport(csvReport);
        report.setStartdate(startDate);
        report.setEnddate(endDate);
        reportRepository.save(report);
    }

    private CSVWriter getCsvWriter(Writer writer) {
        return new CSVWriter(writer,
                ';',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);
    }

    private Date getStartDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Date getEndDate(LocalDate localDate) {
        LocalDateTime endOfDay = localDate.atTime(23, 59, 59);
        ZoneId zone = ZoneId.of(EUROPE_BERLIN);
        ZoneOffset zoneOffSet = zone.getRules().getOffset(endOfDay);
        return Date.from(endOfDay.toInstant(zoneOffSet));
    }
}
