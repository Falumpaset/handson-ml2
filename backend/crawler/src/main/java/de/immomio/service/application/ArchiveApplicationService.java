package de.immomio.service.application;

import de.immomio.constants.exceptions.ImmomioRuntimeException;
import de.immomio.data.base.type.customer.settings.LandlordCustomerApplicationArchiveUnit;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import de.immomio.model.repository.core.landlord.customer.BaseLandlordCustomerRepository;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import de.immomio.model.repository.core.shared.application.BasePropertyApplicationRepository;
import de.immomio.service.CrawlerPropertyCountRefreshCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class ArchiveApplicationService {
    private BaseLandlordCustomerRepository customerRepository;
    private BasePropertyRepository propertyRepository;
    private BasePropertyApplicationRepository applicationRepository;
    private CrawlerPropertyCountRefreshCacheService crawlerPropertyCountRefreshCacheService;

    @Autowired
    public ArchiveApplicationService(
            BaseLandlordCustomerRepository customerRepository,
            BasePropertyRepository propertyRepository,
            BasePropertyApplicationRepository applicationRepository,
            CrawlerPropertyCountRefreshCacheService crawlerPropertyCountRefreshCacheService
    ) {
        this.customerRepository = customerRepository;
        this.propertyRepository = propertyRepository;
        this.applicationRepository = applicationRepository;
        this.crawlerPropertyCountRefreshCacheService = crawlerPropertyCountRefreshCacheService;
    }

    public void archiveApplications() {
        List<LandlordCustomer> customers = customerRepository.findArchiveApplicationsActive();

        customers.forEach(customer -> {
            Date archiveTimestamp;
            try {
                archiveTimestamp = getArchiveTimestamp(customer);
            } catch (ImmomioRuntimeException e) {
                log.info("customer " + customer.getName() + " configs not configured");
                return;
            }

            List<Property> properties = propertyRepository.findForApplicationArchiving(customer, archiveTimestamp);
            properties.forEach(property -> {
                PropertyTenant tenant = property.getTenant();
                if (tenant.getUserProfile() != null) {
                    applicationRepository.archiveApplication(property, tenant.getUserProfile());
                } else {
                    applicationRepository.archiveApplication(property);
                }
                crawlerPropertyCountRefreshCacheService.refreshApplicationCache(property);
            });
        });
    }

    private Date getArchiveTimestamp(LandlordCustomer customer) {
        LandlordCustomerSettings customerSettings = customer.getCustomerSettings();
        long millis;
        Integer applicationArchiveUnitAmount = customerSettings.getApplicationArchiveUnitAmount();
        LandlordCustomerApplicationArchiveUnit applicationArchiveUnit = customerSettings.getApplicationArchiveUnit();
        if (applicationArchiveUnit == null || applicationArchiveUnitAmount == null) {
            throw new ImmomioRuntimeException("values not configured");
        } else {
            millis = applicationArchiveUnit.getMillis() * applicationArchiveUnitAmount;
        }
        Date now = new Date();

        return new Date(now.getTime() - millis);
    }

}
