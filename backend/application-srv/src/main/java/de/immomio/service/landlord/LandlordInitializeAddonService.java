package de.immomio.service.landlord;

import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.model.repository.service.landlord.customer.LandlordCustomerRepository;
import de.immomio.service.contract.AdminDigitalContractUserService;
import de.immomio.service.landlord.selfdisclosure.SelfDisclosureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Niklas Lindemann
 */
@Service
public class LandlordInitializeAddonService {

    private SelfDisclosureService selfDisclosureService;

    private AdminDigitalContractUserService digitalContractUserService;
    private final LandlordCustomerRepository customerRepository;

    @Autowired
    public LandlordInitializeAddonService(SelfDisclosureService selfDisclosureService,
            AdminDigitalContractUserService digitalContractUserService,
            LandlordCustomerRepository customerRepository) {
        this.selfDisclosureService = selfDisclosureService;
        this.digitalContractUserService = digitalContractUserService;
        this.customerRepository = customerRepository;
    }

    public void initializeAddons(LandlordCustomer customer, Set<AddonType> addonTypes) {
        if (addonTypes.contains(AddonType.SELF_DISCLOSURE)) {
            selfDisclosureService.create(customer);
        }
        if (addonTypes.contains(AddonType.DIGITAL_CONTRACT)) {
            digitalContractUserService.createDigitalContractUserIfNotExists(customer);
        }

    }

}
