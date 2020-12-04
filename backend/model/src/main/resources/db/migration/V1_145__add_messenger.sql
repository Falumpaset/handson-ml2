create table shared.conversation
(
    id             bigint       not null,
    external_id    varchar(100) not null,
    application_id bigint not null,
    blocked boolean not null default false,
    created        timestamp,
    updated        timestamp,
    constraint pk_conversation PRIMARY KEY (id),
    constraint fk_conversation_1 foreign key (application_id) REFERENCES shared.application MATCH SIMPLE
        ON UPDATE CASCADE on delete cascade
);

create unique index fki_conversation_1 on shared.conversation (application_id);

CREATE TYPE shared.message_sender AS ENUM ('PROPERTYSEARCHER', 'LANDLORD');
CREATE TYPE shared.message_source AS ENUM ('APP', 'EMAIL');

create table shared.conversation_message
(
    id              bigint                not null,
    conversation_id bigint                not null,
    message         text                  not null,
    sender          shared.message_sender not null,
    source          shared.message_source not null,
    agent_info      jsonb,
    attachments     jsonb,
    created        timestamp,
    updated        timestamp,
    read boolean not null default false,
    constraint pk_conversation_message PRIMARY KEY (id),
    constraint fk_conversation_message_1 foreign key (conversation_id) references shared.conversation match simple
        on update cascade on delete cascade
);

alter table landlord.property
    add column preferences jsonb not null default '{}'::jsonb;