package de.immomio.landlord.service.reporting.data.chart;

import de.immomio.data.landlord.entity.user.reporting.enums.ReportChart;
import de.immomio.landlord.service.reporting.data.BaseLandlordDataHandler;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.reporting.model.chart.ReportingChartData;
import de.immomio.reporting.model.chart.ReportingChartPoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.Locale;

/**
 * @author Niklas Lindemann
 */
@Slf4j
@Component
public class PieChartHandler extends BaseLandlordDataHandler {

    private static final String REPORTING_LABEL_NOT_AVAILABLE = "REPORTING_LABEL_NOT_AVAILABLE";
    private static final String REPORTING_BOOLEAN_PREFIX = "REPORTING_";
    private static final String DASH = "-";
    private static final String SPACE_DASH_SPACE = " - ";
    private static final String LOWER_SPACE = "< ";
    private static final String BIGGER_SPACE = "> ";
    private static final String ASTERISK = "*";


    @Autowired
    public PieChartHandler(
            UserSecurityService userSecurityService,
            RestHighLevelClient client
    ) {
        super(userSecurityService, client);
    }

    public ReportingChartData getRangeChartData(
            String indexPrefix,
            QueryBuilder queryBuilder,
            String field,
            ReportChart reportChart
    ) {
        RangeAggregationBuilder rangeAggregation = AggregationBuilders.range(DEFAULT)
                .addUnboundedTo(REPORTING_LABEL_NOT_AVAILABLE, 1)
                .addRange(1, 500)
                .addRange(500, 1000)
                .addRange(1000, 1500)
                .addRange(1500, 2000)
                .addRange(2000, 2500)
                .addRange(2500, 3000)
                .addRange(3000, 3500)
                .addRange(3500, 4000)
                .addRange(4000, 5000)
                .addRange(5000, 6000)
                .addRange(6000, 7000)
                .addRange(7000, 8000)
                .addRange(8000, 9000)
                .addRange(9000, 10000)
                .addRange(10000, 15000)
                .addRange(15000, 20000)
                .addUnboundedFrom(20000).field(field);
        SearchRequest request = new SearchRequest(indexPrefix + getCustomerId());

        try {
            ParsedRange range = (ParsedRange) performQuery(request, rangeAggregation, queryBuilder, DEFAULT);
            ReportingChartData reportingChartData = extractSeriesData(range);
            appendTitle(reportChart, reportingChartData);
            return reportingChartData;
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return new ReportingChartData();
    }

    public ReportingChartData getPieChartData(
            String indexPrefix,
            QueryBuilder baseQuery,
            Script script,
            ReportChart reportChart
    ) {
        SearchRequest request = new SearchRequest(indexPrefix + getCustomerId());
        TermsAggregationBuilder termsAggregation = AggregationBuilders.terms(DEFAULT).script(script);
        try {
            ParsedTerms terms =
                    (ParsedTerms) performQuery(request, termsAggregation, baseQuery, DEFAULT);

            ReportingChartData reportingChartData = extractSeriesData(terms);
            appendTitle(reportChart, reportingChartData);
            return reportingChartData;
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }

        return new ReportingChartData();
    }

    public ReportingChartData getPieChartData(
            String indexPrefix,
            QueryBuilder baseQuery,
            String field,
            ReportChart reportChart
    ) {
        SearchRequest request = new SearchRequest(indexPrefix + getCustomerId());
        TermsAggregationBuilder termsAggregation = AggregationBuilders.terms(DEFAULT).field(field);
        try {
            ParsedTerms terms =
                    (ParsedTerms) performQuery(request, termsAggregation, baseQuery, DEFAULT);

            ReportingChartData reportingChartData = extractSeriesData(terms);
            appendTitle(reportChart, reportingChartData);
            return reportingChartData;
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }

        return new ReportingChartData();
    }

    private void appendTitle(ReportChart reportChart, ReportingChartData reportingChartData) {
        reportingChartData.setTitle(reportChart.getTitle());
    }

    private ReportingChartData extractSeriesData(ParsedTerms terms) {
        ReportingChartData reportingChartData = new ReportingChartData();
        terms.getBuckets().forEach(bucket -> appendBucketToChartData(reportingChartData, bucket));
        return reportingChartData;
    }

    private ReportingChartData extractSeriesData(ParsedRange range) {
        ReportingChartData reportingChartData = new ReportingChartData();
        range.getBuckets().stream()
                .filter(bucket -> bucket.getDocCount() > 0)
                .forEach(bucket -> appendBucketToChartData(reportingChartData, bucket));

        return reportingChartData;
    }

    private void appendBucketToChartData(ReportingChartData reportingChartData, MultiBucketsAggregation.Bucket bucket) {
        ReportingChartPoint reportingChartPoint = new ReportingChartPoint();
        reportingChartPoint.getValues().put(getKeyFromBucket(bucket), bucket.getDocCount());
        reportingChartData.getData().addAll(Collections.singletonList(reportingChartPoint));
    }

    private String getKeyFromBucket(MultiBucketsAggregation.Bucket bucket) {
        String key = bucket.getKeyAsString();
        Boolean boolKey = BooleanUtils.toBooleanObject(key);
        if (boolKey != null) {
            return getBoolKey(boolKey);
        }
        if (bucket instanceof ParsedRange.ParsedBucket) {
            return getRangeKey(key);
        }

        return getDefaultKey(key);
    }

    private String getDefaultKey(String key) {
        return StringUtils.isNotBlank(key) ? key : REPORTING_LABEL_NOT_AVAILABLE;
    }

    private String getBoolKey(Boolean boolKey) {
        return REPORTING_BOOLEAN_PREFIX + boolKey.toString().toUpperCase();
    }

    private String getRangeKey(String key) {
        String[] splitted = key.split(DASH);
        if (splitted.length >= 2) {
            String first = splitted[0];
            String second = splitted[1];

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
            NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMANY);
            currencyFormat.setMaximumFractionDigits(0);
            numberFormat.setMaximumFractionDigits(0);
            if (NumberUtils.isCreatable(first) && NumberUtils.isCreatable(second)) {
                first = numberFormat.format(Double.valueOf(first));
                second = currencyFormat.format(Double.valueOf(second));
                return first + SPACE_DASH_SPACE + second;
            }
            if (ASTERISK.equals(first) && NumberUtils.isCreatable(second)) {
                return LOWER_SPACE + currencyFormat.format(Double.valueOf(second));
            }
            if (ASTERISK.equals(second) && NumberUtils.isCreatable(first)) {
                return BIGGER_SPACE + currencyFormat.format(Double.valueOf(first));
            }
        }

        return getDefaultKey(key);
    }

}
