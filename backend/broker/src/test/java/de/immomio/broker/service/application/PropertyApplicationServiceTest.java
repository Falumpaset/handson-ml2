package de.immomio.broker.service.application;

import de.immomio.broker.AbstractTest;
import de.immomio.broker.service.CustomQuestionResponseService;
import de.immomio.broker.utils.TestHelper;
import de.immomio.data.base.bean.score.CustomQuestionScoreBean;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.core.shared.application.BasePropertyApplicationRepository;
import de.immomio.model.repository.core.shared.priosetCustomQuestion.BasePriosetCustomQuestionAssociationRepository;
import de.immomio.service.calculator.CalculationExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * @author Freddy Sawma.
 */

public class PropertyApplicationServiceTest extends AbstractTest {

    @Mock
    private CalculationExecutor calculationExecutor;

    @Mock
    private BasePropertyApplicationRepository propertyApplicationRepository;

    @Mock
    private CustomQuestionResponseService customQuestionResponseService;

    @Mock
    private BasePriosetCustomQuestionAssociationRepository basePropertyCustomQuestionAssociationRepository;

    @Spy
    @InjectMocks
    private PropertyApplicationService propertyApplicationService;

    private List<PropertyApplication> applications;

    @Before
    public void generateApplications() {
        applications = TestHelper.generateApplications(3, ApplicationStatus.ACCEPTED);
    }

    @Test
    public void updateScoreWhenPropertyUpdated() {
        when(propertyApplicationRepository.findAllByPropertyId(anyLong())).thenReturn(applications);
        when(calculationExecutor.calculateCustomQuestionScore(any(Property.class), anyList(), anyInt()))
                .thenReturn(CustomQuestionScoreBean.builder().scoreExcludingRange(BigDecimal.ONE).scoreIncludingRange(BigDecimal.ONE).build());
        propertyApplicationService.updateScoreWhenPropertyUpdated(anyLong());

        verify(propertyApplicationService, times(1)).updateScoreWhenPropertyUpdated(
                anyLong());
        verify(customQuestionResponseService, times(1))
                .getCustomQuestionResponses(applications.get(0));
    }

    @Test
    public void updateScoreWhenUserUpdated() {
        when(propertyApplicationRepository.findAllByUserProfile(any())).thenReturn(applications);
        when(calculationExecutor.calculateCustomQuestionScore(any(Property.class), anyList(), anyInt())).thenReturn(CustomQuestionScoreBean.builder().scoreExcludingRange(BigDecimal.ONE).scoreIncludingRange(BigDecimal.ONE).build());
        propertyApplicationService.updateScoreWhenUserUpdated(any());

        verify(propertyApplicationService, times(1)).updateScoreWhenUserUpdated(
                any());
        verify(customQuestionResponseService, times(1))
                .getCustomQuestionResponses(applications.get(0));
    }
}