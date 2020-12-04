package de.immomio.landlord.service.reporting;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.reporting.model.filter.ReportingFilterBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Niklas Lindemann
 */

@Service
public class LandlordReportValidationService {

    private static final int MAX_BUCKETS = 5000;
    private static final long THOUSAND = 1000L;
    private static final String SECONDS = "s";
    private static final String MINUTES = "m";
    private static final String HOURS = "h";
    private static final String DAYS = "d";
    private static final String WEEKS = "w";
    private static final String MONTHS = "M";
    private static final String QUARTERS = "q";
    private static final String YEARS = "y";

    public void validateReportingBean(ReportingFilterBean filterBean) {
        if (filterBean.getStart() == null || filterBean.getEnd() == null || StringUtils.isBlank(filterBean.getInterval())) {
            throw new ApiValidationException("start and enddate must not be null");
        }
        String[] splitted = filterBean.getInterval().split("");
        long intervalInSeconds = 1;
        long amount = Long.parseLong(splitted[0]);
        String interval = splitted[1];
        switch (interval) {
            case SECONDS:
                intervalInSeconds = 1L;
                break;
            case MINUTES:
                intervalInSeconds = 60L;
                break;
            case HOURS:
                intervalInSeconds = 3600L;
                break;
            case DAYS:
                intervalInSeconds = 86400L;
                break;
            case WEEKS:
                intervalInSeconds = 604800L;
                break;
            case MONTHS:
                intervalInSeconds = 2592000L;
                break;
            case QUARTERS:
                intervalInSeconds = 7776000L;
                break;
            case YEARS:
                intervalInSeconds = 31556952L;
                break;
        }

        BigDecimal endTime = BigDecimal.valueOf(filterBean.getEnd().getTime());
        BigDecimal startTime = BigDecimal.valueOf(filterBean.getStart().getTime());

        BigDecimal difference = endTime.subtract(startTime);
        difference = difference.divide(BigDecimal.valueOf(THOUSAND), RoundingMode.HALF_UP);

        BigDecimal multipliedIntervalInSeconds = BigDecimal.valueOf(intervalInSeconds).multiply(BigDecimal.valueOf(amount));

        BigDecimal bucketEntries = difference.divide(multipliedIntervalInSeconds, RoundingMode.HALF_UP);
        if (bucketEntries.compareTo(BigDecimal.valueOf(MAX_BUCKETS)) > 0) {
            throw new ApiValidationException("too many data points (got " + bucketEntries.toString() + " max " + MAX_BUCKETS + " allowed");
        }
    }
}
