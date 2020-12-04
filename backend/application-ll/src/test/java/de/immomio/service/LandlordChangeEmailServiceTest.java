package de.immomio.service;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserBean;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.landlord.entity.user.changeemail.ChangeEmail;
import de.immomio.landlord.service.user.LandlordChangeEmailService;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import utils.TestHelper;

import java.util.HashMap;
import java.util.Map;

import static de.immomio.landlord.service.user.LandlordChangeEmailService.EMAIL_CHANGED_SUBJECT;
import static de.immomio.landlord.service.user.LandlordChangeEmailService.EMAIL_CHANGE_SUBJECT;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LandlordChangeEmailServiceTest extends AbstractTest {

    @Mock
    private LandlordMailSender landlordMailSender;

    @InjectMocks
    private LandlordChangeEmailService landlordChangeEmailService;

    private LandlordUser landlordUser;

    @Before
    public void createLandlordUser() {
        LandlordCustomer landlordCustomer = TestHelper.generateLandlordCustomer(1L);
        landlordUser = TestHelper.generateLandlordUser(landlordCustomer, LandlordUsertype.COMPANYADMIN, 1L);
    }

    @Test
    public void sendChangeEmailNotification() {
        ChangeEmail changeEmail = TestHelper.generateLandlordChangeEmail(landlordUser);

        Map<String, Object> data = new HashMap<>();
        data.put(ModelParams.MODEL_USER, new LandlordUserBean(landlordUser));
        data.put(ModelParams.MODEL_NEW_EMAIL, changeEmail.getEmail());
        data.put(ModelParams.MODEL_TOKEN, changeEmail.getToken());
        data.put(ModelParams.MODEL_IGNORE_DISCLAIMER, true);

        landlordChangeEmailService.sendChangeEmailNotification(landlordUser, changeEmail);

        verify(landlordMailSender, times(1))
                .send(eq(landlordUser.getEmail()),
                        eq(landlordUser),
                        eq(MailTemplate.NEW_EMAIL),
                        eq(EMAIL_CHANGE_SUBJECT),
                        eq(data)
                );
    }

    @Test
    public void sendEmailChangedNotification() {
        Map<String, Object> data = new HashMap<>();
        data.put(ModelParams.MODEL_USER, new LandlordUserBean(landlordUser));

        String oldEmail = TestHelper.generateEmail();
        landlordChangeEmailService.sendEmailChangedNotification(landlordUser, oldEmail);

        verify(landlordMailSender, times(1))
                .send(eq(oldEmail),
                        eq(landlordUser),
                        eq(MailTemplate.NEW_EMAIL_CHANGED),
                        eq(EMAIL_CHANGED_SUBJECT),
                        eq(data)
                );
    }

}
