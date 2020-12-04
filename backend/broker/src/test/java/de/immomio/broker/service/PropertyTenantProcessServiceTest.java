package de.immomio.broker.service;

import de.immomio.broker.AbstractTest;
import de.immomio.broker.service.application.PropertyApplicationNotificationService;
import de.immomio.broker.service.application.PropertyApplicationService;
import de.immomio.broker.service.property.cache.PropertyCountRefreshCacheService;
import de.immomio.broker.service.proposal.PropertyProposalService;
import de.immomio.broker.utils.TestHelper;
import de.immomio.constants.exceptions.RejectAllException;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PropertyTenantProcessServiceTest extends AbstractTest {

    @Spy
    @InjectMocks
    private PropertyTenantProcessService propertyTenantProcessService;

    @Mock
    private PropertyApplicationService applicationService;

    @Mock
    private PropertyProposalService propertyProposalService;

    @Mock
    private PropertyCountRefreshCacheService refreshApplicationCountsCache;

    @Mock
    private PropertyService propertyService;

    @Mock
    private PropertyApplicationNotificationService propertyApplicationNotificationService;

    @Mock
    private SelfDisclosureService selfDisclosureService;

    private LandlordCustomer landlordCustomer;

    @Before
    public void initializeData() {
        landlordCustomer = TestHelper.generateLandlordCustomer();
    }

    @Test
    public void propertyRentProcessingWithRejectAll() throws RejectAllException {
        Property property = TestHelper.generateProperty(landlordCustomer);
        PropertySearcherUserProfile userProfile = TestHelper.generatePropertySearcherUser();
        PropertyTenant propertyTenant = TestHelper.generatePropertyTenant(property, userProfile);

        doNothing().when(refreshApplicationCountsCache).refreshApplicationCache(property);
        doNothing().when(propertyService).disableAutoOffer(property);
        doNothing().when(applicationService).rejectRemainingApplicants(property, null);

        propertyTenantProcessService.processRenting(property, propertyTenant, true);

        verify(propertyProposalService, times(1)).cleanUpProposalsAfterPropertyRented(property);
        verify(refreshApplicationCountsCache, times(1)).refreshApplicationCache(property);
        verify(propertyService, times(1)).disableAutoOffer(property);
        verify(applicationService, times(1)).rejectRemainingApplicants(property, null);
    }

    @Test
    public void propertyRentProcessingWithoutRejectAll() throws RejectAllException {
        Property property = TestHelper.generateProperty(landlordCustomer);
        PropertySearcherUserProfile userProfile = TestHelper.generatePropertySearcherUser();
        PropertyTenant propertyTenant = TestHelper.generatePropertyTenant(property, userProfile);

        doNothing().when(refreshApplicationCountsCache).refreshApplicationCache(property);
        doNothing().when(propertyService).disableAutoOffer(property);
        doNothing().when(applicationService).rejectRemainingApplicants(property, null);

        propertyTenantProcessService.processRenting(property, propertyTenant, false);

        verify(propertyProposalService, times(1)).cleanUpProposalsAfterPropertyRented(property);
        verify(refreshApplicationCountsCache, times(1)).refreshApplicationCache(property);
        verify(propertyService, times(1)).disableAutoOffer(property);
        verify(applicationService, times(0)).rejectRemainingApplicants(property, null);
    }

    @Test
    public void propertyApplicationRentProcessingWithRejectAll() throws RejectAllException {
        PropertyApplication application = TestHelper.generateApplication(ApplicationStatus.UNANSWERED, Portal.EBAY);
        Property property = application.getProperty();
        PropertySearcherUserProfile userProfile = TestHelper.generatePropertySearcherUser();
        PropertyTenant propertyTenant = TestHelper.generatePropertyTenant(property, userProfile);

        doNothing().when(propertyApplicationNotificationService).propertyAccepted(any(), any());
        doNothing().when(refreshApplicationCountsCache).refreshApplicationCache(property);
        doNothing().when(propertyService).disableAutoOffer(property);
        doNothing().when(applicationService).rejectRemainingApplicants(property, null);
        doNothing().when(selfDisclosureService).sendSelfDisclosureFeedbackEmail(any(), any());

        propertyTenantProcessService.processRenting(application, propertyTenant, true);

        verify(propertyApplicationNotificationService, times(1)).propertyAccepted(any(), any());
        verify(propertyProposalService, times(1)).cleanUpProposalsAfterPropertyRented(property);
        verify(refreshApplicationCountsCache, times(1)).refreshApplicationCache(property);
        verify(propertyService, times(1)).disableAutoOffer(property);
        verify(applicationService, times(1)).rejectRemainingApplicants(property, application.getUserProfile());
        verify(selfDisclosureService, times(0)).sendSelfDisclosureFeedbackEmail(any(), any());
    }

    @Test
    public void propertyApplicationRentProcessingWithoutRejectAll() throws RejectAllException {
        PropertyApplication application = TestHelper.generateApplication(ApplicationStatus.UNANSWERED, Portal.EBAY);
        Property property = application.getProperty();
        PropertySearcherUserProfile userProfile = TestHelper.generatePropertySearcherUser();
        PropertyTenant propertyTenant = TestHelper.generatePropertyTenant(property, userProfile);

        doNothing().when(propertyApplicationNotificationService).propertyAccepted(any(), any());
        doNothing().when(refreshApplicationCountsCache).refreshApplicationCache(property);
        doNothing().when(propertyService).disableAutoOffer(property);
        doNothing().when(applicationService).rejectRemainingApplicants(property, null);
        doNothing().when(selfDisclosureService).sendSelfDisclosureFeedbackEmail(any(), any());

        propertyTenantProcessService.processRenting(application, propertyTenant, false);

        verify(propertyApplicationNotificationService, times(1)).propertyAccepted(any(), any());
        verify(propertyProposalService, times(1)).cleanUpProposalsAfterPropertyRented(property);
        verify(refreshApplicationCountsCache, times(1)).refreshApplicationCache(property);
        verify(propertyService, times(1)).disableAutoOffer(property);
        verify(applicationService, times(0)).rejectRemainingApplicants(property, application.getUserProfile());
        verify(selfDisclosureService, times(0)).sendSelfDisclosureFeedbackEmail(any(), any());
    }
}
