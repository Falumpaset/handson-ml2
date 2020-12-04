package de.immomio.service.appointment;

import de.immomio.common.ical.ICalEventHandler;
import de.immomio.data.landlord.bean.property.PropertyMailBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.landlord.service.appointment.AppointmentService;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.service.AbstractTest;
import de.immomio.service.report.PropertyReportService;
import de.immomio.service.shared.EmailModelProvider;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import utils.TestHelper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Niklas Lindemann
 */

public class AppointmentServiceTest extends AbstractTest {

    @Mock
    private LandlordMailSender landlordMailSender;

    @Mock
    private PropertyReportService propertyReportService;

    @Mock
    private EmailModelProvider emailModelProvider;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    public void inviteToViewings() throws Exception {
        appointmentService = spy(appointmentService);

        Appointment appointment = TestHelper.generateAppointment();

        doAnswer(invocation -> {
            Map<String, Object> providedData = (HashMap<String, Object>) invocation.getArguments()[4];
            Map<String, Object> providedAttachments = (HashMap<String, Object>) invocation.getArguments()[5];
            assertThat(providedData.size(), is(2));

            PropertyMailBean propertyMailBean = new PropertyMailBean(appointment.getProperty());
            assertThat(providedData, IsMapContaining.hasEntry(ModelParams.MODEL_FLAT, propertyMailBean));
            assertThat(providedAttachments.size(), is(1));
            assertThat(providedAttachments, IsMapContaining.hasKey(ICalEventHandler.INVITE_ICS_FILE_NAME));
            return null;
        }).when(landlordMailSender).send(anyString(),
                any(LandlordCustomer.class),
                Mockito.eq(MailTemplate.INVITED_TO_VIEWING),
                anyString(),
                anyMap(),
                anyMap());

        appointmentService.inviteToViewings(appointment, Collections.singletonList("mail@test.de"));

        verify(landlordMailSender, times(1)).send(
                anyString(),
                any(LandlordCustomer.class),
                Mockito.eq(MailTemplate.INVITED_TO_VIEWING),
                anyString(),
                anyMap(),
                anyMap());
    }
}
