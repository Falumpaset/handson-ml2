package de.immomio.service.propertySearcher.customer;

import de.immomio.constants.exceptions.DeleteException;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.model.repository.propertysearcher.customer.PropertySearcherCustomerRepository;
import de.immomio.security.service.KeycloakService;
import de.immomio.service.property.cache.PropertyCountRefreshCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PropertySearcherCustomerService {

    private static final String USER_DELETE_EXCEPTION = "USER_DELETE_EXCEPTION_L";

    private final PropertySearcherCustomerRepository customerRepository;
    private final PropertyCountRefreshCacheService countRefreshCacheService;
    private final KeycloakService keycloakService;

    @Autowired
    public PropertySearcherCustomerService(PropertySearcherCustomerRepository customerRepository,
            PropertyCountRefreshCacheService countRefreshCacheService,
            KeycloakService keycloakService) {
        this.customerRepository = customerRepository;
        this.countRefreshCacheService = countRefreshCacheService;
        this.keycloakService = keycloakService;
    }

    public void delete(PropertySearcherUserProfile userProfile) {
        List<Property> properties = new ArrayList<>();
        try {
            String email = userProfile.getEmail();
            properties = userProfile.getPropertyProposals().stream().map(PropertyProposal::getProperty).collect(Collectors.toList());

            properties.addAll(userProfile.getApplications().stream().map(PropertyApplication::getProperty).collect(Collectors.toList()));

            customerRepository.customDelete(userProfile.getUser().getCustomer());

            if (keycloakService.userExists(email)) {
                keycloakService.removeUser(email);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DeleteException(USER_DELETE_EXCEPTION);
        } finally {
            countRefreshCacheService.refreshPropertyCaches(properties);
        }
    }
}
