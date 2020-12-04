package de.immomio.landlord.service.reporting.aggregation;

import de.immomio.data.landlord.entity.user.reporting.enums.ReportChart;
import de.immomio.reporting.model.chart.ReportingChartData;
import de.immomio.reporting.model.filter.ReportingFilterBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportDataService {

    private final ReportCsvConverter reportCsvConverter;
    private final ReportZipConverter reportZipConverter;
    private final ApplicationContext appContext;

    @Autowired
    public ReportDataService(
            ReportCsvConverter reportCsvConverter,
            ReportZipConverter reportZipConverter,
            ApplicationContext appContext
    ) {
        this.reportCsvConverter = reportCsvConverter;
        this.reportZipConverter = reportZipConverter;
        this.appContext = appContext;
    }

    public byte[] getReportCsvContent(ReportingFilterBean filterBean, String endpoint) throws Exception {
        ReportChart reportChart = ReportChart.findByEndpoint(endpoint);
        ReportingChartData chartData = getReportingData(filterBean, reportChart);
        String csvContent = reportCsvConverter.convertToCsv(chartData);

        return csvContent.getBytes(StandardCharsets.ISO_8859_1);
    }

    public byte[] getAllReportsZipContent(ReportingFilterBean filterBean) throws Exception {
        Map<String, String> allReports = new HashMap<>();
        for (ReportChart reportChart : ReportChart.values()) {
            allReports.put(
                    reportChart.getTitle(),
                    reportCsvConverter.convertToCsv(getReportingData(filterBean, reportChart))
            );
        }
        return reportZipConverter.getZip(allReports);
    }


    private ReportingChartData getReportingData(
            ReportingFilterBean filterBean,
            ReportChart reportChart
    ) {
        ReportAggregationInterface aggregation = (ReportAggregationInterface) appContext.getBean(
                reportChart.getAggregationBeanQualifierName()
        );

        return aggregation.getReportingData(reportChart, filterBean);
    }

}
