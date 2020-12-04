package de.immomio.reporting.model.chart;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class ReportingChartData implements Serializable {

    private static final long serialVersionUID = 1705618528854215937L;
    private String title;
    private List<ReportingChartPoint> data = new ArrayList<>();
}
