package de.immomio.landlord.service.reporting.aggregation;

import de.immomio.data.landlord.entity.user.reporting.enums.ReportChart;
import de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants;
import de.immomio.landlord.service.reporting.data.chart.SeriesChartHandler;
import de.immomio.landlord.service.reporting.query.ElasticsearchQueryHandler;
import de.immomio.reporting.model.chart.ReportingChartData;
import de.immomio.reporting.model.filter.ReportingFilterBean;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andreas Hansen
 */
@Service(ReportChartConstants.AGGR_BEAN_BASE_CHART_DATA_SERIES)
public class ReportAggregationBaseChartDataSeries implements ReportAggregationInterface {

    private SeriesChartHandler seriesChartHandler;
    private ElasticsearchQueryHandler queryHandler;

    @Autowired
    public ReportAggregationBaseChartDataSeries(
            SeriesChartHandler seriesChartHandler,
            ElasticsearchQueryHandler queryHandler
    ) {
        this.seriesChartHandler = seriesChartHandler;
        this.queryHandler = queryHandler;
    }

    public ReportingChartData getReportingData(ReportChart reportChart, ReportingFilterBean filterBean) {
        BoolQueryBuilder baseQuery = queryHandler.getBaseDateRangeQuery(
                filterBean,
                reportChart.getQueryPropertyPrefix(),
                reportChart.getEventTypes()
        );

        return seriesChartHandler.getDateHistogramChartData(
                filterBean,
                reportChart.getChartHandlerIndexPrefix(),
                baseQuery,
                reportChart,
                reportChart.getChartLabel());
    }

}
