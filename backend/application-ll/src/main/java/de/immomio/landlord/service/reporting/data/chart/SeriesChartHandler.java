package de.immomio.landlord.service.reporting.data.chart;

import de.immomio.data.landlord.entity.user.reporting.enums.ReportChart;
import de.immomio.landlord.service.reporting.data.BaseLandlordDataHandler;
import de.immomio.landlord.service.reporting.extractor.series.DefaultSeriesExtractor;
import de.immomio.landlord.service.reporting.extractor.series.SeriesExtractor;
import de.immomio.landlord.service.reporting.query.AggregationQueryHelper;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.reporting.model.chart.ReportingChartData;
import de.immomio.reporting.model.chart.ReportingChartPoint;
import de.immomio.reporting.model.filter.ReportingFilterBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Component
public class SeriesChartHandler extends BaseLandlordDataHandler {

    private static final String TIMESTAMP_FIELD = "timestamp";

    private AggregationQueryHelper queryHelper;

    @Autowired
    public SeriesChartHandler(
            UserSecurityService userSecurityService,
            RestHighLevelClient client,
            AggregationQueryHelper queryHelper
    ) {
        super(userSecurityService, client);
        this.userSecurityService = userSecurityService;
        this.client = client;
        this.queryHelper = queryHelper;
    }

    public ReportingChartData getDateHistogramChartData(
            ReportingFilterBean filterBean,
            String indexPrefix,
            BoolQueryBuilder baseQuery,
            ReportChart reportChart,
            String chartLabel,
            AggregationBuilder subAggregationBuilder,
            String dateField,
            SeriesExtractor seriesExtractor
    ) {
        if (seriesExtractor == null) {
            seriesExtractor = new DefaultSeriesExtractor();
        }
        SearchRequest request = new SearchRequest(getIndexName(indexPrefix));
        DateHistogramAggregationBuilder aggregation = getDateHistogramAggregationBuilder(
                filterBean,
                subAggregationBuilder,
                dateField
        );
        try {
            ParsedDateHistogram parsedDateHistogram = (ParsedDateHistogram)
                    performQuery(request, aggregation, baseQuery, DEFAULT);

            return extractSeriesData(parsedDateHistogram, reportChart, chartLabel, seriesExtractor);
        } catch (IOException e) {
            log.info(e.getMessage(), e);
        }

        return null;
    }

    private String getIndexName(String indexPrefix) {
        return indexPrefix + getCustomerId();
    }

    public ReportingChartData getDateHistogramChartData(
            ReportingFilterBean filterBean,
            String indexPrefix,
            BoolQueryBuilder baseQuery,
            ReportChart reportChart,
            String chartLabel
    ) {
        return getDateHistogramChartData(
                filterBean,
                indexPrefix,
                baseQuery,
                reportChart,
                chartLabel,
                null,
                null,
                null
        );
    }

    public ReportingChartData getDateHistogramChartData(
            ReportingFilterBean filterBean,
            String indexPrefix,
            BoolQueryBuilder baseQuery,
            ReportChart reportChart,
            AggregationBuilder aggregationBuilder
    ) {
        return getDateHistogramChartData(
                filterBean,
                indexPrefix,
                baseQuery,
                reportChart,
                null,
                aggregationBuilder,
                null,
                null
        );
    }

    private ReportingChartData extractSeriesData(
            ParsedDateHistogram dateHistogram,
            ReportChart reportChart,
            String label,
            SeriesExtractor extractionStrategy) {
        ReportingChartData reportingChartData = new ReportingChartData();
        reportingChartData.setTitle(reportChart.getTitle());
        Set<String> existingKeys = new HashSet<>();
        List<ReportingChartPoint> points = dateHistogram.getBuckets().stream()
                .map(bucket -> extractionStrategy.getReportingChartPoint(label, existingKeys, bucket))
                .collect(Collectors.toList());
        reportingChartData.setData(points);
        appendEmptyChartData(points, existingKeys);

        return reportingChartData;
    }

    // filling up the empty data, making FE's live easier
    private void appendEmptyChartData(List<ReportingChartPoint> points, Set<String> existingLabels) {
        points.forEach(seriesChartPoint -> existingLabels.forEach(
                label -> seriesChartPoint.getValues().putIfAbsent(label, 0L)
        ));
    }

    private DateHistogramAggregationBuilder getDateHistogramAggregationBuilder(
            ReportingFilterBean filterBean,
            AggregationBuilder subAggregation,
            String dateField) {
        DateHistogramInterval dateHistogramInterval = new DateHistogramInterval(filterBean.getInterval());

        String aggregationField = StringUtils.isNotBlank(dateField) ? dateField : TIMESTAMP_FIELD;
        DateHistogramAggregationBuilder dateAggregation = AggregationBuilders.dateHistogram(DEFAULT)
                .field(aggregationField)
                .dateHistogramInterval(dateHistogramInterval)
                .extendedBounds(new ExtendedBounds(filterBean.getStart().getTime(), filterBean.getEnd().getTime()));
        if (subAggregation != null) {
            dateAggregation.subAggregation(subAggregation);
        }

        return dateAggregation;
    }

}
