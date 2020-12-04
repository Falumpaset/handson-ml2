package de.immomio.broker.service;

import de.immomio.broker.AbstractTest;
import de.immomio.broker.utils.TestHelper;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.core.shared.application.BasePropertyApplicationRepository;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Niklas Lindemann
 */
public class UserServiceTest extends AbstractTest {

    @Mock
    private BasePropertyApplicationRepository applicationRepository;

    @InjectMocks
    private UserService userService;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(userService, "proposalThreshold", 2);
    }

    @Test
    public void userIsBlockedForCreatingProposals() throws IllegalAccessException, NoSuchFieldException {
        PropertyApplication application = TestHelper.generateApplication(ApplicationStatus.ACCEPTED, Portal.EBAY);
        application.setCreated(LocalDate.now().minusDays(5).toDate());

        when(applicationRepository.findFirstByUserProfileOrderByCreatedAsc(any())).thenReturn(Optional.of(application));

        PropertySearcherUserProfile userProfile = TestHelper.generatePropertySearcherUser();
        userProfile.setCreated(LocalDate.now().minusDays(2).toDate());
        boolean blocked = userService.isUserBlockedForCreatingProposals(userProfile);

        Assert.assertTrue(blocked);
    }

    @Test
    public void userIsNotBlockedForCreatingProposalsDate() throws IllegalAccessException, NoSuchFieldException {
        PropertyApplication application = TestHelper.generateApplication(ApplicationStatus.ACCEPTED, Portal.EBAY);
        application.setCreated(LocalDate.now().minusDays(20).toDate());

        when(applicationRepository.findFirstByUserProfileOrderByCreatedAsc(any())).thenReturn(Optional.of(application));

        PropertySearcherUserProfile userProfile = TestHelper.generatePropertySearcherUser();
        userProfile.setCreated(LocalDate.now().minusDays(2).toDate());
        boolean blocked = userService.isUserBlockedForCreatingProposals(userProfile);

        Assert.assertFalse(blocked);
    }

    @Test
    public void userIsNotBlockedForCreatingProposalsPortal() throws IllegalAccessException, NoSuchFieldException {
        PropertyApplication application = TestHelper.generateApplication(ApplicationStatus.ACCEPTED, null);
        application.setCreated(LocalDate.now().minusDays(5).toDate());

        when(applicationRepository.findFirstByUserProfileOrderByCreatedAsc(any())).thenReturn(Optional.of(application));

        PropertySearcherUserProfile userProfile = TestHelper.generatePropertySearcherUser();
        userProfile.setCreated(LocalDate.now().minusDays(2).toDate());

        boolean blocked = userService.isUserBlockedForCreatingProposals(userProfile);

        Assert.assertFalse(blocked);
    }

    @Test
    public void userIsNotBlockedForCreatingProposalsRejected() throws IllegalAccessException, NoSuchFieldException {
        PropertyApplication application = TestHelper.generateApplication(ApplicationStatus.REJECTED, Portal.EBAY);
        application.setCreated(LocalDate.now().minusDays(5).toDate());

        when(applicationRepository.findFirstByUserProfileOrderByCreatedAsc(any())).thenReturn(Optional.of(application));

        PropertySearcherUserProfile userProfile = TestHelper.generatePropertySearcherUser();
        userProfile.setCreated(LocalDate.now().minusDays(2).toDate());

        boolean blocked = userService.isUserBlockedForCreatingProposals(userProfile);

        Assert.assertFalse(blocked);
    }

    @Test
    public void userIsNotBlockedForCreatingProposalsCreated() throws IllegalAccessException, NoSuchFieldException {
        PropertyApplication application = TestHelper.generateApplication(ApplicationStatus.REJECTED, Portal.EBAY);
        application.setCreated(LocalDate.now().minusDays(5).toDate());

        PropertySearcherUserProfile userProfile = TestHelper.generatePropertySearcherUser();
        userProfile.setCreated(LocalDate.now().minusWeeks(4).toDate());

        verify(applicationRepository, never()).findFirstByUserProfileOrderByCreatedAsc(any());
        boolean blocked = userService.isUserBlockedForCreatingProposals(userProfile);

        Assert.assertFalse(blocked);
    }
}