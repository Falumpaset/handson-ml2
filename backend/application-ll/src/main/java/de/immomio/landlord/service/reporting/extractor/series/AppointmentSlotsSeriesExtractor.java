package de.immomio.landlord.service.reporting.extractor.series;

import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.metrics.avg.ParsedAvg;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

/**
 * @author Niklas Lindemann
 */
public class AppointmentSlotsSeriesExtractor extends DefaultSeriesExtractor {

    private static final String INVITEE_COUNT_AVG = "INVITEE_COUNT_AVG_L";

    @Override
    protected void putMultiValuesToChart(Map<String, Number> chartValues, Histogram.Bucket bucket) {
        bucket.getAggregations().forEach(aggregation -> {
            ParsedAvg avg = (ParsedAvg) aggregation;

            double value = avg.getValue();

            float inviteeCount = 0.0f;

            if (Double.isFinite(value)) {
                DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.GERMAN);
                decimalFormatSymbols.setDecimalSeparator('.');
                DecimalFormat decimalFormat = new DecimalFormat("#.#", decimalFormatSymbols);

                inviteeCount = Float.parseFloat(decimalFormat.format(value));
            }

            chartValues.put(INVITEE_COUNT_AVG, inviteeCount);
        });
    }

}
