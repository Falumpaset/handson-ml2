package de.immomio.landlord.service.reporting.extractor.series;

import de.immomio.reporting.model.chart.ReportingChartPoint;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;

import java.util.Set;

/**
 * @author Niklas Lindemann
 */
public interface SeriesExtractor {
    ReportingChartPoint getReportingChartPoint(String label, Set<String> existingKeys, Histogram.Bucket bucket);
}
