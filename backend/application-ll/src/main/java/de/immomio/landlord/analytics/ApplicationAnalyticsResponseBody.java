package de.immomio.landlord.analytics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Maik Kingma
 */

@Getter
@Setter
@NoArgsConstructor
public class ApplicationAnalyticsResponseBody implements Serializable {

    private static final long serialVersionUID = 348988582812718280L;

    private Map<Date, OneDayApplicationStatistics> applicationStatisticsMap = new TreeMap<>();

    public void addElementToMap(OneDayApplicationStatistics daysStats, Date day) {
        applicationStatisticsMap.put(day, daysStats);
    }
}
