create table shared.appointment_invitation
(
    id             bigint                                                                                not null
        constraint appointment_invitation_pkey
            primary key,
    appointment_id bigint                                                                                not null
        constraint fk_appointment_invitation_01
            references shared.appointment
            on update cascade on delete cascade,
    created        timestamp,
    updated        timestamp,
    application_id bigint                            default 1                                           not null
        constraint fk_appointment_invitation_02
            references shared.application
            on update cascade on delete cascade,
    email_sent     boolean                           default false
);

create index fki_fk_appointment_invitation_01
    on shared.appointment_invitation (application_id);

create index fki_fk_appointment_invitation_02
    on shared.appointment_invitation (appointment_id);

create index fki_fk_appointment_invitation_03
    on shared.appointment_invitation (appointment_id, application_id);

create unique index uq_appointment_invitation_1
    on shared.appointment_invitation (appointment_id, application_id);

alter table shared.appointment
    add column exclusive boolean not null default false;

