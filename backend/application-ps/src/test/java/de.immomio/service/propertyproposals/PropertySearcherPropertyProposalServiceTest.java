package de.immomio.service.propertyproposals;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfile;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.messaging.container.proposal.PSSearchProfileMessageContainer;
import de.immomio.model.repository.shared.propertyProposal.PropertyProposalRepository;
import de.immomio.service.property.cache.PropertyCountRefreshCacheService;
import de.immomio.service.propertyProposals.PropertySearcherPropertyProposalService;
import de.immomio.service.sender.SearchProfileToProposalMessageSender;
import de.immomio.utils.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Freddy Sawma
 */

@ExtendWith(MockitoExtension.class)
public class PropertySearcherPropertyProposalServiceTest {

    @Mock
    private SearchProfileToProposalMessageSender proposalMessageSender;

    @Mock
    private PropertyProposalRepository propertyProposalRepository;

    @Mock
    private PropertyCountRefreshCacheService refreshPropertyCountCache;

    @Spy
    @InjectMocks
    private PropertySearcherPropertyProposalService proposalService;

    @Test
    public void createProposalsFromSP() throws Exception {
        PropertySearcherSearchProfile profile = TestHelper.generatePropertySearcherSearchProfile();
        proposalService.createProposalsFromSP(profile);

        verify(proposalMessageSender, times(1))
                .sendProposalUpdateMessage(any(PSSearchProfileMessageContainer.class));
    }

    @Test
    public void deleteProposalsByProfile() throws Exception {
        PropertySearcherSearchProfile profile = TestHelper.generatePropertySearcherSearchProfile();
        proposalService.deleteProposals(profile);

        verify(propertyProposalRepository, times(1)).customDeleteBySearchProfile(anyList());
        verify(refreshPropertyCountCache, times(1)).refreshProposalCache(any(Property.class));
    }

    @Test
    public void deleteProposalsByUser() {
        PropertySearcherUserProfile userProfile = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer()).getMainProfile();
        proposalService.deleteProposals(userProfile);

        verify(propertyProposalRepository, times(1)).customDeleteByUser(nullable(Long.class));
    }
}