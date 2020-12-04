package de.immomio.landlord.service.reporting.aggregation;

import de.immomio.data.landlord.entity.user.reporting.enums.ReportChart;
import de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants;
import de.immomio.landlord.service.reporting.data.chart.PieChartHandler;
import de.immomio.landlord.service.reporting.query.ElasticsearchQueryHandler;
import de.immomio.reporting.model.chart.ReportingChartData;
import de.immomio.reporting.model.filter.ReportingFilterBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants.QUERY_ZIP_CODE_FIELD;
import static de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants.QUERY_ZIP_CODE_SCRIPT;
import static de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConstants.QUERY_ZIP_CODE_SUFFIX;

/**
 * @author Andreas Hansen
 */
@Service(ReportChartConstants.AGGR_BEAN_ZIP_DISTRIBUTION_DATA)
public class ReportAggregationZipDistributionData implements ReportAggregationInterface {

    private final PieChartHandler pieChartHandler;
    private final ElasticsearchQueryHandler queryHandler;

    @Autowired
    public ReportAggregationZipDistributionData(
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
        ExistsQueryBuilder zipExists = QueryBuilders.existsQuery(QUERY_ZIP_CODE_FIELD);
        baseQuery.must(zipExists);

        ReportingChartData reportingChartData = pieChartHandler.getPieChartData(
                reportChart.getChartHandlerIndexPrefix(),
                baseQuery,
                new Script(QUERY_ZIP_CODE_SCRIPT),
                reportChart);

        appendZipCodeSuffixes(reportingChartData);

        return reportingChartData;
    }

    private void appendZipCodeSuffixes(ReportingChartData reportingChartData) {
        reportingChartData.getData().forEach(pieChartValue -> {
            Map<String, Number> values = pieChartValue.getValues();
            Map<String, Number> appendedValues = new HashMap<>();
            values.forEach((key, amount) -> {
                if (StringUtils.isNotBlank(key)) {
                    appendedValues.put(NumberUtils.isCreatable(key) ? key + QUERY_ZIP_CODE_SUFFIX : key, amount);
                }
            });
            pieChartValue.setValues(appendedValues);
        });
    }

}
