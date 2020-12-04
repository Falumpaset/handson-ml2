package de.immomio.broker.service;

import de.immomio.broker.AbstractTest;
import de.immomio.broker.utils.TestHelper;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.messaging.container.PropertyApplicationBrokerContainer;
import de.immomio.model.repository.core.shared.application.BasePropertyApplicationRepository;
import de.immomio.model.repository.core.shared.priosetCustomQuestion.BasePriosetCustomQuestionAssociationRepository;
import de.immomio.service.calculator.CalculationExecutor;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ApplicationMessageServiceTest extends AbstractTest {

    @Mock
    private  CalculationExecutor calculationExecutor;

    @Mock
    private  BasePropertyApplicationRepository basePropertyApplicationRepository;

    @Mock
    private BasePriosetCustomQuestionAssociationRepository basePropertyCustomQuestionAssociationRepository;

    @Mock
    private CustomQuestionResponseService customQuestionResponseService;


    @InjectMocks
    @Spy
    private ApplicationMessageService applicationMessageService;

    @Test
    public void onMessageWhenPropertyApplicationIsNull() {
        applicationMessageService.onMessage(new PropertyApplicationBrokerContainer());

        verify(basePropertyApplicationRepository, never()).save(any(PropertyApplication.class));
    }

    @Test
    public void onMessageWhenPropertySearchUserIsNull() {
        PropertyApplication propertyApplication = TestHelper.generateApplication(ApplicationStatus.ACCEPTED, null);
        propertyApplication.setUserProfile(null);

        when(basePropertyApplicationRepository.findById(anyLong())).thenReturn(Optional.of(propertyApplication));

        applicationMessageService.onMessage(new PropertyApplicationBrokerContainer());

        verify(basePropertyApplicationRepository, times(1)).findById(nullable(Long.class));
    }

    @Test
    public void onMessage() {
        PropertyApplication propertyApplication = TestHelper.generateApplication(ApplicationStatus.ACCEPTED, null);
        propertyApplication.setUserProfile(TestHelper.generatePropertySearcherUser());

        when(basePropertyApplicationRepository.findById(anyLong())).thenReturn(Optional.of(propertyApplication));

        PropertyApplicationBrokerContainer message = new PropertyApplicationBrokerContainer();
        message.setId(1L);
        applicationMessageService.onMessage(message);

        verify(customQuestionResponseService, times(1)).getCustomQuestionResponses(eq(propertyApplication));
        verify(basePropertyCustomQuestionAssociationRepository, times(1)).findByPrioset(eq(propertyApplication.getProperty().getPrioset()));

        verify(basePropertyApplicationRepository, times(1)).save(nullable(PropertyApplication.class));
    }

}