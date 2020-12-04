package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.AppointmentState;
import de.immomio.model.repository.core.shared.appointment.BaseAppointmentRepository;
import de.immomio.service.appointment.AppointmentSendMailService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Component
public class PostViewingEmailTask extends BaseTask {

    private BaseAppointmentRepository appointmentRepository;

    private AppointmentSendMailService appointmentSendMailService;

    @Autowired
    public PostViewingEmailTask(
            BaseAppointmentRepository appointmentRepository,
            AppointmentSendMailService appointmentSendMailService
    ) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentSendMailService = appointmentSendMailService;
    }

    @Override
    public boolean run() {
        DateTime to = new DateTime();
        DateTime from = to.minusHours(24);

        List<Appointment> appointments = appointmentRepository
                .findAllByDateBetweenAndState(from.toDate(), to.toDate(), AppointmentState.ACTIVE);
        appointments.forEach(appointmentSendMailService::sendIntentMails);

        return true;
    }
}
