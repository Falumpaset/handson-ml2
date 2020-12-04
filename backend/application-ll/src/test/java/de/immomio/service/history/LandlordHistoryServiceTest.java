package de.immomio.service.history;

import de.immomio.beans.shared.PropertySearcherHistoryBean;
import de.immomio.beans.shared.PropertySearcherHistoryType;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.application.LandlordPropertyApplicationService;
import de.immomio.landlord.service.history.LandlordHistoryService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.service.AbstractTest;
import de.immomio.service.property.PropertyProposalService;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import utils.TestHelper;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static utils.TestHelper.generateApplication;

/**
 * @author Niklas Lindemann
 */


public class LandlordHistoryServiceTest extends AbstractTest {


    @Mock
    private UserSecurityService userSecurityService;

    @Mock
    private  LandlordPropertyApplicationService propertyApplicationService;

    @Mock
    private PropertyProposalService propertyProposalService;

    @InjectMocks
    private LandlordHistoryService historyService;

    @Test
    public void getHistoryWithoutAppointment() throws InterruptedException {
        PropertyApplication application = generateApplication(ApplicationStatus.ACCEPTED);
        Date accepted = LocalDate.now().plusDays(1).toDate();
        application.setAccepted(accepted);

        when(userSecurityService.getPrincipalUser()).thenReturn(TestHelper.generateLandlordUser(LandlordUsertype.COMPANYADMIN, 1L));
        when(propertyApplicationService.findByLandlordAndPropertySearcher(any(), any())).thenReturn(Collections.singletonList(application));
        List<PropertySearcherHistoryBean> history = historyService.getHistory(application.getUserProfile());
        PropertySearcherHistoryBean applicationAcceptedBean = history.get(0);
        PropertySearcherHistoryBean applicationCreatedBean = history.get(1);

        Assert.assertEquals(2, history.size());
        Assert.assertEquals(applicationCreatedBean.getType(), PropertySearcherHistoryType.APPLIED);
        Assert.assertEquals(applicationAcceptedBean.getType(), PropertySearcherHistoryType.ACCEPTED);
    }

    @Test
    public void getHistoryWithAppointment() throws InterruptedException {
        PropertyApplication application = generateApplication(ApplicationStatus.ACCEPTED);
        LocalDate now = LocalDate.now();
        Date accepted = now.plusDays(1).toDate();
        Date appointment1Date = now.plusDays(2).toDate();
        Date appointment2Date = now.plusDays(3).toDate();
        application.setAccepted(accepted);

        Appointment appointment1 = TestHelper.generateAppointment(appointment1Date);
        appointment1.setCreated(now.plusDays(4).toDate());
        Appointment appointment2 = TestHelper.generateAppointment(appointment2Date);
        appointment2.setCreated(now.plusDays(5).toDate());
        AppointmentAcceptance acceptance1 = TestHelper.generateAppointmentAcceptance(appointment1, application, AppointmentAcceptanceState.ACTIVE);
        acceptance1.setCreated(now.plusDays(6).toDate());
        AppointmentAcceptance acceptance2 = TestHelper.generateAppointmentAcceptance(appointment2, application, AppointmentAcceptanceState.CANCELED);
        acceptance2.setCreated(now.plusDays(7).toDate());
        application.getAppointmentAcceptances().add(acceptance1);
        application.getAppointmentAcceptances().add(acceptance2);

        when(userSecurityService.getPrincipalUser()).thenReturn(TestHelper.generateLandlordUser(LandlordUsertype.COMPANYADMIN, 1L));
        when(propertyApplicationService.findByLandlordAndPropertySearcher(any(), any())).thenReturn(Collections.singletonList(application));

        List<PropertySearcherHistoryBean> history = historyService.getHistory(application.getUserProfile());

        PropertySearcherHistoryBean viewingBean = history.get(0);
        PropertySearcherHistoryBean applicationAcceptedBean = history.get(1);
        PropertySearcherHistoryBean applicationCreatedBean = history.get(2);

        Assert.assertEquals(3, history.size());
        Assert.assertEquals(viewingBean.getType(), PropertySearcherHistoryType.VIEWING);
        Assert.assertEquals(applicationCreatedBean.getType(), PropertySearcherHistoryType.APPLIED);
        Assert.assertEquals(applicationAcceptedBean.getType(), PropertySearcherHistoryType.ACCEPTED);
    }
}