Drop table landlord.property_custom_question;
Alter table shared.custom_question Drop column importance;
create table shared.property_custom_question
(
    id                 bigint  not null,
    property_id        bigint not null,
    custom_question_id bigint not null,
    importance         bigint not null default 0,

    constraint fk_property_custom_question_1 foreign key (property_id)
        REFERENCES landlord.property (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    constraint fk_property_custom_question_2 foreign key (custom_question_id)
        REFERENCES shared.custom_question (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT uq_property_custom_question UNIQUE (property_id, custom_question_id)
);
