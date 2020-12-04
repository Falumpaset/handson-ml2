update landlord.customersettings set contract_customer_settings =
    jsonb_build_object(
        'contractDefaultSignerType', customersettings.contract_customer_settings -> 'contractDefaultSignerType',
        'continueContractWhenNotVisitedFlat', customersettings.contract_customer_settings -> 'continueContractWhenNotVisitedFlat',
        'contractContactInfo', customersettings.contract_customer_settings -> 'contractContactInfo',
        'mappingFields', jsonb_build_array(
            jsonb_build_object('type', 'SIGNATURE', 'label', 'unterschrift', 'mandatory', true, 'width', null, 'font', null, 'fontSize', null),
            jsonb_build_object('type', 'DATE', 'label', 'datum', 'mandatory', false, 'width', null, 'font', 'Arial', 'fontSize', 'Size12'),
            jsonb_build_object('type', 'TEXT', 'label', 'ort', 'mandatory', true, 'width', '70', 'font', 'Arial', 'fontSize', 'Size12'),
            jsonb_build_object('type', 'TEXT', 'label', 'iban', 'mandatory', false, 'width', '70', 'font', 'Arial', 'fontSize', 'Size12'),
            jsonb_build_object('type', 'CHECKBOX', 'label', 'checkbox', 'mandatory', true, 'width', null, 'font', null, 'fontSize', null)
        )
    );

