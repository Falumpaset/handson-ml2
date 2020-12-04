package de.immomio.landlord.service.followup;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.property.PropertyStatus;
import de.immomio.data.landlord.entity.property.followup.Followup;
import de.immomio.data.landlord.entity.property.followup.FollowupWorkingState;
import de.immomio.data.landlord.entity.property.followup.bean.FollowupBean;
import de.immomio.data.landlord.entity.property.followup.bean.FollowupNotificationBean;
import de.immomio.landlord.service.property.PropertyService;
import de.immomio.model.repository.landlord.followup.FollowupRepository;
import de.immomio.service.AbstractTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import utils.TestHelper;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static de.immomio.data.landlord.entity.property.followup.FollowupWorkingState.PROCESSED;
import static de.immomio.data.landlord.entity.property.followup.FollowupWorkingState.UNPROCESSED;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Niklas Lindemann
 */

public class FollowupServiceTest extends AbstractTest {

    @Mock
    private FollowupRepository followupRepository;

    @Mock
    private FollowupNotificationService followupNotificationService;

    @Mock
    private FollowupIntervalSettingsService followupIntervalSettingsService;

    @Mock
    private PropertyService propertyService;

    @Captor
    private ArgumentCaptor<List<FollowupNotificationBean>> followupNotificationBeansCaptor;

    @Spy
    @InjectMocks
    private FollowupService followupService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Date followupDateOne;
    private Date followupDateTwo;
    private Date followupDateThree;

    @Before
    public void init() {
        ZonedDateTime now = ZonedDateTime.now();

        followupDateOne = Date.from(now.plusDays(1).toInstant());
        followupDateTwo = Date.from(now.plusDays(2).toInstant());
        followupDateThree = Date.from(now.toInstant());
    }

    @Test
    public void createFollowupSetNotReserved() {
        FollowupBean followupBean = FollowupBean.builder().date(followupDateOne).id(1L).reason("Hallo").notifications(new ArrayList<>()).build();

        when(followupRepository.save(any(Followup.class))).thenAnswer(saveCall -> saveCall.getArgument(0));

        Followup followupResult = followupService.create(TestHelper.generateProperty(TestHelper.generateLandlordCustomer(1L), 1L), followupBean, false);

        verify(followupNotificationService).updateNotifications(any(Followup.class), anyList());
        verify(followupIntervalSettingsService).saveFollowupIntervals(any(Date.class), anyList());
        verify(followupRepository).save(any(Followup.class));
        verify(propertyService, never()).updatePropertyState(any(), any());

        Assert.assertEquals(followupDateOne.getTime(), followupResult.getDate().getTime());
        Assert.assertEquals(FollowupWorkingState.UNPROCESSED, followupResult.getState());
        Assert.assertEquals("Hallo", followupResult.getReason());
    }

    @Test
    public void createFollowupSetReserved() {
        FollowupBean followupBean = FollowupBean.builder().date(followupDateOne).id(1L).reason("Hallo").notifications(new ArrayList<>()).build();

        when(followupRepository.save(any(Followup.class))).thenAnswer(saveCall -> saveCall.getArgument(0));

        Followup followupResult = followupService.create(TestHelper.generateProperty(TestHelper.generateLandlordCustomer(1L), 1L), followupBean, true);

        verify(propertyService).updatePropertyState(any(), eq(PropertyStatus.RESERVED));
    }

    @Test(expected = ApiValidationException.class)
    public void editFollowupAlreadyProcessed() {
        Followup followup = new Followup();
        followup.setState(PROCESSED);

        FollowupBean followupBean = FollowupBean.builder().date(followupDateOne).id(1L).reason("Hallo").notifications(new ArrayList<>()).build();

        followupService.saveFollowup(followup, followupBean);
    }

    @Test
    public void saveFollowup() {
        Followup followup = new Followup();
        followup.setDate(followupDateOne);
        followup.setReason("Test1");
        followup.setState(UNPROCESSED);

        List<FollowupNotificationBean> notificationBeans = new ArrayList<>();
        notificationBeans.add(FollowupNotificationBean.builder().date(followupDateThree).build());
        FollowupBean followupBean = FollowupBean.builder().date(followupDateTwo).reason("Test2").notifications(notificationBeans).build();

        when(followupRepository.save(any(Followup.class))).thenAnswer(saveCall -> saveCall.getArgument(0));

        followup = followupService.saveFollowup(followup, followupBean);

        verify(followupNotificationService).updateNotifications(any(Followup.class), eq(followupBean.getNotifications()));
        verify(followupIntervalSettingsService).saveFollowupIntervals(eq(followupDateTwo), eq(followupBean.getNotifications()));

        Assert.assertEquals(UNPROCESSED, followup.getState());
        Assert.assertEquals("Test2", followup.getReason());
        Assert.assertEquals(followupDateTwo.getTime(), followup.getDate().getTime());
    }


    @Test
    public void setFollowupProcessed() {
        Followup followup = new Followup();
        followup.setState(UNPROCESSED);

        when(followupRepository.save(any(Followup.class))).thenAnswer(saveCall -> saveCall.getArgument(0));

        followup = followupService.setProcessed(followup, true);

        Assert.assertEquals(PROCESSED, followup.getState());
    }

    @Test
    public void setFollowupUnProcessed() {
        Followup followup = new Followup();
        followup.setState(PROCESSED);

        followupService.setProcessed(followup, false);
        Assert.assertEquals(UNPROCESSED, followup.getState());

    }
}