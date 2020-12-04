package de.immomio.beans.landlord.schufa;

import de.immomio.controller.paging.CustomPageable;
import de.immomio.data.base.bean.schufa.cbi.enums.CbiActionType;
import de.immomio.data.base.type.schufa.JobState;
import de.immomio.data.landlord.bean.landlordfilter.BaseAgentFilterBean;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SchufaReportSearchBean extends CustomPageable implements BaseAgentFilterBean {

    private static final long serialVersionUID = -7743630764498395527L;

    private Long userId;

    private List<Long> agents = new ArrayList<>();

    private List<JobState> states = new ArrayList<>();

    private List<CbiActionType> types = new ArrayList<>();

}
