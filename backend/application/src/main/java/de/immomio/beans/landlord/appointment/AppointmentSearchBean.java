package de.immomio.beans.landlord.appointment;

import de.immomio.controller.paging.CustomPageable;
import de.immomio.data.landlord.bean.landlordfilter.BaseAgentFilterBean;
import de.immomio.data.shared.bean.common.DateRange;
import de.immomio.data.shared.entity.appointment.AppointmentState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSearchBean extends CustomPageable implements BaseAgentFilterBean {
    private static final long serialVersionUID = -6354859676221756718L;

    private Long propertyId;
    private List<Long> agents = new ArrayList<>();
    private AppointmentState state;
    private DateRange range;
}
