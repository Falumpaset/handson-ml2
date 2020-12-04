package de.immomio.reporting.model.beans;

import de.immomio.data.landlord.bean.property.Contact;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.AppointmentState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@NoArgsConstructor
public class ReportingAppointmentBean implements Serializable {

    private static final long serialVersionUID = -5950996055789920462L;

    private Long id;

    private Date date;

    private AppointmentState state;

    private Integer maxInviteeCount;

    private Boolean showContactInformation;

    private Contact contact;

    private Date created;

    private String specialInstructions;

    private ReportingPropertyBean property;

    private boolean exclusive;

    public ReportingAppointmentBean(Appointment appointment) {
        this.id = appointment.getId();
        this.date = appointment.getDate();
        this.state = appointment.getState();
        this.maxInviteeCount = appointment.getMaxInviteeCount();
        this.showContactInformation = appointment.getShowContactInformation();
        this.contact = appointment.getContact();
        this.created = appointment.getCreated();
        this.specialInstructions = appointment.getSpecialInstructions();
        this.property = new ReportingPropertyBean(appointment.getProperty());
        this.exclusive = appointment.isExclusive();
    }
}
