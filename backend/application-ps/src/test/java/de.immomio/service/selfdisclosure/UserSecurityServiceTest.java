package de.immomio.service.selfdisclosure;

import de.immomio.data.base.entity.customer.user.CustomUserDetails;
import de.immomio.data.base.entity.security.BaseAuthority;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.base.type.user.PropertySearcherUserRightType;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserRepository;
import de.immomio.model.repository.propertysearcher.user.right.PropertySearcherUserTypeRightRepository;
import de.immomio.service.security.UserSecurityService;
import de.immomio.utils.TestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Freddy Sawma
 */

@ExtendWith(MockitoExtension.class)
public class UserSecurityServiceTest {

    @Mock
    private PropertySearcherUserRepository userRepository;

    @Mock
    private PropertySearcherUserTypeRightRepository propertySearcherUserTypeRightRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Spy
    @InjectMocks
    private UserSecurityService userSecurityService;

    @BeforeEach
    public void setAuthentication() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void getUserRepository() {
        when(userSecurityService.getUserRepository()).thenReturn(userRepository);

        PropertySearcherUserRepository expectedRepository = userSecurityService.getUserRepository();

        Assertions.assertEquals(expectedRepository, userRepository);

    }

    @Test
    public void getPrincipal() {
        PropertySearcherUser user = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer());

        user.setAuthorities(Collections.singletonList(new BaseAuthority("authority")));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(new CustomUserDetails<>());
        when(userRepository.loadById(nullable(Long.class))).thenReturn(user);

        PropertySearcherUser actualUser = userSecurityService.getPrincipal();

        verify(userRepository, times(1)).loadById(nullable(Long.class));

        Assertions.assertEquals(actualUser, user);
    }

    @Test
    public void getPrincipalUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(new CustomUserDetails<>());

        userSecurityService.getPrincipalUser();

        verify(userRepository, times(1)).loadById(nullable(Long.class));
    }

    @Test
    public void getPrincipalId() {
        PropertySearcherUser user = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer());

        doReturn(user).when(userSecurityService).getPrincipal();
        userSecurityService.getPrincipalId();

        verify(userSecurityService, times(1)).getPrincipal();
    }

    @Test
    public void allowUserToReadPropertyWhenPropertyIsNull() {
        Assertions.assertFalse(userSecurityService.allowUserToReadProperty(null, 99L));
    }

    @Test
    public void allowUserToReadPropertyWhenApplicationDoesNotMatch() throws Exception {
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer(), 999L);
        List<PropertyApplication> applications = TestHelper.generateApplications(3, ApplicationStatus.ACCEPTED);
        property.setPropertyApplications(applications);

        Assertions.assertFalse(userSecurityService.allowUserToReadProperty(property, 99L));
    }

    @Test
    public void allowUserToReadProperty() throws Exception {
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer(), 99L);
        List<PropertyApplication> applications = TestHelper.generateApplications(3, ApplicationStatus.ACCEPTED);
        property.setPropertyApplications(applications);

        Assertions.assertTrue(userSecurityService.allowUserToReadProperty(property, 1L));
    }

    @Test
    public void getAuthoritiesWhenTypesAreNull() {
        PropertySearcherUser user = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer());

        when(propertySearcherUserTypeRightRepository.findAllByUserType(
                any(PropertySearcherUserRightType.class))).thenReturn(null);

        Collection<BaseAuthority> actualAuthorities = userSecurityService.getAuthorities(user);

        verify(propertySearcherUserTypeRightRepository, times(1)).findAllByUserType(
                eq(user.getUsertype()));

        Assertions.assertEquals(actualAuthorities, user.getCustomer().getAuthorities(user.getUsertype()));
    }

    @Test
    public void getAuthoritiesWhenTypesNotNull() {
        PropertySearcherUser user = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer());

        when(propertySearcherUserTypeRightRepository.findAllByUserType(
                any(PropertySearcherUserRightType.class))).thenReturn(TestHelper.generateUserTypeRights());

        Collection<BaseAuthority> actualAuthorities = userSecurityService.getAuthorities(user);

        verify(propertySearcherUserTypeRightRepository, times(1)).findAllByUserType(
                eq(user.getUsertype()));

        Assertions.assertEquals(1, actualAuthorities.size());
    }

}