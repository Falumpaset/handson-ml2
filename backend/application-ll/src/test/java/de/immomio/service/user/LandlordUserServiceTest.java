package de.immomio.service.user;

import de.immomio.constants.exceptions.TokenValidationException;
import de.immomio.constants.exceptions.UserNotFoundException;
import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.landlord.service.user.LandlordChangePasswordService;
import de.immomio.landlord.service.user.LandlordUserService;
import de.immomio.mail.configurator.LandlordMailConfigurator;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import de.immomio.security.common.bean.UserEmailToken;
import de.immomio.security.service.JWTTokenService;
import de.immomio.security.service.KeycloakService;
import de.immomio.service.AbstractTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import utils.TestHelper;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Freddy Sawma
 */

public class LandlordUserServiceTest extends AbstractTest {

    private static final String TOKEN = "0";
    private static final String TEST_EMAIL_DE = "test@email.de";

    @Mock
    private JWTTokenService jwtTokenService;

    @Mock
    private LandlordUserRepository userRepository;

    @Mock
    private LandlordMailSender landlordMailSender;

    @Mock
    private LandlordChangePasswordService changePasswordService;

    @Mock
    private LandlordMailConfigurator landlordMailConfigurator;

    @Mock
    private KeycloakService keycloakService;

    @InjectMocks
    @Spy
    private LandlordUserService landlordUserService;

    private LandlordUser landlordUser;


    @Before
    public void createLandlordUser() {
        landlordUser = TestHelper.generateLandlordUser(
                TestHelper.generateLandlordCustomer(1L),
                LandlordUsertype.COMPANYADMIN,
                1L);
    }

    @Test
    public void findByIdIsNull() {
        Optional<LandlordUser> user = landlordUserService.findById(null);
        Assert.assertFalse("user should be null", user.isPresent());
    }

    @Test
    public void findByIdNotNull() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(landlordUser));

        Optional<LandlordUser> userFound = landlordUserService.findById(0L);
        Assert.assertEquals("users should be equal", userFound, Optional.of(landlordUser));
    }

    @Test
    public void findByEmailIsNull() {
        LandlordUser user = landlordUserService.findByEmail(null);
        Assert.assertNull("user should be null", user);
    }

    @Test
    public void findByEmailNotNull() {
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(landlordUser);

        LandlordUser userFound = landlordUserService.findByEmail("0");
        Assert.assertEquals("users should be equal", userFound, landlordUser);
    }

    @Test
    public void existsWithLastLoginNull() throws UserNotFoundException {
        landlordUser.setLastLogin(null);

        landlordUserService.activateUser(landlordUser);

        verify(changePasswordService, times(1)).resetPassword(anyString());
    }

    @Test
    public void existsWithLastLoginNotNull() throws UserNotFoundException {
        landlordUser.setLastLogin(new Date());

        landlordUserService.activateUser(landlordUser);

        verify(userRepository, times(1)).customSave(landlordUser);
        verify(keycloakService, times(1)).activateUser(anyString());
        verify(changePasswordService, never()).resetPassword(landlordUser.getEmail());
    }

    @Test
    public void activateUser() {
    }

    @Test
    public void deactivateUser() throws UserNotFoundException {
        landlordUser.setEnabled(false);
        landlordUserService.deactivateUser(landlordUser);

        verify(userRepository, times(1)).customSave(landlordUser);
        verify(keycloakService, times(1)).deactivateUser(landlordUser.getEmail());
    }

    @Test
    public void sendEmailVerifyNotification() {
        landlordUserService.sendEmailVerifyNotification(landlordUser);

        verify(landlordMailSender, times(1))
                .send(eq(landlordUser), eq(MailTemplate.EMAIL_VERIFICATION_LANDLORD), anyString(), anyMap());
    }

    @Test
    public void verifyEmailWithEqualIds() {
        when(jwtTokenService.validateEmailToken(anyString())).thenReturn(
                new UserEmailToken(landlordUser.getId(), landlordUser.getEmail()));

        landlordUserService.verifyEmail(landlordUser, TOKEN);

        verify(userRepository, times(1)).customSave(landlordUser);
    }

    @Test(expected = TokenValidationException.class)
    public void verifyEmailWithIdsNotEqual() {
        when(jwtTokenService.validateEmailToken(anyString()))
                .thenReturn(new UserEmailToken(928192L, TEST_EMAIL_DE));

        landlordUserService.verifyEmail(landlordUser, TOKEN);
    }

    @Test
    public void customSave() {
        when(userRepository.customSave(any(LandlordUser.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        LandlordUser updatedUser = landlordUserService.customSave(landlordUser);

        verify(userRepository, times(1)).customSave(landlordUser);

        Assert.assertEquals(updatedUser, landlordUser);
    }

    @Test
    public void updateLastLogin() throws InterruptedException {
        when(userRepository.save(any(LandlordUser.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        Date lastLogin = new Date();
        // test is running too quick for the dates to be different, so I had to sleep the thread for 100 ms
        Thread.sleep(100);
        landlordUser = landlordUserService.updateLastLogin(landlordUser);

        verify(landlordUserService, times(1)).save(landlordUser);

        Assert.assertTrue(landlordUser.getLastLogin().after(lastLogin));
    }

    @Test
    public void updateShowRentedFlatsPreference() {
        when(userRepository.save(any(LandlordUser.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        landlordUser.getPreferences().put(LandlordUserService.SHOW_RENTED_FLATS_KEY, true);
        landlordUser = landlordUserService.updateShowRentedFlatsPreference(true, landlordUser);

        verify(landlordUserService, times(1)).save(landlordUser);

        Assert.assertTrue(Boolean.valueOf((String) landlordUser.getPreferences().get(LandlordUserService.SHOW_RENTED_FLATS_KEY)));
        landlordUser = landlordUserService.updateShowRentedFlatsPreference(false, landlordUser);
        Assert.assertFalse(Boolean.valueOf((String) landlordUser.getPreferences().get(LandlordUserService.SHOW_RENTED_FLATS_KEY)));
    }

    @Test
    public void customDelete() {
        landlordUserService.customDelete(landlordUser);

        verify(userRepository, times(1)).customDelete(landlordUser);
    }

    @Test
    public void customFindOne() {
        when(userRepository.customFindOne(anyLong())).thenReturn(landlordUser);

        LandlordUser returnedUser = landlordUserService.customFindOne(43243L);
        Assert.assertEquals(returnedUser, landlordUser);
    }

    @Test
    public void freeAgentSlotCheckWithoutCountReturnsTrue() {
        LandlordCustomer landlordCustomer = TestHelper.generateLandlordCustomerWithAddon(AddonType.AGENT, 1L);
        boolean freeSlotsAvailable = landlordUserService.freeAgentSlotCheck(landlordCustomer);
        Assert.assertTrue(freeSlotsAvailable);

    }

    @Test
    public void freeAgentSlotCheckWithoutCountReturnsFalse() {
        LandlordCustomer landlordCustomer = TestHelper.generateLandlordCustomerWithAddon(AddonType.AGENT, 1L);
        LandlordUser user = TestHelper.generateLandlordUser(landlordCustomer, LandlordUsertype.EMPLOYEE, 1L);
        landlordCustomer.getUsers().add(user);
        boolean freeSlotsAvailable = landlordUserService.freeAgentSlotCheck(landlordCustomer);
        Assert.assertFalse(freeSlotsAvailable);
    }

    @Test
    public void freeAgentSlotCheckWithCountReturnsTrue() {
        LandlordCustomer landlordCustomer = TestHelper.generateLandlordCustomerWithAddon(AddonType.AGENT, 1L);
        boolean freeSlotsAvailable = landlordUserService.freeAgentSlotCheck(landlordCustomer, 1);
        Assert.assertTrue(freeSlotsAvailable);
    }

    @Test
    public void freeAgentSlotCheckWithCountReturnsFalse() {
        LandlordCustomer landlordCustomer = TestHelper.generateLandlordCustomerWithAddon(AddonType.AGENT, 1L);
        boolean freeSlotsAvailable = landlordUserService.freeAgentSlotCheck(landlordCustomer, 2);
        Assert.assertFalse(freeSlotsAvailable);
    }
}