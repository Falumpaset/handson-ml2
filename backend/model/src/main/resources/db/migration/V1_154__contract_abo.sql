ALTER TYPE landlord.addontype RENAME TO addontype_old;

create type landlord.addontype as enum ('EMAILEDITOR', 'DATAINSIGHTS', 'PORTALPUBLISH', 'SHORTLIST', 'BRANDING', 'IMPORT', 'HPMODULE', 'AGENT', 'CUSTOM_QUESTIONS', 'SCHUFA', 'SELF_DISCLOSURE', 'PREMIUM_SUPPORT', 'REPORTING', 'INTERNAL_TENANT_POOL', 'DIGITAL_CONTRACT');


ALTER TABLE landlord.addonproduct
    ALTER COLUMN "addontype" TYPE landlord.addontype USING "addontype"::text::landlord.addontype;

DROP TYPE landlord.addontype_old;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200028, 'ADDON_TITLE_DIGITAL_CONTRACT_L', 'ADDON_DESCRIPTION_DIGITAL_CONTRACT_L', 'SUBSCRIPTION', NOW(), NOW(),
        'DIGITAL_CONTRACT');

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200029, 'ADDON_TITLE_DIGITAL_CONTRACT_L', 'ADDON_DESCRIPTION_DIGITAL_CONTRACT_L', 'SUBSCRIPTION', NOW(), NOW(),
        'DIGITAL_CONTRACT');

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200028, 100000, 200028);

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200029, 100001, 200029);

INSERT INTO landlord.permission_scheme (id, description, name, created, updated)
VALUES (1400, 'Digital contract', 'Digital contract', NOW(), NOW());

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200028, 1400, 200028);

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200029, 1400, 200029);

insert into shared."right" (id, description, name, shortcode, "group", created, updated)
VALUES (2014, 'show digital contract', 'Show-Digital-Contract', 'digital_contract', 'addon', now(), now());

INSERT INTO landlord.right (id, right_id, created)
VALUES (2014, 2014, now());

INSERT INTO landlord.permissionscheme_rights (id, permission_scheme_id, right_id, usertype)
VALUES (2005, 1400, 2014, 'COMPANYADMIN');

INSERT INTO landlord.permissionscheme_rights (id, permission_scheme_id, right_id, usertype)
VALUES (2006, 1400, 2014, 'EMPLOYEE');

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200028, 0, 20, 'EUR');

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200029, 0, 220, 'EUR');

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200028, 200028, 200028, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD[], null);

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200029, 200029, 200029, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD[], null);


drop table if exists landlord.usage cascade;
drop table if exists landlord.quota cascade;

drop table if exists propertysearcher.quota cascade;

create type shared.quota_product_type as enum (
    'DIGITAL_CONTRACT'
    );


create table landlord.quota_package
(
    id       bigint                      not null,
    type     shared.quota_product_type not null,
    price_id bigint                      not null,
    quantity integer                     not null,
    created  timestamp                   not null,
    updated  timestamp                   not null,

    CONSTRAINT pk_quota_package PRIMARY KEY (id),
    CONSTRAINT fk_quota_package_1 FOREIGN KEY (price_id)
        REFERENCES landlord.price (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

insert into landlord.price (id, fixedpart, variablepart, currency)
values (300001, 200, 0, 'EUR');
insert into landlord.quota_package (id, type, price_id, quantity, created, updated)
values (300001, 'DIGITAL_CONTRACT', 300001, 10, now(), now());

insert into landlord.price (id, fixedpart, variablepart, currency)
values (300002, 875, 0, 'EUR');
insert into landlord.quota_package (id, type, price_id, quantity, created, updated)
values (300002, 'DIGITAL_CONTRACT', 300002, 50, now(), now());

insert into landlord.price (id, fixedpart, variablepart, currency)
values (300003, 1520, 0, 'EUR');
insert into landlord.quota_package (id, type, price_id, quantity, created, updated)
values (300003, 'DIGITAL_CONTRACT', 300003, 100, now(), now());

insert into landlord.price (id, fixedpart, variablepart, currency)
values (300004, 3275, 0, 'EUR');
insert into landlord.quota_package (id, type, price_id, quantity, created, updated)
values (300004, 'DIGITAL_CONTRACT', 300004, 250, now(), now());

insert into landlord.price (id, fixedpart, variablepart, currency)
values (300005, 5550, 0, 'EUR');
insert into landlord.quota_package (id, type, price_id, quantity, created, updated)
values (300005, 'DIGITAL_CONTRACT', 300005, 500, now(), now());

insert into landlord.price (id, fixedpart, variablepart, currency)
values (300006, 9200, 0, 'EUR');
insert into landlord.quota_package (id, type, price_id, quantity, created, updated)
values (300006, 'DIGITAL_CONTRACT', 300006, 1000, now(), now());

insert into landlord.price (id, fixedpart, variablepart, currency)
values (300007, 15000, 0, 'EUR');
insert into landlord.quota_package (id, type, price_id, quantity, created, updated)
values (300007, 'DIGITAL_CONTRACT', 300007, 2000, now(), now());

CREATE TABLE landlord.quotabasket
(
    id            bigint                        NOT NULL,
    customer_id   bigint                        NOT NULL,
    status        shared.productbasketstatus    NOT NULL,

    checkoutdate  timestamp without time zone,

    created     timestamp without time zone NOT NULL,
    updated     timestamp without time zone NOT NULL,

    CONSTRAINT quotabasket_pkey  PRIMARY KEY (id),
    CONSTRAINT fk_quotabasket_01 FOREIGN KEY (customer_id)
        REFERENCES landlord.customer (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);


create table landlord.quota_customer_product
(
    id          bigint                      not null,
    customer_id bigint                      not null,
    type        shared.quota_product_type not null,
    created     timestamp                   not null,
    updated     timestamp                   not null,
    CONSTRAINT pk_quota_customer_product PRIMARY KEY (id),
    CONSTRAINT fk_quota_customer_product_1 FOREIGN KEY (customer_id)
        REFERENCES landlord.customer (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

create index fki_quota_customer_product_1 on landlord.quota_customer_product (customer_id);
create unique index fuq_quota_customer_product_1 on landlord.quota_customer_product (customer_id, type);

create table landlord.quota
(
    id                        bigint    not null,
    quota_customer_product_id bigint    not null,
    quantity                  integer   not null,
    created                   timestamp not null,
    updated                   timestamp not null,
    CONSTRAINT pk_quota PRIMARY KEY (id),
    CONSTRAINT fk_quota_1 FOREIGN KEY (quota_customer_product_id)
        REFERENCES landlord.quota_customer_product (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

create index fki_quota_1 on landlord.quota (quota_customer_product_id);

create table landlord.quota_usage
(
    id                        bigint    not null,
    quota_customer_product_id bigint    not null,
    created                   timestamp not null,
    updated                   timestamp not null,
    CONSTRAINT pk_usage PRIMARY KEY (id),
    CONSTRAINT fk_usage_1 FOREIGN KEY (quota_customer_product_id)
        REFERENCES landlord.quota_customer_product (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

create index fki_usage_1 on landlord.quota_usage (quota_customer_product_id);

create table landlord.quotabasket_quota_package
(
    id               bigint  not null,
    quotabasket_id bigint  not null,
    quota_package_id bigint  not null,
    quantity         integer not null,
    created          timestamp,
    updated           timestamp,
    constraint pk_productbasket_quota_package primary key (id),
    constraint fk_productbasket_quota_package_1 foreign key (quotabasket_id)
        references landlord.quotabasket (id) match simple
        on update cascade
        on delete cascade,
    constraint fk_productbasket_quota_package_2 foreign key (quota_package_id)
        references landlord.quota_package (id) match simple
        on update cascade
        on delete cascade
);

create index fki_productbasket_quota_package_1 on landlord.quotabasket_quota_package (quotabasket_id);
create index fki_productbasket_quota_package_2 on landlord.quotabasket_quota_package (quota_package_id);

CREATE TABLE landlord.discount_customer_quota_package
(
    id                           bigint                   NOT NULL,
    customer_id                  bigint                   NOT NULL,
    quota_package_id             bigint                   NOT NULL,
    discount_id                  bigint                   NOT NULL,

    CONSTRAINT discount_customer_quota_package_pkey PRIMARY KEY (id),
    CONSTRAINT fk_discount_customer_quota_package_01 FOREIGN KEY (customer_id)
        REFERENCES landlord.customer (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_discount_customer_quota_package_02 FOREIGN KEY (quota_package_id)
        REFERENCES landlord.quota_package (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_discount_customer_quota_package_03 FOREIGN KEY (discount_id)
        REFERENCES shared.discount (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

create index fki_fk_discount_customer_quota_package_1 on landlord.discount_customer_quota_package (customer_id);
create index fki_fk_discount_customer_quota_package_2 on landlord.discount_customer_quota_package (quota_package_id);
create index fki_fk_discount_customer_quota_package_3 on landlord.discount_customer_quota_package (discount_id);
create unique index idx_customer_quota_package on landlord.discount_customer_quota_package (customer_id, quota_package_id);


create table propertysearcher.quota_package
(
    id       bigint                      not null,
    type     shared.quota_product_type not null,
    price_id bigint                      not null,
    quantity integer                     not null,
    created  timestamp                   not null,
    updated  timestamp                   not null,

    CONSTRAINT pk_quota_package PRIMARY KEY (id),
    CONSTRAINT fk_quota_package_1 FOREIGN KEY (price_id)
        REFERENCES propertysearcher.price (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);


create table propertysearcher.quota_customer_product
(
    id          bigint                      not null,
    customer_id bigint                      not null,
    type        shared.quota_product_type not null,
    created     timestamp                   not null,
    updated     timestamp                   not null,
    CONSTRAINT pk_quota_customer_product PRIMARY KEY (id),
    CONSTRAINT fk_quota_customer_product_1 FOREIGN KEY (customer_id)
        REFERENCES propertysearcher.customer (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

create index fki_quota_customer_product_1 on propertysearcher.quota_customer_product (customer_id);
create unique index fuq_quota_customer_product_1 on propertysearcher.quota_customer_product (customer_id, type);

create table propertysearcher.quota
(
    id                        bigint    not null,
    quota_customer_product_id bigint    not null,
    quantity                  integer   not null,
    created                   timestamp not null,
    updated                   timestamp not null,
    CONSTRAINT pk_quota PRIMARY KEY (id),
    CONSTRAINT fk_quota_1 FOREIGN KEY (quota_customer_product_id)
        REFERENCES propertysearcher.quota_customer_product (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

create index fki_quota_1 on propertysearcher.quota (quota_customer_product_id);

create table propertysearcher.quota_usage
(
    id                        bigint    not null,
    quota_customer_product_id bigint    not null,
    created                   timestamp not null,
    updated                   timestamp not null,
    CONSTRAINT pk_usage PRIMARY KEY (id),
    CONSTRAINT fk_usage_1 FOREIGN KEY (quota_customer_product_id)
        REFERENCES propertysearcher.quota_customer_product (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

create index fki_usage_1 on propertysearcher.quota_usage (quota_customer_product_id);

create table propertysearcher.productbasket_quota_package
(
    id               bigint  not null,
    productbasket_id bigint  not null,
    quota_package_id bigint  not null,
    quantity         integer not null,
    created          timestamp,
    updated           timestamp,
    constraint pk_productbasket_quota_package primary key (id),
    constraint fk_productbasket_quota_package_1 foreign key (productbasket_id)
        references propertysearcher.productbasket (id) match simple
        on update cascade
        on delete cascade,
    constraint fk_productbasket_quota_package_2 foreign key (quota_package_id)
        references propertysearcher.quota_package (id) match simple
        on update cascade
        on delete cascade
);

create index fki_productbasket_quota_package_1 on propertysearcher.productbasket_quota_package (productbasket_id);
create index fki_productbasket_quota_package_2 on propertysearcher.productbasket_quota_package (quota_package_id);

create table propertysearcher.discount_customer_quota_package
(
    id               bigint not null,
    quota_package_id bigint not null,
    discount_id      bigint not null,
    customer_id      bigint not null,
    CONSTRAINT discount_customer_quota_package_pkey PRIMARY KEY (id),
    CONSTRAINT fk_discount_customer_quota_package_01 FOREIGN KEY (customer_id)
        REFERENCES propertysearcher.customer (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_discount_customer_quota_package_02 FOREIGN KEY (quota_package_id)
        REFERENCES propertysearcher.quota_package (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_discount_customer_quota_package_03 FOREIGN KEY (discount_id)
        REFERENCES shared.discount (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE

);
