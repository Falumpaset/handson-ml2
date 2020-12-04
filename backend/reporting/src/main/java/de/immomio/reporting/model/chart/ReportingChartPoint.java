package de.immomio.reporting.model.chart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportingChartPoint implements Serializable {
    private static final long serialVersionUID = -3618895451491234731L;

    private Date date = null;
    private Map<String, Number> values = new LinkedHashMap<>();
}
