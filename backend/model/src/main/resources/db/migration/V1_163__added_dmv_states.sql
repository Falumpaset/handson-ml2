alter type shared.digital_contract_history_state rename to digital_contract_history_state_old;
create type shared.digital_contract_history_state as enum ('INTERNAL_CREATED',
    'DOCUSIGN_CREATED',
    'DOCUSIGN_CREATE_FAILED', 'INTERNAL_UPDATED', 'DOCUSIGN_UPDATED',
    'DOCUSIGN_UPDATE_FAILED', 'DOCUSIGN_SENT', 'DOCUSIGN_COMPLETED',
    'INTERNAL_CANCELED',
    'INTERNAL_INTERRUPTED', 'DOCUSIGN_UPDATED_AFTER_INTERRUPTED', 'DOCUSIGN_SENT_AFTER_INTERRUPTED');

alter table shared.digital_contract_history
    alter column state TYPE shared.digital_contract_history_state using "state"::text::shared.digital_contract_history_state;

alter table shared.digital_contract
    alter column current_state TYPE shared.digital_contract_history_state using "current_state"::text::shared.digital_contract_history_state;

DROP TYPE shared.digital_contract_history_state_old;