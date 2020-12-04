create table landlord.conversation_message_template
(
    id          bigint       not null,
    customer_id bigint       not null,
    content     text         not null,
    title       varchar(500) not null,
    attachments jsonb,
    created     timestamp,
    updated     timestamp,
    constraint pk_conversation_message_template PRIMARY KEY (id),
    constraint fk_conversation_message_template_1 foreign key (customer_id) references landlord.customer match simple
        on update cascade on delete cascade
);

create index fki_conversation_message_template_1 on landlord.conversation_message_template(customer_id);