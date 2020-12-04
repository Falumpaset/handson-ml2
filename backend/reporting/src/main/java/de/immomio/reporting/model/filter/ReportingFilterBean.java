package de.immomio.reporting.model.filter;

import de.immomio.data.landlord.bean.landlordfilter.BaseAgentFilterBean;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class ReportingFilterBean implements BaseAgentFilterBean {

    private static final long serialVersionUID = 2570859526522612034L;

    @NotNull
    private Date start;
    @NotNull
    private Date end;
    @NotNull
    private String interval;
    @NotNull
    private List<Long> agents = new ArrayList<>();
    @NotNull
    private List<String> cities = new ArrayList<>();
    @NotNull
    private List<String> zipCodes = new ArrayList<>();
    @NotNull
    private List<Long> properties = new ArrayList<>();
}
