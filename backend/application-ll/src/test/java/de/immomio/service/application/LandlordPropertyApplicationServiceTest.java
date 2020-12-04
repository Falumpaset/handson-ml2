package de.immomio.service.application;

import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.application.LandlordPropertyApplicationService;
import de.immomio.landlord.service.note.NoteService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.model.repository.shared.note.NoteRepository;
import de.immomio.service.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import utils.TestHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Niklas Lindemann
 */

public class LandlordPropertyApplicationServiceTest extends AbstractTest {

    private static final long USER_ID_1 = 1L;

    private static final long USER_ID_2 = 2L;

    @Mock
    private LandlordMailSender mailSender;

    @Mock
    private PropertyApplicationRepository propertyApplicationRepository;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserSecurityService userSecurityService;

    @Mock
    private NoteService noteService;

    @InjectMocks
    @Spy
    private LandlordPropertyApplicationService applicationService;


    @Test
    public void processApplicationsTrue() {
        long one = 1L;
        long two = 2L;
        List<Long> ids = Arrays.asList(one, two);

        PropertyApplication firstApplication = new PropertyApplication();
        TestHelper.setId(firstApplication, one);
        PropertyApplication secondApplication = new PropertyApplication();
        TestHelper.setId(secondApplication, two);

        when(propertyApplicationRepository.findById(eq(one))).thenReturn(Optional.of(firstApplication));
        when(propertyApplicationRepository.findById(eq(two))).thenReturn(Optional.of(secondApplication));
        when(userSecurityService.allowedToReadApplication(any())).thenReturn(true);

        applicationService.tagSeen(ids, true);
        verify(propertyApplicationRepository, times(2)).save(any(PropertyApplication.class));

        Assert.assertNotNull(firstApplication.getSeen());
        Assert.assertNotNull(secondApplication.getSeen());
    }

    @Test
    public void processApplicationsFalse() {
        long idOne = 1L;
        long idTwo = 2L;
        List<Long> ids = Arrays.asList(idOne, idTwo);

        PropertyApplication firstApplication = new PropertyApplication();
        TestHelper.setId(firstApplication, idOne);
        PropertyApplication secondApplication = new PropertyApplication();
        TestHelper.setId(secondApplication, idTwo);

        when(propertyApplicationRepository.findById(eq(idOne))).thenReturn(Optional.of(firstApplication));
        when(propertyApplicationRepository.findById(eq(idTwo))).thenReturn(Optional.of(secondApplication));
        when(userSecurityService.allowedToReadApplication(any())).thenReturn(true);

        applicationService.tagSeen(ids, false);
        verify(propertyApplicationRepository, times(2)).save(any(PropertyApplication.class));

        Assert.assertNull(firstApplication.getSeen());
        Assert.assertNull(secondApplication.getSeen()
        );
    }

}