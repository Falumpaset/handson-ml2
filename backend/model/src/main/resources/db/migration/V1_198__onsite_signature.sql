alter table shared.digital_contract_signer rename column verification_data to aes_verification_data;
alter table shared.digital_contract_signer add column onsite_data_verified timestamp;

alter table shared.digital_contract_signer add column onsite_host boolean not null default false;

alter table shared.digital_contract add column onsite_host_agent_id bigint;

alter table shared.digital_contract
    add constraint fk_onsite_signing_agent foreign key (onsite_host_agent_id) references landlord.user (id) on update cascade on delete set null ;
