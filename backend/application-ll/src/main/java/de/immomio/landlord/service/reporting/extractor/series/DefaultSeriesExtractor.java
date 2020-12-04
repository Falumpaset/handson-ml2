package de.immomio.landlord.service.reporting.extractor.series;

import de.immomio.reporting.model.chart.ReportingChartPoint;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */
public class DefaultSeriesExtractor implements SeriesExtractor {

    @Override
    public ReportingChartPoint getReportingChartPoint(String label, Set<String> existingKeys, Histogram.Bucket bucket) {
        Map<String, Number> chartValues = new HashMap<>();
        if (bucket.getAggregations().asList().isEmpty()) {
            chartValues.put(label, bucket.getDocCount());
        } else {
            putMultiValuesToChart(chartValues, bucket);
            existingKeys.addAll(chartValues.keySet());
        }

        Date bucketDate = new Date(Long.parseLong(bucket.getKeyAsString()));

        return new ReportingChartPoint(bucketDate, chartValues);
    }

    protected void putMultiValuesToChart(Map<String, Number> chartValues, Histogram.Bucket bucket) {
        bucket.getAggregations().forEach(aggregation -> {
            Map<String, Long> collect;
            Terms terms = (Terms) aggregation;
            collect = terms.getBuckets().stream()
                    .collect(Collectors.toMap(element -> element.getKey().toString(),
                            MultiBucketsAggregation.Bucket::getDocCount));
            chartValues.putAll(collect);
        });
    }

}
