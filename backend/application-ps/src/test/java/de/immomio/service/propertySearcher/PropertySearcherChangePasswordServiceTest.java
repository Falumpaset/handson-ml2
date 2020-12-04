package de.immomio.service.propertySearcher;

import de.immomio.beans.ChangePasswordBean;
import de.immomio.beans.ResetPasswordConfirmBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.user.PropertySearcherUserType;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherChangePassword;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.mail.configurator.PropertySearcherMailConfigurator;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserRepository;
import de.immomio.model.repository.propertysearcher.user.change.password.PropertySearcherChangePasswordRepository;
import de.immomio.security.service.KeycloakService;
import de.immomio.service.propertySearcher.change.PropertySearcherChangePasswordService;
import de.immomio.service.security.UserSecurityService;
import de.immomio.utils.TestHelper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Freddy Sawma
 */

@ExtendWith(MockitoExtension.class)
public class PropertySearcherChangePasswordServiceTest {

    @Mock
    private PropertySearcherMailSender mailSender;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private PropertySearcherUserRepository userRepository;

    @Mock
    private PropertySearcherChangePasswordRepository changePasswordRepository;

    @Mock
    private UserSecurityService userSecurityService;

    @Mock
    private PropertySearcherMailConfigurator mailConfigurator;

    @InjectMocks
    public PropertySearcherChangePasswordService changePasswordService;

    @Test
    public void resetPasswordWhenEmailIsNullOrEmpty() {
        Assertions.assertNull(changePasswordService.resetPassword((String) null));
    }

    @Test
    public void resetPassword() {
        PropertySearcherUserProfile userProfile = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer()).getMainProfile();
        userProfile.getUser().setType(PropertySearcherUserType.REGISTERED);
        userProfile.setAddress(TestHelper.generateAddress());
        when(userRepository.findByEmail(anyString())).thenReturn(userProfile.getUser());

        when(changePasswordRepository.customSave(
                any(PropertySearcherChangePassword.class))).thenReturn(new PropertySearcherChangePassword());

        changePasswordService.resetPassword(TestHelper.generateEmail());

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(changePasswordRepository, times(1)).customSave(any());
        verify(mailSender, times(1)).send(any(PropertySearcherUserProfile.class),
                eq(MailTemplate.RESET_PASSWORD), anyString(), anyMap());
    }

    @Test
    public void resetPasswordWhenUserIsNull() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        Assertions.assertThrows(ApiValidationException.class, () -> changePasswordService.resetPassword(TestHelper.generateEmail()));
    }

    @Test
    public void changePasswordWhenUserIsNull() {
        ChangePasswordBean bean = new ChangePasswordBean();
        Assertions.assertThrows(ApiValidationException.class, () -> changePasswordService.changePassword(bean));
    }

    @Test
    public void changePasswordWhenChangePasswordBeanIsNull() {
        Assertions.assertThrows(ApiValidationException.class, () -> changePasswordService.changePassword(null));
    }

    @Test
    public void changePasswordWhenPasswordNotConfirmed() {
        ChangePasswordBean bean = new ChangePasswordBean();
        bean.setPassword(null);

        Assertions.assertThrows(ApiValidationException.class, () -> changePasswordService.changePassword(bean));
    }

    @Test
    public void changePasswordWhenChangePasswordNull() {
        PropertySearcherUser user = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer());
        ChangePasswordBean bean = new ChangePasswordBean();
        bean.setPassword("test");
        bean.setConfirmedPassword("test");

        when(userSecurityService.getPrincipalUser()).thenAnswer(invocationOnMock -> user);

        Assertions.assertTrue(changePasswordService.changePassword(bean));

        verify(keycloakService, times(1)).resetPassword(anyString(), anyString(), anyBoolean());
        verify(mailSender, times(1))
                .send(any(PropertySearcherUserProfile.class),
                        eq(MailTemplate.CHANGED_PASSWORD),
                        anyString(),
                        anyMap());
    }

    @Test
    public void changePasswordWhenResetPasswordConfirmBeanIsNull() {
        ResetPasswordConfirmBean bean = null;
        Assertions.assertThrows(ApiValidationException.class, () -> changePasswordService.changePassword(bean));
    }

    @Test
    public void changePasswordWhenPasswordNotEqualsConfirmedPassword() {
        ResetPasswordConfirmBean bean = new ResetPasswordConfirmBean();
        bean.setPassword("test1");
        bean.setConfirmedPassword("test2");
        Assertions.assertThrows(ApiValidationException.class, () -> changePasswordService.resetPassword(bean));
    }

}