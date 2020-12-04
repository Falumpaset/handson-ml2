package de.immomio.broker.service;

import de.immomio.broker.service.application.PropertyApplicationNotificationService;
import de.immomio.broker.service.application.PropertyApplicationService;
import de.immomio.broker.service.property.cache.PropertyCountRefreshCacheService;
import de.immomio.broker.service.property.tenant.AbstractTenantSelect;
import de.immomio.broker.service.proposal.PropertyProposalService;
import de.immomio.constants.exceptions.RejectAllException;
import de.immomio.data.base.type.property.TenantSelectType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PropertyTenantProcessService {

    private final PropertyCountRefreshCacheService refreshApplicationCountsCache;

    private final PropertyProposalService propertyProposalService;

    private final PropertyService propertyService;

    private final PropertyApplicationService applicationService;

    private final SelfDisclosureService selfDisclosureService;

    private final PropertyApplicationNotificationService propertyApplicationNotificationService;

    @Autowired
    private List<AbstractTenantSelect> tenantSelects = new ArrayList<>();

    @Autowired
    public PropertyTenantProcessService(
            PropertyCountRefreshCacheService refreshApplicationCountsCache,
            PropertyProposalService propertyProposalService,
            PropertyService propertyService,
            PropertyApplicationService applicationService,
            SelfDisclosureService selfDisclosureService,
            PropertyApplicationNotificationService propertyApplicationNotificationService
    ) {
        this.refreshApplicationCountsCache = refreshApplicationCountsCache;
        this.propertyProposalService = propertyProposalService;
        this.propertyService = propertyService;
        this.applicationService = applicationService;
        this.selfDisclosureService = selfDisclosureService;
        this.propertyApplicationNotificationService = propertyApplicationNotificationService;
    }

    public void processRenting(
            PropertyApplication application,
            PropertyTenant propertyTenant,
            Boolean rejectAll
    ) throws RejectAllException {
        final Property property = application.getProperty();
        final PropertySearcherUserProfile userProfile = application.getUserProfile();
        final LandlordCustomer customer = property.getCustomer();

        propertyApplicationNotificationService.propertyAccepted(userProfile, property);
        selectTenant(property.getCustomer(), propertyTenant);

        if (customer.isSelfDisclosureAllowed()) {
            selfDisclosureService.sendSelfDisclosureFeedbackEmail(property, userProfile);
        }

        if (BooleanUtils.isTrue(rejectAll)) {
            applicationService.rejectRemainingApplicants(property, userProfile);
        }

        propertyProposalService.cleanUpProposalsAfterPropertyRented(property);
        refreshApplicationCache(application.getProperty());

        removeDkLevels(application);
        propertyService.disableAutoOffer(property);
    }

    public void processRenting(
            Property property,
            PropertyTenant propertyTenant,
            Boolean rejectAll
    ) throws RejectAllException {
        log.info("process renting");
        selectTenant(property.getCustomer(), propertyTenant);
        propertyProposalService.cleanUpProposalsAfterPropertyRented(property);
        refreshApplicationCache(property);

        if (BooleanUtils.isTrue(rejectAll)) {
            applicationService.rejectRemainingApplicants(property, null);
        }
        removeDkLevels(property);
        propertyService.disableAutoOffer(property);
    }

    private void removeDkLevels(Property property) {
        LandlordCustomerSettings customerSettings = property.getCustomer().getCustomerSettings();
        if (customerSettings != null && BooleanUtils.isTrue(customerSettings.getDeleteDkLevelAfterRenting())) {
            applicationService.removeDkLevels(property, null);
        }
    }

    private void removeDkLevels(PropertyApplication application) {
        LandlordCustomerSettings customerSettings = application.getProperty().getCustomer().getCustomerSettings();
        if (customerSettings != null && BooleanUtils.isTrue(customerSettings.getDeleteDkLevelAfterRenting())) {
            applicationService.removeDkLevels(application.getProperty(), application.getUserProfile());
        }
    }

    private void selectTenant(LandlordCustomer customer, PropertyTenant propertyTenant) {
        if (customer.getCustomerSettings() != null && customer.getCustomerSettings().getTenantSelectType() != null) {
            boolean result = runSelectTenantLogic(customer, propertyTenant);
            if (!result) {
                log.error("Error running SelectTenantLogic for PropertyTenant " + propertyTenant.getId());
            }
        }
    }

    private boolean runSelectTenantLogic(LandlordCustomer customer, PropertyTenant propertyTenant) {
        TenantSelectType tenantSelectType = customer.getCustomerSettings().getTenantSelectType();

        for (AbstractTenantSelect tenantSelect : tenantSelects) {
            if (tenantSelect.isApplicable(tenantSelectType)) {
                return tenantSelect.run(propertyTenant);
            }
        }
        return false;
    }

    private void refreshApplicationCache(Property property) {
        refreshApplicationCountsCache.refreshApplicationCache(property);
    }

}
