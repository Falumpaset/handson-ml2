package de.immomio.service.application;

import com.vividsolutions.jts.util.Assert;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.propertysearcher.bean.AppointmentBundleBean;
import de.immomio.service.appointment.AppointmentService;
import de.immomio.utils.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

/**
 * @author Freddy Sawma
 */

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Spy
    @InjectMocks
    private AppointmentService appointmentBundleService;

    @Test
    public void getAppointmentBundleBeans() {
        PropertySearcherUserProfile mainProfile = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer()).getMainProfile();

        List<AppointmentBundleBean> appointmentBundleBeans = appointmentBundleService.getAppointmentBundleBeans(mainProfile);

        Assert.equals(7, appointmentBundleBeans.size());
    }
}