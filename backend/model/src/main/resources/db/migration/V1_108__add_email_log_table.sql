CREATE TYPE propertysearcher.maileventtype AS ENUM ('PROFILE_COMPLETION');


CREATE TABLE propertysearcher.email_log
(
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    event_type propertysearcher.maileventtype,
    created TIMESTAMP,
    updated TIMESTAMP,

    CONSTRAINT email_log_pk PRIMARY KEY (id),
    CONSTRAINT fk_email_log FOREIGN KEY (user_id) REFERENCES
    propertysearcher.user (id) MATCH SIMPLE
);

create unique index email_log_user_id_event_type_uindex on propertysearcher.email_log (user_id, event_type);
