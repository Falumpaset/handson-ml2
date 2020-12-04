package de.immomio.propertysearcher.bean;

import de.immomio.data.shared.entity.appointment.Appointment;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class AppointmentBundleBean implements Serializable , Comparable {

    private static final long serialVersionUID = -7384395214022539390L;

    private Long applicationId;

    private PropertyBean property;

    private List<Appointment> appointments = new ArrayList<>();

    private List<AppointmentAcceptanceBean> appointmentAcceptances = new ArrayList<>();

    private List<AppointmentInvitationBean> appointmentInvitations = new ArrayList<>();

    @Override
    public int compareTo(Object bundleBean) {
        AppointmentBundleBean anotherBundleBean = (AppointmentBundleBean) bundleBean;
        Optional<Appointment> appointmentToCompare = this.getAppointments().stream().min(Comparator.comparing(Appointment::getDate));
        Optional<Appointment> appointment = anotherBundleBean.getAppointments().stream().min(Comparator.comparing(Appointment::getDate));

        if (appointmentToCompare.isPresent() && appointment.isEmpty()) {
            return 1;
        } else if (appointment.isPresent() && appointmentToCompare.isEmpty()) {
            return -1;
        } else if (appointmentToCompare.isEmpty()) {
            return 0;
        }

        return appointmentToCompare.get().getDate().compareTo(appointment.get().getDate());
    }
}