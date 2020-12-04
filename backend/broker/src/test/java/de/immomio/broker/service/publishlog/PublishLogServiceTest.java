package de.immomio.broker.service.publishlog;

import de.immomio.broker.AbstractTest;
import de.immomio.broker.utils.TestComparatorHelper;
import de.immomio.broker.utils.TestHelper;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.publish.PublishLog;
import de.immomio.model.repository.core.landlord.customer.property.publishlog.BaseLandlordPublishLogRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PublishLogServiceTest extends AbstractTest {

    @Mock
    private BaseLandlordPublishLogRepository publishLogRepository;

    @InjectMocks
    @Spy
    private PublishLogService publishLogService;

    @Test
    public void savePublishLog() {
        PublishLog publishLog = new PublishLog();
        publishLogService.savePublishLog(publishLog);

        verify(publishLogRepository, times(1)).save(eq(publishLog));
    }

    @Test
    public void createPublishLog() {
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer());
        PublishLog publishLog = TestHelper.generatePublishLog(property);

        when(publishLogRepository.save(any(PublishLog.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        PublishLog actualPublishLog = publishLogService.createPublishLog(property);
        TestComparatorHelper.comparePublishLogs(actualPublishLog, publishLog);

        verify(publishLogRepository, times(1)).save(any(PublishLog.class));
    }
}