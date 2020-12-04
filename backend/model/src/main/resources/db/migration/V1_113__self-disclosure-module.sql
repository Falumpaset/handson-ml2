ALTER TYPE landlord.addontype RENAME TO addontype_old;

create type landlord.addontype as enum ('EMAILEDITOR', 'DATAINSIGHTS', 'PORTALPUBLISH', 'SHORTLIST', 'BRANDING', 'IMPORT', 'HPMODULE', 'AGENT', 'CUSTOM_QUESTIONS', 'SCHUFA', 'SELF_DISCLOSURE');

ALTER TABLE landlord.addonproduct
    ALTER COLUMN "addontype" TYPE landlord.addontype USING "addontype"::text::landlord.addontype;

DROP TYPE landlord.addontype_old;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200020, 'ADDON_TITLE_SELF_DISCLOSURE_L', 'ADDON_DESCRIPTION_SELF_DISCLOSURE_L', 'SUBSCRIPTION', NOW(), NOW(),
        'SELF_DISCLOSURE');

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200021, 'ADDON_TITLE_SELF_DISCLOSURE_L', 'ADDON_DESCRIPTION_SELF_DISCLOSURE_L', 'SUBSCRIPTION', NOW(), NOW(),
        'SELF_DISCLOSURE');

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200020, 100000, 200020);

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200021, 100001, 200021);

INSERT INTO landlord.permission_scheme (id, description, name, created, updated)
VALUES (1000, 'Self Disclosure', 'Self Disclosure', NOW(), NOW());

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200020, 1000, 200020);

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200021, 1000, 200021);

insert into shared."right" (id, description, name, shortcode, "group", created, updated)
VALUES (2010, 'show self disclosure', 'Show-Self-Disclosure', 'self_disclosure', 'addon', now(), now());

INSERT INTO landlord.right (id, right_id, created)
VALUES (2010, 2010, now());

INSERT INTO landlord.permissionscheme_rights (id, permission_scheme_id, right_id, usertype)
VALUES (1001, 1000, 2010, 'COMPANYADMIN');

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200020, 0, 30, 'EUR');

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200021, 0, 330, 'EUR');

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200020, 200020, 200020, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD[], null);

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200021, 200021, 200021, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD[], null);

create type shared.selfdisclosurequestiontype as enum ('BOOLEAN', 'SELECT', 'PERSON', 'CHILD', 'CHILDREN', 'PERSONS', 'ADDRESS', 'EMPLOYMENT', 'FLAT');

CREATE TABLE shared.self_disclosure
(
    id             bigint not null,
    customer_id    bigint,
    feedback_email varchar(255),
    description    varchar(2048),
    documents      jsonb,
    confirmations  jsonb,
    created        timestamp without time zone,
    updated        timestamp without time zone,

    CONSTRAINT pk_self_disclosure PRIMARY KEY (id),
    CONSTRAINT fk_self_disclosure FOREIGN KEY (customer_id)
        REFERENCES landlord.customer (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
create index self_disclosure_customer_index on shared.self_disclosure (customer_id);

CREATE TABLE shared.self_disclosure_question
(
    id                 bigint  not null,
    title              varchar(255),
    mandatory          boolean not null,
    hidden             boolean not null,
    self_disclosure_id bigint  not null,
    type               shared.selfdisclosurequestiontype,
    desired_responses  jsonb   not null,
    sub_questions      jsonb,
    comment_allowed    boolean not null,
    comment_hint       varchar(2048),
    created            timestamp without time zone,
    updated            timestamp without time zone,

    CONSTRAINT pk_self_disclosure_question PRIMARY KEY (id),
    CONSTRAINT fk_self_disclosure_question FOREIGN KEY (self_disclosure_id)
        REFERENCES shared.self_disclosure (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
create index fki_self_disclosure_question on shared.self_disclosure_question (self_disclosure_id);

create table shared.self_disclosure_response
(
    id                 bigint not null,
    user_id            bigint not null,
    property_id        bigint not null,
    self_disclosure_id bigint not null,
    data               jsonb  not null,
    created            timestamp without time zone,
    updated            timestamp without time zone,

    CONSTRAINT pk_self_disclosure_response PRIMARY KEY (id),
    constraint fk_self_disclosure_response_1 foreign key (user_id)
        REFERENCES propertysearcher.user (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    constraint fk_self_disclosure_response_question foreign key (self_disclosure_id)
        REFERENCES shared.self_disclosure (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    constraint fk_self_disclosure_response_property foreign key (property_id)
        REFERENCES landlord.property (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT uq_user_self_disclosure_question_response UNIQUE (user_id, property_id, self_disclosure_id)
);

ALTER TABLE landlord.property
    ADD COLUMN show_self_disclosure_questions boolean not null default false;

INSERT INTO "shared"."self_disclosure" ("id", "customer_id", "feedback_email", "description", "documents",
                                        "confirmations", "created", "updated")
VALUES (1000, null, null, null, null, null, NOW(), NOW());

INSERT INTO "shared"."self_disclosure_question" ("id", "title", "mandatory", "hidden", "self_disclosure_id", "type",
                                                 "desired_responses", "comment_allowed", "comment_hint", "created",
                                                 "updated", "sub_questions")
VALUES (1000, 'SELF_DISCLOSURE_FIND_OUT_ABOUT_US_L', true, false, 1000, 'SELECT', '{
  "question": [
    "IMMOSCOUT",
    "IMMONET",
    "RECOMMENDED",
    "WEBPAGE",
    "OTHER"
  ]
}', false, null, NOW(), NOW(), null);

INSERT INTO "shared"."self_disclosure_question" ("id", "title", "mandatory", "hidden", "self_disclosure_id", "type",
                                                 "desired_responses", "comment_allowed", "comment_hint", "created",
                                                 "updated", "sub_questions")
VALUES (1001, 'SELF_DISCLOSURE_FLAT_VISITED_L', false, false, 1000, 'FLAT', '{}', false, null, NOW(), NOW(), null);

INSERT INTO "shared"."self_disclosure_question" ("id", "title", "mandatory", "hidden", "self_disclosure_id", "type",
                                                 "desired_responses", "comment_allowed", "comment_hint", "created",
                                                 "updated", "sub_questions")
VALUES (1002, 'SELF_DISCLOSURE_HAS_PETS_L', true, false, 1000, 'BOOLEAN', '{}', true,
        'SELF_DISCLOSURE_HAS_PETS_COMMENT_HINT_L', NOW(), NOW(), null);

INSERT INTO "shared"."self_disclosure_question" ("id", "title", "mandatory", "hidden", "self_disclosure_id", "type",
                                                 "desired_responses", "comment_allowed", "comment_hint", "created",
                                                 "updated", "sub_questions")
VALUES (1003, 'SELF_DISCLOSURE_HAS_WBS_L', true, false, 1000, 'BOOLEAN', '{}', true,
        'SELF_DISCLOSURE_HAS_WBS_COMMENT_HINT_L', NOW(), NOW(), null);

INSERT INTO "shared"."self_disclosure_question" ("id", "title", "mandatory", "hidden", "self_disclosure_id", "type",
                                                 "desired_responses", "comment_allowed", "comment_hint", "created",
                                                 "updated", "sub_questions")
VALUES (1004, 'SELF_DISCLOSURE_PERSONAL_INFO_L', false, false, 1000, 'PERSON', '{}', false, null, NOW(), NOW(), '[
  {
    "title": "SELF_DISCLOSURE_CURRENT_ADDRESS_L",
    "type": "ADDRESS",
    "mandatory": false,
    "hidden": false,
    "commentAllowed": false
  },
  {
    "title": "SELF_DISCLOSURE_EMPLOYMENT_L",
    "type": "EMPLOYMENT",
    "mandatory": false,
    "hidden": false,
    "commentAllowed": false
  },
  {
    "title": "SELF_DISCLOSURE_PAYMENT_OBLIGATIONS_L",
    "type": "BOOLEAN",
    "mandatory": true,
    "hidden": false,
    "commentAllowed": true,
    "commentHint": "SELF_DISCLOSURE_PAYMENT_OBLIGATIONS_COMMENT_HINT_L"
  },
  {
    "title": "SELF_DISCLOSURE_LIST_OF_DEBTORS_L",
    "type": "BOOLEAN",
    "mandatory": true,
    "hidden": false,
    "commentAllowed": false
  },
  {
    "title": "SELF_DISCLOSURE_PAST_EVICTIONS_L",
    "type": "BOOLEAN",
    "mandatory": true,
    "hidden": false,
    "commentAllowed": false
  },
  {
    "title": "SELF_DISCLOSURE_PRIVATE_BANKRUPTCY_L",
    "type": "BOOLEAN",
    "mandatory": true,
    "hidden": false,
    "commentAllowed": false
  }
]');

INSERT INTO "shared"."self_disclosure_question" ("id", "title", "mandatory", "hidden", "self_disclosure_id", "type",
                                                 "desired_responses", "comment_allowed", "comment_hint", "created",
                                                 "updated", "sub_questions")
VALUES (1005, 'SELF_DISCLOSURE_PERSONAL_INFO_TENANT_L', false, false, 1000, 'PERSONS', '{}', false, null, NOW(), NOW(),
        '[
          {
            "title": "SELF_DISCLOSURE_CURRENT_ADDRESS_L",
            "type": "ADDRESS",
            "mandatory": false,
            "hidden": false,
            "commentAllowed": false
          },
          {
            "title": "SELF_DISCLOSURE_EMPLOYMENT_L",
            "type": "EMPLOYMENT",
            "mandatory": false,
            "hidden": false,
            "commentAllowed": false
          },
          {
            "title": "SELF_DISCLOSURE_PAYMENT_OBLIGATIONS_L",
            "type": "BOOLEAN",
            "mandatory": true,
            "hidden": false,
            "commentAllowed": true,
            "commentHint": "SELF_DISCLOSURE_PAYMENT_OBLIGATIONS_COMMENT_HINT_L"
          },
          {
            "title": "SELF_DISCLOSURE_LIST_OF_DEBTORS_L",
            "type": "BOOLEAN",
            "mandatory": true,
            "hidden": false,
            "commentAllowed": false
          },
          {
            "title": "SELF_DISCLOSURE_PAST_EVICTIONS_L",
            "type": "BOOLEAN",
            "mandatory": true,
            "hidden": false,
            "commentAllowed": false
          },
          {
            "title": "SELF_DISCLOSURE_PRIVATE_BANKRUPTCY_L",
            "type": "BOOLEAN",
            "mandatory": true,
            "hidden": false,
            "commentAllowed": false
          }
        ]');

INSERT INTO "shared"."self_disclosure_question" ("id", "title", "mandatory", "hidden", "self_disclosure_id", "type",
                                                 "desired_responses", "comment_allowed", "comment_hint", "created",
                                                 "updated", "sub_questions")
VALUES (1006, 'SELF_DISCLOSURE_CHILDREN_ADULT_L', true, false, 1000, 'CHILDREN', '{}', false, null, NOW(), NOW(),
        null);

INSERT INTO "shared"."self_disclosure_question" ("id", "title", "mandatory", "hidden", "self_disclosure_id", "type",
                                                 "desired_responses", "comment_allowed", "comment_hint", "created",
                                                 "updated", "sub_questions")
VALUES (1007, 'SELF_DISCLOSURE_CHILDREN_MINOR_L', true, false, 1000, 'CHILDREN', '{}', false, null, NOW(), NOW(),
        null);
