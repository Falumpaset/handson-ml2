create table shared.digital_contract_api_user
(
    id                   bigint not null,
    customer_id          bigint not null,
    docusign_api_user_id uuid not null,
    created timestamp,
    updated timestamp,
    constraint pk_digital_contract_api_user PRIMARY KEY (id),
    constraint fk_digital_contract_api_user_customer foreign key (customer_id) references landlord.customer match simple
        on update cascade on delete cascade
);
