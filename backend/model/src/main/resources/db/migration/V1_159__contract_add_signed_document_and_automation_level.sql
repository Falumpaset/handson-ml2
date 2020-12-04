create type shared.digital_contract_automation_level as enum (
    'MANUAL_SIGNATURE_MAPPING',
    'AUTOMATED_SIGNATURE_MAPPING'
    );

alter table shared.digital_contract add column signed_document_file jsonb;

alter table shared.digital_contract add column automation_level shared.digital_contract_automation_level;
