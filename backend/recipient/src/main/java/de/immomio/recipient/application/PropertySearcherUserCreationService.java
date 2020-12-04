package de.immomio.recipient.application;

import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.model.repository.core.propertysearcher.customer.BasePropertySearcherCustomerRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserProfileRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserRepository;
import de.immomio.service.propertysearcher.AbstractPropertySearcherUserCreationService;
import de.immomio.service.propertysearcher.PropertySearcherSearchUntilCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertySearcherUserCreationService extends AbstractPropertySearcherUserCreationService {

    private final BasePropertySearcherCustomerRepository customerRepository;

    @Autowired
    public PropertySearcherUserCreationService(BasePropertySearcherUserRepository userRepository,
            BasePropertySearcherUserProfileRepository userProfileRepository,
            BasePropertySearcherCustomerRepository customerRepository,
            PropertySearcherSearchUntilCalculationService searchUntilCalculationService) {
        super(userRepository, userProfileRepository, searchUntilCalculationService);
        this.customerRepository = customerRepository;
    }

    @Override
    protected PropertySearcherCustomer saveCustomer(PropertySearcherCustomer customer) {
        return customerRepository.save(customer);
    }
}
