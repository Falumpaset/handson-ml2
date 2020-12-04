package de.immomio.service.propertySearcher;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherChangeEmail;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.propertysearcher.user.change.email.PropertySearcherChangeEmailRepository;
import de.immomio.service.propertySearcher.change.PropertySearcherChangeEmailService;
import de.immomio.utils.TestHelper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static de.immomio.service.propertySearcher.change.PropertySearcherChangeEmailService.EMAIL_CHANGED_SUBJECT_KEY;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Freddy Sawma
 */

@ExtendWith(MockitoExtension.class)
public class PropertySearcherChangeEmailServiceTest {

    @Mock
    private PropertySearcherMailSender propertySearcherMailSender;

    @Mock
    private PropertySearcherChangeEmailRepository changeEmailRepository;

    @Spy
    @InjectMocks
    private PropertySearcherChangeEmailService propertySearcherChangeEmailService;

    @Test
    public void sendEmailChangedNotification() {
        PropertySearcherUser propertySearcherUser = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer());
        String oldEmail = TestHelper.generateEmail();
        propertySearcherChangeEmailService.sendEmailChangedNotification(propertySearcherUser.getMainProfile(), oldEmail);

        verify(propertySearcherMailSender, times(1))
                .send(eq(oldEmail),
                        eq(propertySearcherUser.getMainProfile()),
                        eq(MailTemplate.NEW_EMAIL_CHANGED),
                        eq(EMAIL_CHANGED_SUBJECT_KEY),
                        anyMap()
                );
    }

    @Test
    public void sendEmailChangeNotificationWithChangeEmail() {
        PropertySearcherChangeEmail changeEmail = new PropertySearcherChangeEmail();
        PropertySearcherUser user = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer());
        user.getMainProfile().getData().setFirstname("firstName");
        user.getMainProfile().getData().setName("lastName");
        TestHelper.setId(user, 999L);
        TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer());

        changeEmail.setToken("123test123");
        changeEmail.setEmail("test@immomio.de");
        propertySearcherChangeEmailService.sendEmailChangeNotification(changeEmail, user.getMainProfile());

        verify(propertySearcherMailSender, times(1))
                .send(anyString(), any(PropertySearcherUserProfile.class), eq(MailTemplate.NEW_EMAIL), anyString(), anyMap());
    }

}
