CREATE TABLE shared.custom_question
(
    id                bigint  not null,
    customer_id       bigint  not null,
    scoring           boolean not null,
    importance        int     not null,
    schema            jsonb   not null,
    form              jsonb   not null,
    desired_responses jsonb   not null,

    created   timestamp without time zone,
    updated   timestamp without time zone,
    CONSTRAINT pk_custom_question PRIMARY KEY (id),
    CONSTRAINT fk_custom_question FOREIGN KEY (customer_id)
        REFERENCES landlord.customer (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

create index fki_custom_question on shared.custom_question (customer_id);

create table landlord.property_custom_question
(
    property_id        bigint not null,
    custom_question_id bigint not null,
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

create index fki_property_custom_question_1 on landlord.property_custom_question (property_id);
create index fki_property_custom_question_2 on landlord.property_custom_question (custom_question_id);

create table shared.custom_question_response
(
    id bigint not null,
    user_id bigint not null,
    custom_question_id bigint not null,
    data jsonb not null,

    created   timestamp without time zone,
    updated   timestamp without time zone,
    CONSTRAINT pk_custom_question_response PRIMARY KEY (id),
    constraint fk_custom_question_response_1 foreign key (user_id)
        REFERENCES propertysearcher.user (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    constraint fk_custom_question_response_2 foreign key (custom_question_id)
        REFERENCES shared.custom_question (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT uq_user_custom_question UNIQUE (user_id, custom_question_id)

);


ALTER TYPE landlord.addontype RENAME TO addontype_old;

create type landlord.addontype as enum ('EMAILEDITOR', 'DATAINSIGHTS', 'PORTALPUBLISH', 'SHORTLIST', 'BRANDING', 'IMPORT', 'HPMODULE', 'AGENT', 'AGENT1', 'CUSTOM_QUESTIONS');

ALTER TABLE landlord.addonproduct
    ALTER COLUMN "addontype" DROP DEFAULT,
    ALTER COLUMN "addontype" TYPE landlord.addontype USING "addontype"::text::landlord.addontype;

DROP TYPE landlord.addontype_old;

update landlord.addonproduct set addontype = 'AGENT' where addontype = 'AGENT1';


ALTER TYPE landlord.addontype RENAME TO addontype_old;

create type landlord.addontype as enum ('EMAILEDITOR', 'DATAINSIGHTS', 'PORTALPUBLISH', 'SHORTLIST', 'BRANDING', 'IMPORT', 'HPMODULE', 'AGENT', 'CUSTOM_QUESTIONS');

ALTER TABLE landlord.addonproduct
    ALTER COLUMN "addontype" DROP DEFAULT,
    ALTER COLUMN "addontype" TYPE landlord.addontype USING "addontype"::text::landlord.addontype;

DROP TYPE landlord.addontype_old;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200016, 'ADDON_TITLE_CUSTOM_QUESTIONS_L', 'ADDON_DESCRIPTION_CUSTOM_QUESTIONS_L', 'SUBSCRIPTION', NOW(), NOW(), 'CUSTOM_QUESTIONS');

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200017, 'ADDON_TITLE_CUSTOM_QUESTIONS_L', 'ADDON_DESCRIPTION_CUSTOM_QUESTIONS_L', 'SUBSCRIPTION', NOW(), NOW(), 'CUSTOM_QUESTIONS');

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200016, 100000, 200016);

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200017, 100001, 200017);

INSERT INTO landlord.permission_scheme (id, description, name, created, updated)
VALUES (800, 'Custom Questions', 'Custom Questions', NOW(), NOW());

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200016, 800, 200016);

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200017, 800, 200017);

insert into shared."right"   (id, description, name, shortcode, "group", created, updated)
VALUES (2008, 'show custom questions', 'Show-Custom-Questions', 'custom_questions', 'addon', now(), now());

INSERT INTO landlord.right (id, right_id, created) VALUES
(2008,2008, now());

INSERT INTO landlord.permissionscheme_rights (id, permission_scheme_id, right_id, usertype)
VALUES (801, 800, 2008, 'COMPANYADMIN')  ;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200016, 0, 30, 'EUR');

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200017, 0, 300, 'EUR');

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200016, 200016, 200016, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null);

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200017, 200017, 200017, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null);