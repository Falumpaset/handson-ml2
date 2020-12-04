package de.immomio.service.appointment;

import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.landlord.service.appointment.AppointmentNotificationService;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.service.AbstractTest;
import de.immomio.service.shared.EmailModelProvider;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import utils.TestHelper;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Niklas Lindemann
 */

public class AppointmentNotificationServiceTest extends AbstractTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private EmailModelProvider emailModelProvider;

    @InjectMocks
    private AppointmentNotificationService notificationService;

    @Test
    public void appointmentCreated() throws Exception {
        LandlordCustomer landlordCustomer = TestHelper.generateLandlordCustomerWithAddon(AddonType.AGENT, 1L);
        Appointment appointment = TestHelper.generateAppointment();
        Property property = getTestProperty(landlordCustomer);
        appointment.setProperty(property);

        notificationService.publicAppointmentCreated(appointment);

        verify(rabbitTemplate, times(3)).convertAndSend(anyString(), anyString(), any(PropertySearcherProfileMailBrokerContainer.class));
    }

    @Test
    public void appointmentCanceled() throws Exception {
        LandlordCustomer landlordCustomer = TestHelper.generateLandlordCustomerWithAddon(AddonType.AGENT, 1L);
        Appointment appointment = TestHelper.generateAppointment();
        Property property = getTestProperty(landlordCustomer);
        appointment.setProperty(property);
        List<AppointmentAcceptance> acceptances = property.getPropertyApplications().stream()
                .map(application -> TestHelper.generateAppointmentAcceptance(appointment, application, AppointmentAcceptanceState.ACTIVE))
                .collect(Collectors.toList());

        notificationService.appointmentCanceled(acceptances);

        verify(rabbitTemplate, times(5)).convertAndSend(anyString(), anyString(), any(PropertySearcherProfileMailBrokerContainer.class));
    }

    private Property getTestProperty(LandlordCustomer landlordCustomer) {
        Property property = TestHelper.generateProperty(landlordCustomer, 1L);
        property.getPropertyApplications().addAll(TestHelper.generateApplications(3, ApplicationStatus.ACCEPTED));
        property.getPropertyApplications().addAll(TestHelper.generateApplications(2, ApplicationStatus.REJECTED));
        return property;
    }


}