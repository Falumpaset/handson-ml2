package de.immomio.service.property.notification;

import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.property.notification.PropertyApplicationNotificationService;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.service.AbstractTest;
import de.immomio.service.shared.EmailModelProvider;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import utils.TestHelper;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Niklas Lindemann
 */

public class PropertyApplicationNotificationServiceTest extends AbstractTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private EmailModelProvider emailModelProvider;

    @InjectMocks
    private PropertyApplicationNotificationService notificationService;

    @Test
    public void applicationAcceptedDefault() throws Exception {
        PropertyApplication application = TestHelper.generateApplication(ApplicationStatus.ACCEPTED);

        notificationService.applicationAccepted(application, MailTemplate.APPLICATION_ACCEPTED_V1, null);

        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(PropertySearcherProfileMailBrokerContainer.class));
    }

    @Test
    public void applicationRejected() {
        PropertyApplication application = TestHelper.generateApplication(ApplicationStatus.ACCEPTED);

        notificationService.sendRejectedApplicationNotification(application);

        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(PropertySearcherProfileMailBrokerContainer.class));
    }

}