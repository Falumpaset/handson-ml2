DROP TYPE IF EXISTS landlord.cbi_action_type CASCADE;
CREATE TYPE landlord.cbi_action_type
    AS ENUM ('SCHUFA2_ANFRAGE_IDENTITAETS_CHECK', 'SCHUFA2_ANFRAGE_BONITAETSAUSKUNFT',
    'SCHUFA2_AUSKUNFT_BONITAETSAUSKUNFT', 'SCHUFA2_ANFRAGE_KONTONUMMERN_CHECK');


DROP TYPE IF EXISTS landlord.job_state CASCADE;
CREATE TYPE landlord.job_state
    AS ENUM ('ACCEPTED', 'IN_PROGRESS',
    'PROBLEM', 'RESULT', 'ERROR', 'FATAL_ERROR');


CREATE TABLE landlord.schufa_job
(
    id                           bigint                   NOT NULL,
    customer_id                  bigint                   NOT NULL,
    user_id                      bigint,

    credit_rating_request        jsonb,
    credit_rating_response       jsonb,
    credit_rating_result         jsonb,

    account_number_check_request jsonb,
    account_number_check_result  jsonb,

    identity_check_request       jsonb,
    identity_check_result        jsonb,

    state                        landlord.job_state       NOT NULL,
    type                         landlord.cbi_action_type NOT NULL,

    job_id                       bigint                   NOT NULL,

    schufa_job_id                varchar(255),
    error_message                text,

    created                      timestamp without time zone,
    updated                      timestamp without time zone,

    last_update                  timestamp without time zone,
    agent_info                   jsonb,
    user_info                    jsonb,

    CONSTRAINT schufa_job_pkey PRIMARY KEY (id),
    CONSTRAINT fk_schufa_job_01 FOREIGN KEY (customer_id)
        REFERENCES landlord.customer (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_schufa_job_02 FOREIGN KEY (user_id)
        REFERENCES propertysearcher."user" (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

create index fki_fk_schufajob_1 on landlord.schufa_job (customer_id);

create index idx_state_type on landlord.schufa_job (state, type);

create table landlord.schufa_account
(
    id          bigint       NOT NULL,
    customer_id bigint       NOT NULL,
    username    varchar(255) NOT NULL,
    password    varchar(255) NOT NULL,
    created     timestamp without time zone,
    updated     timestamp without time zone,
    CONSTRAINT schufa_account_pkey PRIMARY KEY (id),
    CONSTRAINT fk_schufa_account_01 FOREIGN KEY (customer_id)
        REFERENCES landlord.customer (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

create unique index fki_fk_schufa_account_1 on landlord.schufa_account (customer_id);



ALTER TYPE landlord.addontype RENAME TO addontype_old;

create type landlord.addontype as enum ('EMAILEDITOR', 'DATAINSIGHTS', 'PORTALPUBLISH', 'SHORTLIST', 'BRANDING', 'IMPORT', 'HPMODULE', 'AGENT', 'CUSTOM_QUESTIONS', 'SCHUFA');

ALTER TABLE landlord.addonproduct
    ALTER COLUMN "addontype" DROP DEFAULT,
    ALTER COLUMN "addontype" TYPE landlord.addontype USING "addontype"::text::landlord.addontype;

DROP TYPE landlord.addontype_old;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200018, 'ADDON_TITLE_SCHUFA_L', 'ADDON_DESCRIPTION_SCHUFA_L', 'SUBSCRIPTION', NOW(), NOW(), 'SCHUFA');

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200019, 'ADDON_TITLE_SCHUFA_L', 'ADDON_DESCRIPTION_SCHUFA_L', 'SUBSCRIPTION', NOW(), NOW(), 'SCHUFA');

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200018, 100000, 200018);

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200019, 100001, 200019);

INSERT INTO landlord.permission_scheme (id, description, name, created, updated)
VALUES (900, 'Schufa', 'Schufa', NOW(), NOW());

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200018, 900, 200018);

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200019, 900, 200019);

insert into shared."right"   (id, description, name, shortcode, "group", created, updated)
VALUES (2009, 'show schufa', 'Show-Schufa', 'schufa', 'addon', now(), now());

INSERT INTO landlord.right (id, right_id, created) VALUES
(2009,2009, now());

INSERT INTO landlord.permissionscheme_rights (id, permission_scheme_id, right_id, usertype)
VALUES (901, 900, 2009, 'COMPANYADMIN')  ;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200018, 0, 50, 'EUR');

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200019, 0, 500, 'EUR');

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200018, 200018, 200018, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null);

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200019, 200019, 200019, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null);