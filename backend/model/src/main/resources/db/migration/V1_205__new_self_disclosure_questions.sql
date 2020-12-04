create type shared.self_disclosure_sub_question_type as enum ('BOOLEAN', 'SELECT', 'CHILD', 'ADDRESS', 'EMPLOYMENT', 'FLAT', 'DOCUMENT');

create table shared.self_disclosure_sub_question
(
    id              bigint                                   not null,
    title           varchar(2048)                            not null,
    comment_allowed boolean                                  not null,
    comment_hint    varchar(2048),
    order_number    int,
    type            shared.self_disclosure_sub_question_type not null,
    created         timestamp without time zone,
    updated         timestamp without time zone,
    CONSTRAINT pk_self_disclosure_sub_question PRIMARY KEY (id)
);

create table shared.self_disclosure_question_sub_question
(
    self_disclosure_question_id     bigint not null,
    self_disclosure_sub_question_id bigint not null,
    constraint uq_self_disclosure_question_sub_question unique (self_disclosure_question_id, self_disclosure_sub_question_id),
    constraint fk_self_disclosure_question_sub_question_1 foreign key (self_disclosure_question_id) references shared.self_disclosure_question (id) match simple on update cascade on delete cascade ,
    constraint fk_self_disclosure_question_sub_question_2 foreign key (self_disclosure_sub_question_id) references shared.self_disclosure_sub_question (id) match simple on update cascade on delete cascade
);

create index fki_self_disclosure_question_sub_question_1 on shared.self_disclosure_question_sub_question (self_disclosure_question_id);
create index fki_self_disclosure_question_sub_question_2 on shared.self_disclosure_question_sub_question (self_disclosure_sub_question_id);


create table shared.self_disclosure_question_configuration
(
    id                          bigint  not null,
    self_disclosure_id          bigint  not null,
    self_disclosure_question_id bigint  not null,
    hidden                      boolean not null,
    mandatory                   boolean not null,
    created                     timestamp without time zone,
    updated                     timestamp without time zone,
    CONSTRAINT pk_self_disclosure_question_configuration PRIMARY KEY (id),
    constraint fk_self_disclosure_question_configuration_1 foreign key (self_disclosure_question_id) references shared.self_disclosure_question (id) match simple on update cascade on delete cascade,
    constraint fk_self_disclosure_question_configuration_2 foreign key (self_disclosure_id) references shared.self_disclosure (id) match simple on update cascade on delete cascade

);

create table shared.self_disclosure_sub_question_configuration
(
    id                                        bigint  not null,
    self_disclosure_question_configuration_id bigint  not null,
    self_disclosure_sub_question_id           bigint  not null,
    hidden                                    boolean not null,
    mandatory                                 boolean not null,
    created                                   timestamp without time zone,
    updated                                   timestamp without time zone,
    CONSTRAINT pk_self_disclosure_sub_question_configuration PRIMARY KEY (id),
    constraint fk_self_disclosure_sub_question_configuration_1 foreign key (self_disclosure_question_configuration_id) references shared.self_disclosure_question_configuration (id) match simple on update cascade on delete cascade,
    constraint fk_self_disclosure_sub_question_configuration_2 foreign key (self_disclosure_sub_question_id) references shared.self_disclosure_sub_question (id) match simple on update cascade on delete cascade
);


insert into shared.self_disclosure_sub_question
values (1, 'SELF_DISCLOSURE_SALUTATION_L', false, null, 1, 'SELECT', now(), now());
insert into shared.self_disclosure_sub_question
values (2, 'SELF_DISCLOSURE_CURRENT_ADDRESS_L', false, null, 2, 'ADDRESS', now(), now());
insert into shared.self_disclosure_sub_question
values (3, 'SELF_DISCLOSURE_EMPLOYMENT_L', false, null, 3, 'EMPLOYMENT', now(), now());
insert into shared.self_disclosure_sub_question
values (4, 'SELF_DISCLOSURE_PAYMENT_OBLIGATIONS_L', true, 'SELF_DISCLOSURE_PAYMENT_OBLIGATIONS_L', 4, 'BOOLEAN', now(),
        now());
insert into shared.self_disclosure_sub_question
values (5, 'SELF_DISCLOSURE_LIST_OF_DEBTORS_L', false, null, 5, 'BOOLEAN', now(), now());
insert into shared.self_disclosure_sub_question
values (6, 'SELF_DISCLOSURE_PAST_EVICTIONS_L', false, null, 6, 'BOOLEAN', now(), now());
insert into shared.self_disclosure_sub_question
values (7, 'SELF_DISCLOSURE_PRIVATE_BANKRUPTCY_L', false, null, 7, 'BOOLEAN', now(), now());
insert into shared.self_disclosure_sub_question
values (8, 'SELF_DISCLOSURE_PRIVATE_BANKRUPTCY_5_YEARS_L', false, null, 8, 'BOOLEAN', now(), now());
insert into shared.self_disclosure_sub_question
values (9, 'SELF_DISCLOSURE_FINANCIAL_REPORT_L', false, null, 9, 'BOOLEAN', now(), now());
insert into shared.self_disclosure_sub_question
values (10, 'SELF_DISCLOSURE_RENTAL_ARREARS_L', false, null, 10, 'BOOLEAN', now(), now());

insert into shared.self_disclosure_sub_question
values (11, 'SELF_DISCLOSURE_ANIMALS_L', true, 'SELF_DISCLOSURE_ANIMALS_COMMENT_HINT_L', 11, 'BOOLEAN', now(), now());

insert into shared.self_disclosure_sub_question
values (12, 'SELF_DISCLOSURE_DOCUMENT_INCOME', false, null, 12, 'DOCUMENT', now(), now());

insert into shared.self_disclosure_sub_question
values (13, 'SELF_DISCLOSURE_DOCUMENT_CREDIT_RATING', false, null, 13, 'DOCUMENT', now(), now());

insert into shared.self_disclosure_question_sub_question values (1004, 1);
insert into shared.self_disclosure_question_sub_question values (1004, 2);
insert into shared.self_disclosure_question_sub_question values (1004, 3);
insert into shared.self_disclosure_question_sub_question values (1004, 4);
insert into shared.self_disclosure_question_sub_question values (1004, 5);
insert into shared.self_disclosure_question_sub_question values (1004, 6);
insert into shared.self_disclosure_question_sub_question values (1004, 7);
insert into shared.self_disclosure_question_sub_question values (1004, 8);
insert into shared.self_disclosure_question_sub_question values (1004, 9);
insert into shared.self_disclosure_question_sub_question values (1004, 10);
insert into shared.self_disclosure_question_sub_question values (1004, 11);
insert into shared.self_disclosure_question_sub_question values (1004, 12);
insert into shared.self_disclosure_question_sub_question values (1004, 13);

insert into shared.self_disclosure_question_sub_question values (1005, 1);
insert into shared.self_disclosure_question_sub_question values (1005, 2);
insert into shared.self_disclosure_question_sub_question values (1005, 3);
insert into shared.self_disclosure_question_sub_question values (1005, 4);
insert into shared.self_disclosure_question_sub_question values (1005, 5);
insert into shared.self_disclosure_question_sub_question values (1005, 6);
insert into shared.self_disclosure_question_sub_question values (1005, 7);
insert into shared.self_disclosure_question_sub_question values (1005, 8);
insert into shared.self_disclosure_question_sub_question values (1005, 9);
insert into shared.self_disclosure_question_sub_question values (1005, 10);
insert into shared.self_disclosure_question_sub_question values (1005, 11);
insert into shared.self_disclosure_question_sub_question values (1005, 12);
insert into shared.self_disclosure_question_sub_question values (1005, 13);
