package de.immomio.service.appointment;

import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.mail.LandlordMailBrokerContainer;
import de.immomio.service.application.PropertyApplicationNotificationService;
import de.immomio.service.shared.EmailModelProvider;
import de.immomio.utils.TestHelper;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsMapContaining;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Freddy Sawma
 */

@ExtendWith(MockitoExtension.class)
public class AppointmentNotificationServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private PropertySearcherMailSender mailSender;

    @Mock
    private EmailModelProvider emailModelProvider;

    @Spy
    @InjectMocks
    private PropertyApplicationNotificationService applicationNotificationService;

    @InjectMocks
    private AppointmentNotificationService appointmentNotificationService;

    @Test
    public void applicationConfirmed() {
        PropertySearcherUserProfile userProfile = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer()).getMainProfile();
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer());

        doAnswer(invocation -> {
            Map<String, Object> map = (HashMap<String, Object>) invocation.getArguments()[3];
            Assertions.assertEquals(map.size(), 6);
            Assertions.assertTrue(map.containsKey(ModelParams.MODEL_USER));
            Assertions.assertTrue(map.containsKey(ModelParams.MODEL_USER_PROFILE));
            Assertions.assertTrue(map.containsKey(ModelParams.MODEL_FLAT));
            return null;
        }).when(mailSender).send(
                eq(userProfile),
                any(MailTemplate.class),
                anyString(),
                anyMap(),
                nullable(Long.class));

        applicationNotificationService.applicationConfirmed(userProfile, property);

        verify(mailSender, times(1)).send(eq(userProfile),
                any(MailTemplate.class),
                anyString(),
                anyMap(),
                nullable(Long.class));
    }

    @Test
    public void applicationAccepted() {
        PropertySearcherUserProfile userProfile = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer()).getMainProfile();
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer());

        doAnswer(invocation-> {
            Map<String, Object> map = (HashMap<String, Object>) invocation.getArguments()[3];
            Assertions.assertEquals(map.size(),6);
            MatcherAssert.assertThat(map, IsMapContaining.hasKey(ModelParams.MODEL_USER));
            Assertions.assertTrue(map.containsKey(ModelParams.MODEL_USER_PROFILE));
            Assertions.assertTrue(map.containsKey(ModelParams.MODEL_FLAT));
            return null;
        }).when(mailSender).send(
                eq(userProfile),
                any(MailTemplate.class),
                anyString(),
                anyMap());

        applicationNotificationService.applicationAccepted(userProfile, property);

        verify(mailSender, times(1)).send(eq(userProfile),
                any(MailTemplate.class),
                anyString(),
                anyMap());
    }

    @Test
    public void appointmentAcceptedWhenAppointmentAcceptancesIsNotEmpty() throws Exception {
        Appointment appointment = TestHelper.generateAppointment();
        PropertySearcherUserProfile userProfile = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer()).getMainProfile();
        PropertyApplication application = TestHelper.generateApplication(ApplicationStatus.ACCEPTED);
        AppointmentAcceptance appointmentAcceptance =
                TestHelper.generateAppointmentAcceptance(appointment, application, AppointmentAcceptanceState.ACTIVE);
        appointmentAcceptance.setApplication(application);
        application.setUserProfile(userProfile);
        List<AppointmentAcceptance> acceptances = Arrays.asList(appointmentAcceptance);
        appointment.setAppointmentAcceptances(acceptances);


        appointmentNotificationService.appointmentAccepted(appointmentAcceptance);

        verify(mailSender, times(1)).send(
                eq(userProfile),
                any(MailTemplate.class),
                anyString(),
                any(String[].class),
                anyMap(),
                nullable(Long.class),
                anyMap());

        verify(rabbitTemplate, never())
                .setMessageConverter(any(Jackson2JsonMessageConverter.class));
        verify(rabbitTemplate, never())
                .convertAndSend(anyString(), anyString(), any(LandlordMailBrokerContainer.class));
    }

    @Test
    public void appointmentAcceptedWhenAppointmentAcceptancesIsEmpty() throws Exception {
        Appointment appointment = TestHelper.generateAppointment();
        PropertySearcherUser user = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer());
        PropertyApplication application = TestHelper.generateApplication(ApplicationStatus.ACCEPTED);
        AppointmentAcceptance appointmentAcceptance =
                TestHelper.generateAppointmentAcceptance(appointment, application, AppointmentAcceptanceState.ACTIVE);
        appointmentAcceptance.setApplication(application);
        application.setUserProfile(user.getMainProfile());


        appointmentNotificationService.appointmentAccepted(appointmentAcceptance);

        verify(mailSender, times(1)).send(
                eq(user.getMainProfile()),
                any(MailTemplate.class),
                anyString(),
                any(String[].class),
                anyMap(),
                nullable(Long.class),
                anyMap());

        verify(rabbitTemplate, times(1))
                .setMessageConverter(any(Jackson2JsonMessageConverter.class));
        verify(rabbitTemplate, times(1))
                .convertAndSend(anyString(), anyString(), any(LandlordMailBrokerContainer.class));
    }

    @Test
    public void appointmentDeclined() throws Exception {
        Appointment appointment = TestHelper.generateAppointment();
        PropertySearcherUser user = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer());
        PropertyApplication application = TestHelper.generateApplication(ApplicationStatus.ACCEPTED);
        AppointmentAcceptance appointmentAcceptance =
                TestHelper.generateAppointmentAcceptance(appointment, application, AppointmentAcceptanceState.ACTIVE);
        appointmentAcceptance.setApplication(application);
        application.setUserProfile(user.getMainProfile());

        appointmentNotificationService.appointmentCanceled(appointmentAcceptance);

        verify(rabbitTemplate, times(1))
                .setMessageConverter(any(Jackson2JsonMessageConverter.class));
        verify(rabbitTemplate, times(1))
                .convertAndSend(anyString(), anyString(), any(LandlordMailBrokerContainer.class));
    }
}