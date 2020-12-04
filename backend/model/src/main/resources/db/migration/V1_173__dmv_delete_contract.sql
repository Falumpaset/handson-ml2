alter table shared.digital_contract add column internal_contract_id uuid;
alter table shared.digital_contract alter internal_contract_id type uuid using docusign_envelope_id;