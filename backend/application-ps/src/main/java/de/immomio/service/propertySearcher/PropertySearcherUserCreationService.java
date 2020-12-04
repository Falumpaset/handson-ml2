package de.immomio.service.propertySearcher;

import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.model.repository.propertysearcher.customer.PropertySearcherCustomerRepository;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserProfileRepository;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserRepository;
import de.immomio.service.propertysearcher.AbstractPropertySearcherUserCreationService;
import de.immomio.service.propertysearcher.PropertySearcherSearchUntilCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertySearcherUserCreationService extends AbstractPropertySearcherUserCreationService {
    private final PropertySearcherCustomerRepository customerRepository;

    @Autowired
    public PropertySearcherUserCreationService(PropertySearcherCustomerRepository customerRepository,
            PropertySearcherUserRepository userRepository,
            PropertySearcherUserProfileRepository userProfileRepository,
            PropertySearcherSearchUntilCalculationService searchUntilCalculationService) {
        super(userRepository, userProfileRepository, searchUntilCalculationService);
        this.customerRepository = customerRepository;
    }

    @Override
    protected PropertySearcherCustomer saveCustomer(PropertySearcherCustomer customer) {
        return customerRepository.customSave(customer);
    }
}
