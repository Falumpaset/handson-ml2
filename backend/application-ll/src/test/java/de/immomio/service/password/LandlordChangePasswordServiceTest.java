package de.immomio.service.password;

import de.immomio.beans.ChangePasswordBean;
import de.immomio.constants.exceptions.UserNotFoundException;
import de.immomio.data.landlord.bean.customer.LandlordCustomerBean;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserBean;
import de.immomio.data.landlord.entity.user.changepassword.ChangePassword;
import de.immomio.landlord.service.user.LandlordChangePasswordService;
import de.immomio.mail.configurator.LandlordMailConfigurator;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import de.immomio.model.repository.landlord.customer.user.changepassword.LandlordChangePasswordRepository;
import de.immomio.security.service.KeycloakService;
import de.immomio.service.AbstractTest;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.validation.BindingResult;
import utils.TestHelper;

import java.util.HashMap;
import java.util.Map;

import static de.immomio.data.landlord.entity.user.LandlordUsertype.EMPLOYEE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Freddy Sawma
 */

public class LandlordChangePasswordServiceTest extends AbstractTest {

    @Mock
    private LandlordMailSender mailSender;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private LandlordUserRepository userRepository;

    @Mock
    private LandlordMailConfigurator mailConfigurator;

    @Mock
    private LandlordChangePasswordRepository changePasswordRepository;

    @InjectMocks
    @Spy
    private LandlordChangePasswordService landlordChangePasswordService;

    private LandlordUser landlordUser;

    @Before
    public void createLandlordUser() {
        landlordUser = TestHelper.generateLandlordUser(EMPLOYEE, 1L);
    }

    @Test(expected = UserNotFoundException.class)
    public void resetPasswordUserIsNull() throws UserNotFoundException {
        LandlordUser user = userRepository.findByEmail(null);
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        landlordChangePasswordService.resetPassword(anyString());
    }

    @Test
    public void resetPassword() {
        when(changePasswordRepository.customSave(any(ChangePassword.class))).thenAnswer(
                invocation -> invocation.getArguments()[0]);

        LandlordUserBean userBean = new LandlordUserBean(landlordUser);

        doAnswer(invocation ->  {
            Map<String, Object> provided = (HashMap<String, Object>) invocation.getArguments()[3];
            assertThat(provided.size(), is(3));
            assertThat(provided, IsMapContaining.hasEntry(ModelParams.MODEL_USER, userBean));
            assertThat(provided, IsMapContaining.hasKey(ModelParams.MODEL_TOKEN));
            assertThat(provided, IsMapContaining.hasEntry(ModelParams.RETURN_URL, mailConfigurator.buildAppUrl()));
            return null;
        }).when(mailSender).send(any(LandlordUser.class), any(MailTemplate.class), anyString(), anyMap());

        landlordChangePasswordService.resetPassword(landlordUser);
        verify(mailSender, times(1)).send(
                any(LandlordUser.class), eq(MailTemplate.RESET_PASSWORD), anyString(), anyMap());
    }

    @Test
    public void newUserWhenUserIsNull() {
        LandlordUser user = userRepository.findByEmail(null);
        assertNull("user should be null", user);
    }

    @Test
    public void newUser() {
        when(changePasswordRepository.customSave(any(ChangePassword.class))).thenAnswer(
                invocation -> invocation.getArguments()[0]);

        Map<String, Object> expected = new HashMap<>();
        LandlordUserBean userBean = new LandlordUserBean(landlordUser);
        LandlordCustomerBean customerBean = new LandlordCustomerBean(
                landlordUser.getCustomer(), landlordUser.fullName());

        expected.put(ModelParams.MODEL_USER, userBean);
        expected.put(ModelParams.MODEL_CUSTOMER, customerBean);
        expected.put(ModelParams.MODEL_IGNORE_DISCLAIMER, false);

        doAnswer(invocation ->  {
            Map<String, Object> provided = (HashMap<String, Object>) invocation.getArguments()[3];
            assertThat(provided.size(), is(4));
            assertThat(provided, IsMapContaining.hasEntry(ModelParams.MODEL_USER, userBean));
            assertThat(provided, IsMapContaining.hasKey(ModelParams.MODEL_CUSTOMER));
            assertThat(provided, IsMapContaining.hasKey(ModelParams.MODEL_IGNORE_DISCLAIMER));
            assertThat(provided, IsMapContaining.hasKey(ModelParams.MODEL_TOKEN));
            return invocation.getArguments()[3];
        }).when(mailSender).send(
                anyString(), eq(MailTemplate.COMMERCIAL_NEW_AGENT), anyString(), anyMap());

        landlordChangePasswordService.newUser(landlordUser);
        verify(mailSender, times(1)).send(
                anyString(), eq(landlordUser), eq(landlordUser.getCustomer()), eq(MailTemplate.COMMERCIAL_NEW_AGENT), anyString(), anyMap());
    }

    @Test
    public void changePassword() {   //Problems approaching this test

    }

    @Test
    public void changePasswordResultNull() {
        assertFalse(landlordChangePasswordService.changePassword(
                any(LandlordUser.class), any(ChangePasswordBean.class), any(BindingResult.class)));
    }
}