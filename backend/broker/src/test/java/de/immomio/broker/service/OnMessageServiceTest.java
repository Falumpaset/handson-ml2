package de.immomio.broker.service;

import de.immomio.broker.AbstractTest;
import de.immomio.broker.service.application.PropertyApplicationService;
import de.immomio.broker.service.proposal.PropertyProposalService;
import de.immomio.broker.service.proposal.PropertyRequirementService;
import de.immomio.broker.utils.TestHelper;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfile;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.messaging.container.proposal.LandlordPropertyMessageContainer;
import de.immomio.messaging.container.proposal.PSSearchProfileMessageContainer;
import de.immomio.messaging.container.user.PSUserProfileMessageContainer;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import de.immomio.model.repository.core.propertysearcher.searchprofile.BasePropertySearcherSearchProfileRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserProfileRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OnMessageServiceTest extends AbstractTest {
    @Mock
    private BasePropertySearcherSearchProfileRepository searchProfileRepository;

    @Mock
    private BasePropertyRepository propertyRepository;

    @Mock
    private PropertyProposalService propertyProposalService;

    @Mock
    private PropertyRequirementService requirementService;

    @Mock
    private PropertyApplicationService propertyApplicationService;

    @Mock
    private BrokerGeoCodingService brokerGeoCodingService;

    @Mock
    private BasePropertySearcherUserProfileRepository userProfileRepository;

    @InjectMocks
    @Spy
    private OnMessageService onMessageService;

    @Test
    public void onMessageForPsSearchProfile() {
        PSSearchProfileMessageContainer container = new PSSearchProfileMessageContainer();
        container.setSearchProfileId(1377L);

        when(searchProfileRepository.findById(anyLong())).thenReturn(Optional.of(TestHelper.generatePropertySearcherSearchProfile()));
        onMessageService.onMessage(container);
        verify(propertyRepository, never()).customFindNearestToPoint(anyDouble(), anyDouble(), anyLong());
        verify(searchProfileRepository, times(1)).findById(anyLong());
    }

    @Test
    public void onMessageForPsSearchProfileNullProfile() {
        when(searchProfileRepository.findById(anyLong())).thenReturn(null);
        PSSearchProfileMessageContainer container = new PSSearchProfileMessageContainer();
        onMessageService.onMessage(container);

        verify(propertyProposalService, never()).generateForSearchProfile(any(PropertySearcherSearchProfile.class));
    }

    @Test
    public void onMessageForLandlordUser() {
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer());
        when(propertyRepository.findAllByIdIn(anyList())).thenReturn(Collections.singletonList(property));

        LandlordPropertyMessageContainer container = new LandlordPropertyMessageContainer();
        container.setCalculateCoordinates(true);
        onMessageService.onMessage(container);

        verify(brokerGeoCodingService, times(1)).generateAndSaveCoordinates(anyList(), anyBoolean());
        verify(propertyApplicationService, times(1)).updateScoreWhenPropertyUpdated(nullable(Long.class));
        verify(propertyProposalService, times(1)).calculateProposals(property);
    }

    @Test
    public void onMessageForPsUser() {
        when(userProfileRepository.findById(anyLong())).thenReturn(Optional.of(TestHelper.generatePropertySearcherUser()));
        PSUserProfileMessageContainer container = new PSUserProfileMessageContainer();
        container.setUserProfileId(8128L);
        onMessageService.onMessage(container);

        verify(propertyApplicationService, times(1)).updateScoreWhenUserUpdated(any());
        verify(propertyProposalService, times(1)).generateForUser(any(PropertySearcherUserProfile.class));
    }
}