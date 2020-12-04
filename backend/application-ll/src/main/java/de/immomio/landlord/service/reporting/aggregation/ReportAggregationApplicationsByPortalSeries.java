package de.immomio.landlord.service.reporting.aggregation;

import de.immomio.data.landlord.entity.user.reporting.enums.ReportChart;
import de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants;
import de.immomio.landlord.service.reporting.data.chart.SeriesChartHandler;
import de.immomio.landlord.service.reporting.query.ElasticsearchQueryHandler;
import de.immomio.reporting.model.chart.ReportingChartData;
import de.immomio.reporting.model.filter.ReportingFilterBean;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants.QUERY_APPLICATION_PORTAL_KEYWORD;
import static de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants.QUERY_PORTAL;

/**
 * @author Andreas Hansen
 */
@Service(ReportChartConstants.AGGR_BEAN_APPLICATIONS_BY_PORTAL_SERIES)
public class ReportAggregationApplicationsByPortalSeries implements ReportAggregationInterface {

    private SeriesChartHandler seriesChartHandler;
    private ElasticsearchQueryHandler queryHandler;

    @Autowired
    public ReportAggregationApplicationsByPortalSeries(
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
        baseQuery.must(new ExistsQueryBuilder(QUERY_APPLICATION_PORTAL_KEYWORD));

        return seriesChartHandler.getDateHistogramChartData(
                filterBean,
                reportChart.getChartHandlerIndexPrefix(),
                baseQuery,
                reportChart,
                AggregationBuilders.terms(QUERY_PORTAL).field(QUERY_APPLICATION_PORTAL_KEYWORD));
    }

}
