package de.immomio.landlord.service.reporting.aggregation;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.opencsv.CSVWriter;
import de.immomio.config.ApplicationMessageSource;
import de.immomio.reporting.model.chart.ReportingChartData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Andreas Hansen
 */
@Service
public class ReportCsvConverter {

    private static final String CSV_FIRST_ROW_LABEL = "Label";
    private static final String CSV_DATE_FORMAT_PATTERN = "dd.MM.yyyy";

    private final ApplicationMessageSource applicationMessageSource;

    @Autowired
    public ReportCsvConverter(ApplicationMessageSource applicationMessageSource) {
        this.applicationMessageSource = applicationMessageSource;
    }

    public String convertToCsv(ReportingChartData reportingChartData) throws Exception {
        String firstRowLabel = CSV_FIRST_ROW_LABEL;
        SimpleDateFormat sdf = new SimpleDateFormat(CSV_DATE_FORMAT_PATTERN);
        Multimap<String, String> csvData = LinkedListMultimap.create();
        NumberFormat decimalFormat = DecimalFormat.getInstance(Locale.GERMANY);
        reportingChartData.getData().forEach(chartPoint -> {
            if (chartPoint.getDate() != null) {
                csvData.put(firstRowLabel, sdf.format(chartPoint.getDate()));
            }
            chartPoint.getValues().forEach((rowKey, value) -> {
                String rowLabel = applicationMessageSource.resolveCodeString(rowKey, Locale.GERMAN);
                csvData.put(rowLabel, decimalFormat.format(value));
            });
        });

        return getCsvData(csvData);
    }

    private <T, Y> String getCsvData(Multimap<String, String> data) throws Exception {
        String csvData;

        try (Writer stringWriter = new StringWriter()) {

            CSVWriter csvWriter = new CSVWriter(
                    stringWriter,
                    ';',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.NO_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END

            );

            data.asMap().forEach((k, v) -> {
                List<String> arrayList = new ArrayList<>();
                arrayList.add(k);
                arrayList.addAll(v);
                csvWriter.writeNext(arrayList.toArray(String[]::new));
            });

            csvData = stringWriter.toString();
        }

        return csvData;
    }

}
