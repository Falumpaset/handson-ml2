alter table landlord.customersettings
    add column contract_first_signer_type shared.digital_contract_signer_type default 'TENANT';
alter table landlord.customersettings
    add column contract_continue_when_not_visited_flat boolean default true;
alter table landlord.customersettings
    add column contract_contact_person jsonb default '{}';

alter table shared.digital_contract
    add column user_id bigint;
alter table shared.digital_contract
    add constraint fk_digital_contract_user foreign key (user_id) references landlord."user" (id)
        on update cascade
        on delete set null;

create index fki_digital_contract_application on shared.digital_contract (application_id);
create index fki_digital_contract_customer on shared.digital_contract (customer_id);
create index fki_digital_contract_user on shared.digital_contract (user_id);

create index fki_digital_contract_history_contract on shared.digital_contract_history (digital_contract_id);
