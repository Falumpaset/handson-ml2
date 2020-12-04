create type shared.digital_contract_schufa_state as enum ('SUCCESS', 'SUCCESS_DATA_NEEDS_TO_BE_CONFIRMED', 'SUCCESS_AFTER_CONFIRMATION', 'DATA_NOT_CORRECT_AFTER_CONFIRMATION', 'DATA_NOT_CORRECT_NEEDS_TO_BE_RESTARTED', 'CANCEL' , 'ERROR', 'MAX_SCHUFA_REQUESTS_EXCEEDED');

create table shared.digital_contract_schufa_history
(
    id              bigint not null,
    signer_id       bigint not null,
    created         timestamp,
    updated         timestamp,
    created_by      varchar(100),
    updated_by      varchar(100),
    schufa_request jsonb,
    schufa_response jsonb,
    state           shared.digital_contract_schufa_state,
    constraint pk_digital_contract_aes_schufa_history PRIMARY KEY (id),
    constraint fk_digital_contract_aes_schufa_history_signer foreign key (signer_id) references shared.digital_contract_signer match simple
        on update cascade on delete cascade
);


create index fki_digital_contract_aes_schufa_history_signer on shared.digital_contract_schufa_history (signer_id);


alter table shared.digital_contract_signer rename current_state to current_state_old;
alter table shared.digital_contract_signer add column current_state jsonb;
update shared.digital_contract_signer set current_state = jsonb_build_object('signerState', digital_contract_signer.current_state_old);

alter table shared.digital_contract_signer drop column current_state_old;
alter table shared.digital_contract drop column automation_level;
drop type shared.digital_contract_automation_level;