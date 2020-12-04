package de.immomio.service.landlord.listener;

import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.landlord.bean.customer.settings.DigitalContractCustomerSettings;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.model.repository.service.landlord.customer.LandlordCustomerSettingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RepositoryEventHandler
public class LandlordCustomerListener {

    private final LandlordCustomerSettingsRepository landlordCustomerSettingsRepository;

    @Autowired
    public LandlordCustomerListener(LandlordCustomerSettingsRepository landlordCustomerSettingsRepository) {
        this.landlordCustomerSettingsRepository = landlordCustomerSettingsRepository;
    }

    @HandleAfterCreate
    public void afterCreatedHandler(LandlordCustomer customer) {
        LandlordCustomerSettings customerSettings = new LandlordCustomerSettings();
        DigitalContractCustomerSettings contractCustomerSettings = DigitalContractCustomerSettings.builder()
                .continueContractWhenNotVisitedFlat(true)
                .contractDefaultSignerType(DigitalContractSignerType.TENANT)
                .build();
        customerSettings.setContractCustomerSettings(contractCustomerSettings);
        customerSettings.setCustomer(customer);

        landlordCustomerSettingsRepository.save(customerSettings);
    }
}
