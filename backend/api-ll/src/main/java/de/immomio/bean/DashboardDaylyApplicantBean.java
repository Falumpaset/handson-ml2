package de.immomio.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DashboardDaylyApplicantBean implements Serializable {

    private static final long serialVersionUID = 3641634243707278957L;

    private String day;

    private Long count;

    public DashboardDaylyApplicantBean() {
    }

    public DashboardDaylyApplicantBean(String day, Long count) {
        this.day = day;
        this.count = count;
    }
}
