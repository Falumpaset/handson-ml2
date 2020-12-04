create table if not exists landlord.external_api_user
(
    id          bigint       not null,
    customer_id bigint       not null,
    created     timestamp    not null,
    updated     timestamp    not null,
    username    varchar(255) not null,
    constraint external_api_user_pky primary key (id),
    constraint fk_external_api_user_customer foreign key (customer_id) references landlord.customer (id) match simple
        on update cascade on DELETE cascade
);

create index if not exists fki_external_api_user_customer on landlord.external_api_user(customer_id);