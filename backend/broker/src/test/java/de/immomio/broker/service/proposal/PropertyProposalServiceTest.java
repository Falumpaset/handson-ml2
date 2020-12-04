package de.immomio.broker.service.proposal;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import de.immomio.broker.AbstractTest;
import de.immomio.broker.service.BrokerPropertyCountRefreshCacheService;
import de.immomio.broker.service.CustomQuestionResponseService;
import de.immomio.broker.service.reporting.BrokerElasticsearchIndexingSenderService;
import de.immomio.broker.utils.TestHelper;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfile;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposalState;
import de.immomio.model.repository.core.propertysearcher.searchprofile.BasePropertySearcherSearchProfileRepository;
import de.immomio.model.repository.core.shared.priosetCustomQuestion.BasePriosetCustomQuestionAssociationRepository;
import de.immomio.model.repository.core.shared.propertyProposal.BasePropertyProposalRepository;
import de.immomio.service.calculator.CalculationExecutor;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Freddy Sawma.
 */
public class PropertyProposalServiceTest extends AbstractTest {

    @Mock
    private BasePropertyProposalRepository proposalRepository;

    @Mock
    private CalculationExecutor calculationExecutor;

    @Mock
    private BasePropertySearcherSearchProfileRepository searchProfileRepository;

    @Mock
    private PropertyRequirementService requirementService;

    @Mock
    private CustomQuestionResponseService customQuestionResponseService;

    @Mock
    private BasePriosetCustomQuestionAssociationRepository basePropertyCustomQuestionAssociationRepository;

    @Mock
    private BrokerPropertyCountRefreshCacheService propertyCacheCountService;

    @Mock
    private ProposalOfferService proposalOfferService;

    @Spy
    @InjectMocks
    private PropertyProposalService propertyProposalService;

    @Mock
    private BrokerElasticsearchIndexingSenderService elasticsearchIndexingService;


    @Test
    public void generateOrUpdateProposal() {
        PropertySearcherSearchProfile searchProfile = TestHelper.generatePropertySearcherSearchProfile();
        PropertySearcherUserProfile userProfile = searchProfile.getUserProfile();
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer());
        PropertyProposal propertyProposal = TestHelper.generatePropertyProposal();

        when(proposalRepository.findByUserProfileAndProperty(any(PropertySearcherUserProfile.class), any(Property.class))).thenReturn(propertyProposal);
        propertyProposalService.generateOrUpdateProposal(property, searchProfile);

        verify(calculationExecutor, times(1)).calculateScore(eq(property), eq(userProfile), anyList(), anyList(), anyList());
        verify(customQuestionResponseService, times(1)).getCustomQuestionResponses(eq(property), eq(userProfile));
        verify(basePropertyCustomQuestionAssociationRepository, times(1)).findByPrioset(eq(property.getPrioset()));
    }

    @Test
    public void matchForProposalsWhenPointIsNull() {
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer());
        Address address = property.getData().getAddress();
        address.setCoordinates(null);
        propertyProposalService.matchForProposals(property);

        verify(requirementService, never()).propertyMeetsRequirementsForCreatingProposal(any(Property.class), any(PropertySearcherSearchProfile.class));
    }

    @Test
    public void matchForProposalsWhenPointNotNull() {
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer());
        Coordinate coordinates = new Coordinate(property.getData().getAddress().getCoordinates().getLongitude(),
        property.getData().getAddress().getCoordinates().getLatitude());
        Point point = new GeometryFactory(new PrecisionModel(), 4326).createPoint(coordinates);

        when(searchProfileRepository.customFindNearestToPoint(point.getY(), point.getX())).
                thenReturn(TestHelper.generatePropertySearcherSearchProfileList(3));

        propertyProposalService.matchForProposals(property);

        verify(requirementService, times(3)).propertyMeetsRequirementsForCreatingProposal(
                any(Property.class), any(PropertySearcherSearchProfile.class));
    }

    @Test
    public void updateScoreWhenPropertyUpdated1() {
        List<PropertyProposal> proposals = new ArrayList<>();
        PropertyProposal propertyProposal = TestHelper.generatePropertyProposal();
        proposals.add(propertyProposal);
        propertyProposalService.updateScoreWhenPropertyUpdated(proposals);

        verify(calculationExecutor, times(1)).calculateScore(eq(proposals.get(0).getProperty()), eq(proposals.get(0).getUserProfile()),  anyList(), anyList(), anyList());
        verify(customQuestionResponseService, times(1)).getCustomQuestionResponses(eq(propertyProposal.getProperty()), eq(propertyProposal.getUserProfile()));
    }

    @Test
    public void proposalShouldBeDeletedWhenStateNotEqual() {
        PropertyProposal propertyProposal = TestHelper.generatePropertyProposal();
        assertNotEquals(propertyProposal.getState(), PropertyProposalState.PROSPECT);

        verify(requirementService, never()).propertyFulfillsSP(eq(
                propertyProposal.getProperty()), any(PropertySearcherSearchProfile.class));
    }

}