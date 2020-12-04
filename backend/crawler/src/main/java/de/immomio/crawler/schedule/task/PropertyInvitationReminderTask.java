package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.AppointmentBean;
import de.immomio.data.shared.entity.appointment.AppointmentState;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.mail.ModelParams;
import de.immomio.mail.configurator.PropertySearcherMailConfigurator;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.core.shared.appointment.BaseAppointmentRepository;
import de.immomio.service.shared.EmailModelProvider;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister.
 */

@Slf4j
@Component
public class PropertyInvitationReminderTask extends BaseTask {

    private static final String INVITATION_SUMMARY_SUBJECT = "invitation.summary.subject";

    private static final String INVITATION_REMINDER_SUBJECT = "invitation.reminder.subject";

    private final BaseAppointmentRepository appointmentRepository;

    private final LandlordMailSender landlordMailSender;

    private final PropertySearcherMailSender propertySearcherMailSender;

    private final EmailModelProvider emailModelProvider;

    private final PropertySearcherMailConfigurator mailConfigurator;

    @Autowired
    public PropertyInvitationReminderTask(
            BaseAppointmentRepository appointmentRepository,
            LandlordMailSender landlordMailSender,
            PropertySearcherMailSender propertySearcherMailSender,
            EmailModelProvider emailModelProvider,
            PropertySearcherMailConfigurator mailConfigurator
    ) {
        this.appointmentRepository = appointmentRepository;
        this.landlordMailSender = landlordMailSender;
        this.propertySearcherMailSender = propertySearcherMailSender;
        this.emailModelProvider = emailModelProvider;
        this.mailConfigurator = mailConfigurator;
    }

    @Override
    public boolean run() {
        invitations();

        return true;
    }

    private void invitations() {
        DateTime from = new DateTime();
        DateTime to = from.plusHours(24);

        List<Appointment> appointments = appointmentRepository.findAllByDateBetweenAndState(from.toDate(),
                to.toDate(), AppointmentState.ACTIVE);

        for (Appointment appointment : appointments) {
            if (appointment.getProperty() != null) {
                notifyCustomer(appointment);
                notifyTenant(appointment);
            }
        }
    }

    private void notifyCustomer(Appointment appointment) {
        Property property = appointment.getProperty();
        LandlordUser user = getPropertyUser(property);

        if (user == null) {
            log.error("Can't find user for invitation [" + appointment.getId() + "] & flat [" + property.getId() + "]");
            return;
        }

        Map<String, Object> model = getLandlordInvitationReminderModel(appointment, user);
        landlordMailSender.send(user, MailTemplate.INVITATION_SUMMARY, INVITATION_SUMMARY_SUBJECT, model);
    }

    private void notifyTenant(Appointment appointment) {
        appointment.getAppointmentAcceptances().stream()
                .filter(appointmentAcceptance -> appointmentAcceptance.getState() == AppointmentAcceptanceState.ACTIVE)
                .forEach(appointmentAcceptance -> {
                    PropertySearcherUserProfile userProfile = appointmentAcceptance.getApplication().getUserProfile();

                    if (userProfile != null) {
                        Map<String, Object> model = getPropertySearcherInvitationReminderModel(appointment, userProfile);

                        propertySearcherMailSender.send(userProfile, MailTemplate.INVITATION_REMINDER,
                                INVITATION_REMINDER_SUBJECT, model, appointment.getProperty().getCustomer().getId());
                    }
                });
    }

    private Map<String, Object> getPropertySearcherInvitationReminderModel(Appointment appointment,
                                                                           PropertySearcherUserProfile userProfile) {
        Map<String, Object> model = emailModelProvider.createUserProfilePropertyModel(userProfile, appointment.getProperty());

        model.put(ModelParams.MODEL_APPOINTMENT, new AppointmentBean(appointment));
        model.put(ModelParams.SELF_DISCLOSURE_ALLOWED, appointment.getProperty().isAllowSelfDisclosure());
        model.put(ModelParams.RETURN_URL, mailConfigurator.buildAppUrl());
        emailModelProvider.appendFormattedDate(appointment.getDate(), model, ModelParams.MODEL_APPOINTMENT_DATE_FORMATTED);

        return model;
    }

    private Map<String, Object> getLandlordInvitationReminderModel(Appointment appointment, LandlordUser user) {
        Map<String, Object> model = emailModelProvider.createPropertyModel(appointment.getProperty());

        model.put(ModelParams.MODEL_USER, new LandlordUserBean(user));
        model.put(ModelParams.MODEL_APPOINTMENT, new AppointmentBean(appointment));
        model.put(ModelParams.MODEL_IGNORE_DISCLAIMER, true);

        emailModelProvider.appendFormattedDate(appointment.getDate(), model, ModelParams.MODEL_APPOINTMENT_DATE_FORMATTED);

        return model;
    }
}
