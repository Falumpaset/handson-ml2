package de.immomio.landlord.service.reporting.aggregation;

import de.immomio.data.landlord.entity.user.reporting.enums.ReportChart;
import de.immomio.reporting.model.chart.ReportingChartData;
import de.immomio.reporting.model.filter.ReportingFilterBean;

public interface ReportAggregationInterface {

    ReportingChartData getReportingData(ReportChart reportChart, ReportingFilterBean filterBean);

}
