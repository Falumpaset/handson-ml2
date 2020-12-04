create type shared.digital_contract_itp_state as enum ('INIT', 'FAILED', 'UPLOADED', 'PENDING', 'ACCEPTED', 'UNKNOWN', 'TECHNICAL_ERROR');

create table shared.digital_contract_itp_history
(
    id              bigint not null,
    signer_id       bigint not null,
    created         timestamp,
    updated         timestamp,
    created_by      varchar(100),
    updated_by      varchar(100),
    itp_request jsonb,
    itp_response jsonb,
    state           shared.digital_contract_itp_state,
    constraint pk_digital_contract_aes_itp_history PRIMARY KEY (id),
    constraint fk_digital_contract_aes_itp_history_signer foreign key (signer_id) references shared.digital_contract_signer match simple
        on update cascade on delete cascade
);


create index fki_digital_contract_aes_itp_history_signer on shared.digital_contract_itp_history (signer_id);

alter table shared.digital_contract_signer add column verification_data jsonb default '{"aesCode": "dummy123"}';
