package de.immomio.landlord.service.reporting.aggregation;

import de.immomio.data.landlord.entity.user.reporting.enums.ReportChart;
import de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants;
import de.immomio.landlord.service.reporting.data.chart.PieChartHandler;
import de.immomio.landlord.service.reporting.query.ElasticsearchQueryHandler;
import de.immomio.reporting.model.chart.ReportingChartData;
import de.immomio.reporting.model.filter.ReportingFilterBean;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andreas Hansen
 */
@Service(ReportChartConstants.AGGR_BEAN_INCOME_DISTRIBUTION_DATA)
public class ReportAggregationIncomeDistributionData implements ReportAggregationInterface {

    private PieChartHandler pieChartHandler;
    private ElasticsearchQueryHandler queryHandler;

    @Autowired
    public ReportAggregationIncomeDistributionData(
            PieChartHandler pieChartHandler,
            ElasticsearchQueryHandler queryHandler
    ) {
        this.pieChartHandler = pieChartHandler;
        this.queryHandler = queryHandler;
    }

    public ReportingChartData getReportingData(ReportChart reportChart, ReportingFilterBean filterBean) {
        BoolQueryBuilder baseQuery = queryHandler.getBaseDateRangeQuery(
                filterBean,
                reportChart.getQueryPropertyPrefix()
        );
        return pieChartHandler.getRangeChartData(
                reportChart.getChartHandlerIndexPrefix(),
                baseQuery,
                reportChart.getPieChartAggregateFieldname(),
                reportChart);
    }

}
