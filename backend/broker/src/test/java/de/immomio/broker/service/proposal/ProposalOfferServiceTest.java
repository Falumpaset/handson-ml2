package de.immomio.broker.service.proposal;

import de.immomio.broker.TestUtils;
import de.immomio.broker.service.MailMessageService;
import de.immomio.broker.service.reporting.BrokerElasticsearchIndexingSenderService;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposalState;
import de.immomio.model.repository.core.shared.propertyProposal.BasePropertyProposalRepository;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Niklas Lindemann
 */

@RunWith(SpringRunner.class)
public class ProposalOfferServiceTest {

    private static Property propertyAutoOffer;

    private static Property propertyNoAutoOffer;

    @Mock
    private BasePropertyProposalRepository propertyProposalRepository;

    @Mock
    private MailMessageService mailMessageService;

    @Mock
    private BrokerElasticsearchIndexingSenderService elasticsearchIndexingService;

    @Spy
    @InjectMocks
    private ProposalOfferService proposalOfferService;

    @BeforeClass
    public static void init() {
        propertyAutoOffer = new Property();
        propertyAutoOffer.setAutoOfferEnabled(true);
        propertyAutoOffer.setAutoOfferThreshold(5d);

        propertyNoAutoOffer = new Property();
        propertyNoAutoOffer.setAutoOfferEnabled(false);
    }

    @Test
    public void offerProposal() {
        PropertyProposal propertyProposal = new PropertyProposal();
        propertyProposal.setScore(5d);
        propertyProposal.setProperty(propertyAutoOffer);
        propertyProposal.setState(PropertyProposalState.PROSPECT);

        doNothing().when(proposalOfferService).inviteForProposal(any());

        proposalOfferService.offerProposalIfNecessary(propertyProposal);

        verify(propertyProposalRepository, times(1)).customSetOffered(any());
        verify(proposalOfferService, times(1)).inviteForProposal(any());
    }

    @Test
    public void offerProposalNotNecessary() {
        PropertyProposal propertyProposal = TestUtils.createPropertyProposal(
                propertyAutoOffer,
                3d,
                PropertyProposalState.PROSPECT);
        doNothing().when(proposalOfferService).inviteForProposal(any());

        proposalOfferService.offerProposalIfNecessary(propertyProposal);

        verify(propertyProposalRepository, times(0)).customSetOffered(any());
        verify(proposalOfferService, times(0)).inviteForProposal(any());
    }
    @Test
    public void proposalShouldBeOffered() {
        PropertyProposal propertyProposal = TestUtils.createPropertyProposal(
                propertyAutoOffer,
                5d,
                PropertyProposalState.PROSPECT);
        boolean shouldBeOffered = proposalOfferService.proposalShouldBeOffered(propertyProposal);

        Assert.assertTrue(shouldBeOffered);
    }

    @Test
    public void proposalShouldNotBeOfferedDueToLowerScore() {
        PropertyProposal propertyProposal = TestUtils.createPropertyProposal(
                propertyAutoOffer,
                3d,
                PropertyProposalState.PROSPECT);
        boolean shouldBeOffered = proposalOfferService.proposalShouldBeOffered(propertyProposal);

        Assert.assertFalse(shouldBeOffered);
    }

    @Test
    public void proposalShouldNotBeOfferedDueToUnsuitableState() {
        PropertyProposal propertyProposal = TestUtils.createPropertyProposal(
                propertyAutoOffer,
                5d,
                PropertyProposalState.OFFERED);
        boolean shouldBeOffered = proposalOfferService.proposalShouldBeOffered(propertyProposal);

        Assert.assertFalse(shouldBeOffered);
    }

    @Test
    public void proposalShouldNotBeOfferedDueToDisabled() {
        PropertyProposal propertyProposal = TestUtils.createPropertyProposal(
                propertyNoAutoOffer,
                5d,
                PropertyProposalState.PROSPECT);
        boolean shouldBeOffered = proposalOfferService.proposalShouldBeOffered(propertyProposal);

        Assert.assertFalse(shouldBeOffered);
    }
}