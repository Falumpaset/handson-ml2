package de.immomio.service.security;

import de.immomio.data.base.entity.customer.user.CustomUserDetails;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.landlord.service.caching.LandlordCachingService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import de.immomio.model.repository.landlord.customer.user.right.usertyperight.LandlordUserTypeRightRepository;
import de.immomio.model.repository.shared.property.PropertyRepository;
import de.immomio.service.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.security.core.Authentication;
import utils.TestHelper;

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * @author Niklas Lindemann
 */

public class UserSecurityServiceTest extends AbstractTest {

    @Mock
    private LandlordUserRepository landlordUserRepository;

    @Mock
    private LandlordUserTypeRightRepository landlordUserTypeRightRepository;

    @Mock
    private LandlordCachingService landlordCachingService;

    @Mock
    private Authentication authentication;

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    @Spy
    private UserSecurityService userSecurityService;

    @Test
    public void getUserRepository() {
        LandlordUserRepository userRepository = userSecurityService.getUserRepository();
        Assert.assertEquals(userRepository, landlordUserRepository);
    }

    @Test
    public void getPrincipal() {
        CustomUserDetails customUserDetails = TestHelper.generateCustomUserDetails();
        LandlordUser landlordUser = TestHelper.generateLandlordUser((LandlordCustomer) customUserDetails.getCustomer(),
                LandlordUsertype.COMPANYADMIN, 1L);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(userSecurityService.getAuthentication()).thenReturn(authentication);
        when(landlordUserRepository.loadById(anyLong())).thenReturn(landlordUser);
        doReturn(Collections.emptyList()).when(userSecurityService).getAuthorities(any());

        LandlordUser principal = userSecurityService.getPrincipalUser();

        Assert.assertEquals(principal, landlordUser);
    }

    @Test
    public void allowUserToReadProperty() {
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer(1L), 1L);
        property.setPropertyApplications(TestHelper.generateApplications(1, ApplicationStatus.ACCEPTED));
        boolean allow = userSecurityService.allowUserToReadProperty(property, 1L);
        Assert.assertTrue(allow);
    }

    @Test
    public void allowUserNotToReadProperty() {
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer(1L), 1L);
        property.setPropertyApplications(TestHelper.generateApplications(1, ApplicationStatus.ACCEPTED));
        boolean allow = userSecurityService.allowUserToReadProperty(property, 2L);

        Assert.assertFalse(allow);
    }

    @Test
    public void hasPropertyDeleteRight() {
        LandlordUser user = TestHelper.generateLandlordUser(LandlordUsertype.COMPANYADMIN, 1L);

        doReturn(user).when(userSecurityService).getPrincipalUser();
        doReturn(user).when(userSecurityService).getPrincipal();

        Property property = TestHelper.generateProperty(user.getCustomer(), 1L);
        boolean hasPropertyDeleteRight = userSecurityService.hasPropertyDeleteRight(property);

        Assert.assertTrue(hasPropertyDeleteRight);
    }

    @Test
    public void isCompanyAdmin() {
        doReturn(TestHelper.generateLandlordUser(LandlordUsertype.COMPANYADMIN, 1L)).when(userSecurityService).getPrincipalUser();

        boolean companyAdmin = userSecurityService.isCompanyAdmin();

        Assert.assertTrue(companyAdmin);
    }

    @Test
    public void isNotCompanyAdmin() {
        doReturn(TestHelper.generateLandlordUser(LandlordUsertype.EMPLOYEE, 1L)).when(userSecurityService).getPrincipalUser();

        boolean companyAdmin = userSecurityService.isCompanyAdmin();

        Assert.assertFalse(companyAdmin);
    }

    // TODO: 2019-04-17 implement
    @Test
    public void getAuthoritiesDefault() {
//        BaseAuthority authority = TestHelper.createAuthority();
//        LandlordCustomer landlordCustomer = TestHelper.generateLandlordCustomer();
//        LandlordUser landlordUser = TestHelper.generateLandlordUser(LandlordUsertype.EMPLOYEE);

    }
}