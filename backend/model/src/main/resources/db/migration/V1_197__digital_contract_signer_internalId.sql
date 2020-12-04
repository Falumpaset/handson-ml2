alter table shared.digital_contract_signer add column internal_signer_id uuid;

CREATE INDEX internal_signer_id on shared.digital_contract_signer (internal_signer_id);

update shared.digital_contract_signer set internal_signer_id =  uuid_in(md5(random()::text || clock_timestamp()::text)::cstring);