package de.immomio.service.shared.contract;

import com.docusign.esign.model.NewUser;
import de.immomio.data.landlord.entity.contract.DigitalContractApiUser;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.docusign.service.DocuSignUserService;
import de.immomio.model.repository.core.shared.contract.BaseDigitalContractApiUserRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * @author Niklas Lindemann
 */

@Slf4j
public abstract class AbstractDigitalContractUserService<AUR extends BaseDigitalContractApiUserRepository> {
    private final AUR apiUserRepository;

    private final DocuSignUserService docuSignUserService;

    public AbstractDigitalContractUserService(AUR apiUserRepository, DocuSignUserService docuSignUserService) {
        this.apiUserRepository = apiUserRepository;
        this.docuSignUserService = docuSignUserService;
    }

    public NewUser createApiUser(String customerName, Long customerId) {
        return docuSignUserService.createApiUser(customerId);
    }

    public void createDigitalContractUserIfNotExists(LandlordCustomer customer) {
        if (!apiUserRepository.existsByCustomer(customer)) {
            log.info("No api user found for customer {}", customer.getId());
            NewUser newUser = createApiUser(customer.getName(), customer.getId());
            UUID docuSignApiUserId = UUID.fromString(newUser.getUserId());
            DigitalContractApiUser apiUser = new DigitalContractApiUser();
            apiUser.setCustomer(customer);
            apiUser.setDocuSignApiUserId(docuSignApiUserId);
            apiUserRepository.save(apiUser);
        }
    }

}
