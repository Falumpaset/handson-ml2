create table landlord.productbasket_productaddon_to_remove
(
    id               bigint    not null,
    productbasket_id bigint    not null
        constraint fk_productbasket_producaddont_01
            references landlord.productbasket
            on update cascade on delete cascade,
    productaddon_id  bigint    not null
        constraint fk_productbasket_productaddon_to_remove_02
            references landlord.productaddon
            on update cascade on delete cascade,
    quantity         integer   not null,
    created          timestamp not null,
    updated          timestamp not null,
    constraint productbasket_productaddon_to_remove_pkey
        primary key (productbasket_id, productaddon_id)
);


create index fki_fk_productbasket_productaddon_to_remove_02
    on landlord.productbasket_productaddon_to_remove (productaddon_id);

create index fki_fk_productbasket_producaddon_to_remove_01
    on landlord.productbasket_productaddon_to_remove (productbasket_id);


create table propertysearcher.productbasket_productaddon_to_remove
(
    id               bigint    not null,
    productbasket_id bigint    not null
        constraint fk_productbasket_producaddont_01
            references propertysearcher.productbasket
            on update cascade on delete cascade,
    productaddon_id  bigint    not null
        constraint fk_productbasket_productaddon_to_remove_02
            references propertysearcher.productaddon
            on update cascade on delete cascade,
    quantity         integer   not null,
    created          timestamp not null,
    updated          timestamp not null,
    constraint productbasket_productaddon_to_remove_pkey
        primary key (productbasket_id, productaddon_id)
);


create index fki_fk_productbasket_productaddon_to_remove_02
    on propertysearcher.productbasket_productaddon_to_remove (productaddon_id);

create index fki_fk_productbasket_producaddon_to_remove_01
    on propertysearcher.productbasket_productaddon_to_remove (productbasket_id);

