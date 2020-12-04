package de.immomio.landlord.listener.customer;

import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.base.type.customer.settings.DigitalContractMappingFieldType;
import de.immomio.data.landlord.bean.customer.settings.DigitalContractCustomerSettings;
import de.immomio.data.landlord.bean.customer.settings.DigitalContractMappingField;
import de.immomio.data.landlord.bean.customer.settings.DigitalContractMappingFields;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.model.repository.landlord.customer.LandlordCustomerSettingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RepositoryEventHandler
public class LandlordCustomerListener {

    private static final String LABEL_SIGNATURE = "unterschrift";
    private static final String LABEL_DATE = "datum";
    private static final String FONT_ARIAL = "Arial";
    private static final String FONT_SIZE_12 = "Size12";
    private static final String LABEL_IBAN = "iban";
    private static final String LABEL_CITY = "ort";
    private static final String FIELD_WIDTH = "70";
    private static final String LABEL_CHECKBOX = "checkbox";
    private final LandlordCustomerSettingsRepository landlordCustomerSettingsRepository;

    @Autowired
    public LandlordCustomerListener(LandlordCustomerSettingsRepository landlordCustomerSettingsRepository) {
        this.landlordCustomerSettingsRepository = landlordCustomerSettingsRepository;
    }

    @HandleAfterCreate
    public void afterCreatedHandler(LandlordCustomer customer) {
        LandlordCustomerSettings customerSettings = new LandlordCustomerSettings();
        customerSettings.setCustomer(customer);
        customerSettings.setDeleteDkLevelAfterRenting(true);
        DigitalContractCustomerSettings contractCustomerSettings = DigitalContractCustomerSettings.builder()
                .continueContractWhenNotVisitedFlat(true)
                .contractDefaultSignerType(DigitalContractSignerType.TENANT)
                .build();
        contractCustomerSettings.setMappingFields(getDefaultFields());
        customerSettings.setContractCustomerSettings(contractCustomerSettings);

        landlordCustomerSettingsRepository.save(customerSettings);
    }

    private DigitalContractMappingFields getDefaultFields() {
        DigitalContractMappingFields fields = new DigitalContractMappingFields();
        DigitalContractMappingField field = DigitalContractMappingField.builder()
                .type(DigitalContractMappingFieldType.SIGNATURE)
                .label(LABEL_SIGNATURE)
                .mandatory(true)
                .build();
        fields.add(field);
        field = DigitalContractMappingField.builder()
                .type(DigitalContractMappingFieldType.DATE)
                .label(LABEL_DATE)
                .mandatory(false)
                .font(FONT_ARIAL)
                .fontSize(FONT_SIZE_12)
                .build();
        fields.add(field);
        field = DigitalContractMappingField.builder()
                .type(DigitalContractMappingFieldType.TEXT)
                .label(LABEL_CITY)
                .mandatory(false)
                .width(FIELD_WIDTH)
                .font(FONT_ARIAL)
                .fontSize(FONT_SIZE_12)
                .build();
        fields.add(field);
        field = DigitalContractMappingField.builder()
                .type(DigitalContractMappingFieldType.TEXT)
                .label(LABEL_IBAN)
                .mandatory(false)
                .width(FIELD_WIDTH)
                .font(FONT_ARIAL)
                .fontSize(FONT_SIZE_12)
                .build();
        fields.add(field);
        field = DigitalContractMappingField.builder()
                .type(DigitalContractMappingFieldType.CHECKBOX)
                .label(LABEL_CHECKBOX)
                .mandatory(true)
                .build();
        fields.add(field);

        return fields;
    }
}
