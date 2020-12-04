create type landlord.followup_state as enum ('PROCESSED','UNPROCESSED');

create table landlord.followup
(
    id          bigint                  not null,
    property_id bigint                  not null,
    date        timestamp               not null,
    created     timestamp               not null,
    updated     timestamp               not null,
    state       landlord.followup_state not null,
    reason      text,
    constraint pk_followup primary key (id),
    constraint fk_followup_property foreign key (property_id) references landlord.property (id) on update cascade on delete cascade
);

create index fki_followup_property on landlord.followup (property_id);

create table landlord.followup_notification
(
    id          bigint    not null,
    followup_id bigint    not null,
    date        timestamp not null,
    created     timestamp not null,
    updated     timestamp not null,
    constraint pk_followup_notification primary key (id),
    constraint fk_followup_notification_followup foreign key (followup_id) references landlord.followup (id) on update cascade on delete cascade
);

create index fki_followup_notification_followup on landlord.followup_notification (followup_id);

create table landlord.usersettings
(
    id            bigint    not null,
    followup_data jsonb,
    created       timestamp not null,
    updated       timestamp not null,
    constraint pk_usersettings primary key (id)
);