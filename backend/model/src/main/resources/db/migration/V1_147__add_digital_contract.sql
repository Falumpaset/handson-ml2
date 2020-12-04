drop type if exists shared.digital_contract_signature_type cascade;
create type shared.digital_contract_signature_type as enum (
    'AES_MAIL',
    'AES_OFFICE',
    'ES_MAIL',
    'ES_OFFICE'
    );

drop type if exists shared.digital_contract_history_state cascade;
create type shared.digital_contract_history_state as enum (
    'INTERNAL_CREATED',
    'DOCUSIGN_CREATED',
    'DOCUSIGN_CREATE_FAILED',
    'INTERNAL_UPDATED',
    'DOCUSIGN_UPDATED',
    'DOCUSIGN_UPDATE_FAILED',
    'DOCUSIGN_SENT',
    'DOCUSIGN_COMPLETED',
    'INTERNAL_CANCELED'
    );

drop type if exists shared.digital_contract_signer_type cascade;
create type shared.digital_contract_signer_type as enum (
    'LANDLORD',
    'TENANT'
    );

drop type if exists shared.digital_contract_type cascade;
create type shared.digital_contract_type as enum (
    'RENTAL',
    'SALE'
    );

drop type if exists shared.digital_contract_signer_history_state cascade;
create type shared.digital_contract_signer_history_state as enum (
    'DOCUSIGN_CREATED',
    'INTERNAL_MAIL_SENT',
    'INTERNAL_FLAT_VISITED_UPDATED',
    'INTERNAL_STOPPED_BY_PS',
    'DOCUSIGN_SENT',
    'DOCUSIGN_SIGNED',
    'DOCUSIGN_DECLINED',
    'DOCUSIGN_FAXPENDING',
    'DOCUSIGN_AUTORESPONDED',
    'DOCUSIGN_DELIVERED',
    'DOCUSIGN_VOIDED',
    'DOCUSIGN_COMPLETED'
    );

create table shared.digital_contract
(
    id                   bigint not null,
    signature_type       shared.digital_contract_signature_type,
    property_data        jsonb  not null,
    document_file        jsonb  not null,
    docusign_envelope_id uuid,
    first_signer_type    shared.digital_contract_signer_type,
    contract_type        shared.digital_contract_type,
    customer_id          bigint not null,
    external_id          varchar(100),
    application_id       bigint,
    created              timestamp,
    updated              timestamp,
    created_by           varchar(100),
    updated_by           varchar(100),
    constraint pk_digital_contract PRIMARY KEY (id),
    constraint fk_digital_contract_application foreign key (application_id) references shared.application match simple
        on update cascade on delete set null,
    constraint fk_digital_contract_customer foreign key (customer_id) references landlord.customer match simple
        on update cascade on delete cascade
);

create table shared.digital_contract_history
(
    id                  bigint not null,
    digital_contract_id bigint not null,
    state               shared.digital_contract_history_state,
    docusign_response   text,
    created             timestamp,
    updated             timestamp,
    created_by          varchar(100),
    updated_by          varchar(100),
    constraint pk_digital_contract_history PRIMARY KEY (id),
    constraint fk_dc_history_digital_contract foreign key (digital_contract_id) references shared.digital_contract
        match simple on update cascade on delete cascade
);

create table shared.digital_contract_signer
(
    id                           bigint                              not null,
    digital_contract_id          bigint                              not null,
    docusign_recipient_client_id uuid,
    data                         jsonb,
    type                         shared.digital_contract_signer_type not null,
    created                      timestamp,
    updated                      timestamp,
    created_by                   varchar(100),
    updated_by                   varchar(100),
    flat_visited                 timestamp,
        constraint pk_digital_contract_signer PRIMARY KEY (id),
    constraint fk_dc_signer_digital_contract foreign key (digital_contract_id)
        references shared.digital_contract (id) match simple
        on update cascade on delete cascade
);

create table shared.digital_contract_signer_history
(
    id         bigint not null,
    signer_id  bigint not null,
    state      shared.digital_contract_signer_history_state,
    created    timestamp,
    updated    timestamp,
    created_by varchar(100),
    updated_by varchar(100),
    constraint pk_digital_contract_signer_history PRIMARY KEY (id),
    constraint fk_dc_signer_history_signer foreign key (signer_id) references shared.digital_contract_signer
        match simple on update cascade on delete cascade
);
