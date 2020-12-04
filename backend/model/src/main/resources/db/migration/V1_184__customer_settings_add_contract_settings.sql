alter table landlord.customersettings add column contract_customer_settings jsonb default '{}';

update landlord.customersettings cs1 set id = cs2.row_number
FROM (
       SELECT id, row_number() over () as row_number
       FROM landlord.customersettings
       ORDER BY 1
     ) cs2
WHERE cs1.id = cs2.id;

update landlord.customersettings set id = customer_id;

update landlord.customersettings set contract_customer_settings =
    jsonb_build_object(
        'contractDefaultSignerType', customersettings.contract_first_signer_type,
        'continueContractWhenNotVisitedFlat', customersettings.contract_continue_when_not_visited_flat,
        'contractContactInfo', customersettings.contract_contact_person
    );

alter table landlord.customersettings drop column contract_first_signer_type;
alter table landlord.customersettings drop column contract_continue_when_not_visited_flat;
alter table landlord.customersettings drop column contract_contact_person;

alter table landlord.customersettings drop constraint uq_settings_customer;
alter table landlord.customersettings drop constraint fk_settings_01;
alter table landlord.customersettings drop column customer_id;
