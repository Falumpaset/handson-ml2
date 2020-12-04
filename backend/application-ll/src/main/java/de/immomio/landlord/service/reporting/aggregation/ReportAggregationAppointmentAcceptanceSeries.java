package de.immomio.landlord.service.reporting.aggregation;

import de.immomio.data.landlord.entity.user.reporting.enums.ReportChart;
import de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants;
import de.immomio.landlord.service.reporting.data.chart.SeriesChartHandler;
import de.immomio.landlord.service.reporting.query.ElasticsearchQueryHandler;
import de.immomio.reporting.model.chart.ReportingChartData;
import de.immomio.reporting.model.filter.ReportingFilterBean;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants.QUERY_EVENTTYPE;
import static de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants.QUERY_EVENT_TYPE_KEYWORD;

/**
 * @author Andreas Hansen
 */
@Service(ReportChartConstants.AGGR_BEAN_APPOINTMENT_ACCEPTANCE_SERIES)
public class ReportAggregationAppointmentAcceptanceSeries implements ReportAggregationInterface {

    private SeriesChartHandler seriesChartHandler;
    private ElasticsearchQueryHandler queryHandler;

    @Autowired
    public ReportAggregationAppointmentAcceptanceSeries(
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
                reportChart.getEventTypes());

        return seriesChartHandler.getDateHistogramChartData(
                filterBean,
                reportChart.getChartHandlerIndexPrefix(),
                baseQuery,
                reportChart,
                AggregationBuilders.terms(QUERY_EVENTTYPE).field(QUERY_EVENT_TYPE_KEYWORD));
    }

}
