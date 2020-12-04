package de.immomio.landlord.service.reporting.aggregation;

import de.immomio.data.landlord.entity.user.reporting.enums.ReportChart;
import de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants;
import de.immomio.landlord.service.reporting.data.chart.SeriesChartHandler;
import de.immomio.landlord.service.reporting.extractor.series.AppointmentSlotsSeriesExtractor;
import de.immomio.landlord.service.reporting.query.ElasticsearchQueryHandler;
import de.immomio.reporting.model.chart.ReportingChartData;
import de.immomio.reporting.model.filter.ReportingFilterBean;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants.APPOINTMENT_QUERY_DATE;
import static de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants.APPOINTMENT_QUERY_STATE;
import static de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants.QUERY_ACTIVE;
import static de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants.QUERY_APPOINTMENT_MAX_INVITEE_COUNT;
import static de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants.QUERY_INVITEE_COUNT;

/**
 * @author Andreas Hansen
 */
@Service(ReportChartConstants.AGGR_BEAN_APPOINTMENT_SLOTS_SERIES)
public class ReportAggregationAppointmentSlotsSeries implements ReportAggregationInterface {

    private SeriesChartHandler seriesChartHandler;
    private ElasticsearchQueryHandler queryHandler;

    @Autowired
    public ReportAggregationAppointmentSlotsSeries(
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
                APPOINTMENT_QUERY_DATE,
                reportChart.getEventTypes());
        baseQuery.must(getAppointmentActiveQuery());
        AvgAggregationBuilder inviteeCountAggregation = AggregationBuilders
                .avg(QUERY_INVITEE_COUNT)
                .field(QUERY_APPOINTMENT_MAX_INVITEE_COUNT);

        return seriesChartHandler.getDateHistogramChartData(
                filterBean,
                reportChart.getChartHandlerIndexPrefix(),
                baseQuery,
                reportChart,
                reportChart.getChartLabel(),
                inviteeCountAggregation,
                APPOINTMENT_QUERY_DATE,
                new AppointmentSlotsSeriesExtractor());
    }

    private MatchQueryBuilder getAppointmentActiveQuery() {
        return QueryBuilders.matchQuery(APPOINTMENT_QUERY_STATE, QUERY_ACTIVE);
    }

}
