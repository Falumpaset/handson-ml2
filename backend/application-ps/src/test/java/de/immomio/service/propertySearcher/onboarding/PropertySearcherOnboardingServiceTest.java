package de.immomio.service.propertySearcher.onboarding;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.BadRequestException;
import de.immomio.data.base.type.user.PropertySearcherUserType;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserProfileRepository;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserRepository;
import de.immomio.propertysearcher.bean.PropertySearcherRegisterBean;
import de.immomio.security.common.bean.ImpersonateResponse;
import de.immomio.security.service.KeycloakService;
import de.immomio.service.propertySearcher.onboarding.PropertySearcherOnboardingService;
import de.immomio.utils.TestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Freddy Sawma
 */

@ExtendWith(MockitoExtension.class)
public class PropertySearcherOnboardingServiceTest {

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private PropertySearcherUserRepository userRepository;

    @Mock
    private PropertySearcherUserProfileRepository userProfileRepository;

    @Spy
    @InjectMocks
    private PropertySearcherOnboardingService onboardingService;

    public PropertySearcherRegisterBean getPropertySearcherBean() {
        var propertySearcherRegisterBean = new PropertySearcherRegisterBean();
        propertySearcherRegisterBean.setEmail(TestHelper.generateEmail());
        propertySearcherRegisterBean.setPassword("test");
        propertySearcherRegisterBean.setConfirmPassword("test");
        propertySearcherRegisterBean.setFirstName("vorname");
        propertySearcherRegisterBean.setLastName("nachname");

        return propertySearcherRegisterBean;
    }

    @Test()
    public void registerWhenRegisterBeenIsNull() {
        Assertions.assertThrows(ApiValidationException.class,
                () -> onboardingService.changePropertySearcherStateToRegistered(null));
    }

    @Test()
    public void registerWhenEmailIsNull() {
        PropertySearcherRegisterBean bean = new PropertySearcherRegisterBean();
        bean.setEmail("");
        bean.setPassword("password");

        Assertions.assertThrows(ApiValidationException.class,
                () -> onboardingService.changePropertySearcherStateToRegistered(bean));
    }

    @Test()
    public void registerWhenPasswordIsNull() {
        PropertySearcherRegisterBean bean = new PropertySearcherRegisterBean();
        bean.setEmail(TestHelper.generateEmail());
        bean.setPassword(null);

        Assertions.assertThrows(ApiValidationException.class,
                () -> onboardingService.changePropertySearcherStateToRegistered(bean));
    }

    @Test()
    public void registerWhenPasswordIsEmpty() {
        PropertySearcherRegisterBean bean = new PropertySearcherRegisterBean();
        bean.setEmail(TestHelper.generateEmail());
        bean.setPassword("");

        Assertions.assertThrows(ApiValidationException.class,
                () -> onboardingService.changePropertySearcherStateToRegistered(bean));
    }

    @Test
    public void registerWhenPasswordNotEqualToConfirmedPassword() {
        PropertySearcherRegisterBean bean = new PropertySearcherRegisterBean();
        bean.setEmail(TestHelper.generateEmail());
        bean.setPassword("test123");
        bean.setConfirmPassword("test");
        bean.setFirstName("tester");
        bean.setLastName("test");

        Assertions.assertThrows(ApiValidationException.class,
                () -> onboardingService.changePropertySearcherStateToRegistered(bean));
    }

    @Test
    public void registerWhenUsersSizeIsOne() {
        var userRep = new UserRepresentation();
        userRep.setEmail("test@mail.de");

        PropertySearcherUser user = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer());

        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(userRepository.save(any(PropertySearcherUser.class))).thenAnswer(i -> i.getArgument(0));
        when(userProfileRepository.save(any(PropertySearcherUserProfile.class))).thenAnswer(i -> i.getArgument(0));
        when(keycloakService.impersonateUser(anyString())).thenReturn(new ImpersonateResponse());
        when(keycloakService.searchUser(anyString())).thenReturn(Collections.singletonList(userRep));
        when(keycloakService.createUser(anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString())).thenReturn(true);

        PropertySearcherRegisterBean propertySearcherBean = getPropertySearcherBean();
        onboardingService.changePropertySearcherStateToRegistered(propertySearcherBean);

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any(PropertySearcherUser.class));
        verify(userProfileRepository, times(1)).save(any(PropertySearcherUserProfile.class));
        verify(keycloakService, times(1)).removeUser(anyString());
        verify(keycloakService, times(1)).impersonateUser(anyString());
        verify(keycloakService, times(1)).createUser(anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString());

        Assertions.assertEquals(user.getMainProfile().getData().getFirstname(), propertySearcherBean.getFirstName());
        Assertions.assertEquals(user.getMainProfile().getData().getName(), propertySearcherBean.getLastName());
        Assertions.assertEquals(user.getType(), PropertySearcherUserType.REGISTERED);
    }

    @Test
    public void registerWhenUsersSizeNotOne() {
        List<UserRepresentation> list = new ArrayList<>();
        list.add(new UserRepresentation());
        list.add(new UserRepresentation());

        PropertySearcherUser user = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer());

        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(userRepository.save(any(PropertySearcherUser.class))).thenAnswer(i -> i.getArgument(0));
        when(userProfileRepository.save(any(PropertySearcherUserProfile.class))).thenAnswer(i -> i.getArgument(0));
        when(keycloakService.impersonateUser(anyString())).thenReturn(new ImpersonateResponse());
        when(keycloakService.searchUser(anyString())).thenReturn(list);
        when(keycloakService.createUser(anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString())).thenReturn(true);

        PropertySearcherRegisterBean propertySearcherBean = getPropertySearcherBean();
        onboardingService.changePropertySearcherStateToRegistered(propertySearcherBean);

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any(PropertySearcherUser.class));
        verify(userProfileRepository, times(1)).save(any(PropertySearcherUserProfile.class));
        verify(keycloakService, times(2)).removeUser(nullable(String.class));
        verify(keycloakService, times(1)).impersonateUser(anyString());
        verify(keycloakService, times(1)).createUser(anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString());

        Assertions.assertEquals(user.getMainProfile().getData().getFirstname(), propertySearcherBean.getFirstName());
        Assertions.assertEquals(user.getMainProfile().getData().getName(), propertySearcherBean.getLastName());
        Assertions.assertEquals(user.getType(), PropertySearcherUserType.REGISTERED);
    }

    @Test
    public void registerWhenCreatedIsFalse() {
        List<UserRepresentation> list = new ArrayList<>();
        var userRep = new UserRepresentation();
        userRep.setEmail("test@Mail.de");
        list.add(userRep);

        PropertySearcherUser user = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer());

        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(keycloakService.searchUser(anyString())).thenReturn(list);
        when(keycloakService.createUser(anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString())).thenReturn(false);

        Assertions.assertThrows(BadRequestException.class,
                () -> onboardingService.changePropertySearcherStateToRegistered(getPropertySearcherBean()));

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(keycloakService, times(1)).removeUser(anyString());
        verify(keycloakService, never()).impersonateUser(anyString());
        verify(keycloakService, times(1)).createUser(anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString());
        verify(userRepository, never()).save(any(PropertySearcherUser.class));
        verify(keycloakService, times(1)).searchUser(anyString());
    }
}