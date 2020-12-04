alter table propertysearcher.user add column customer_tenant_pool_id bigint;

alter table propertysearcher.user add  CONSTRAINT fk_user_tenant_pool FOREIGN KEY (customer_tenant_pool_id)
    REFERENCES landlord.customer (id) MATCH SIMPLE
    on update cascade
      ON DELETE SET NULL;


ALTER TYPE landlord.addontype RENAME TO addontype_old;

create type landlord.addontype as enum ('EMAILEDITOR', 'DATAINSIGHTS', 'PORTALPUBLISH', 'SHORTLIST', 'BRANDING', 'IMPORT', 'HPMODULE', 'AGENT', 'CUSTOM_QUESTIONS', 'SCHUFA', 'SELF_DISCLOSURE', 'PREMIUM_SUPPORT', 'REPORTING', 'INTERNAL_TENANT_POOL');

ALTER TABLE landlord.addonproduct
    ALTER COLUMN "addontype" TYPE landlord.addontype USING "addontype"::text::landlord.addontype;

DROP TYPE landlord.addontype_old;

alter table landlord.customersettings add column search_until_interval_weeks integer;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200026, 'ADDON_TITLE_INTERNAL_TENANT_POOL_L', 'ADDON_DESCRIPTION_INTERNAL_TENANT_POOL_L', 'SUBSCRIPTION', NOW(), NOW(),
        'INTERNAL_TENANT_POOL');

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200027, 'ADDON_TITLE_INTERNAL_TENANT_POOL_L', 'ADDON_DESCRIPTION_INTERNAL_TENANT_POOL_L', 'SUBSCRIPTION', NOW(), NOW(),
        'INTERNAL_TENANT_POOL');

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200026, 100000, 200026);

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200027, 100001, 200027);

INSERT INTO landlord.permission_scheme (id, description, name, created, updated)
VALUES (1300, 'Internal Tenant Pool', 'Internal Tenant Pool', NOW(), NOW());

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200026, 1300, 200026);

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200027, 1300, 200027);

insert into shared."right" (id, description, name, shortcode, "group", created, updated)
VALUES (2013, 'show internal tenant pool', 'Show-Internal-Tenant-Pool', 'internal_tenant_pool', 'addon', now(), now());

INSERT INTO landlord.right (id, right_id, created)
VALUES (2013, 2013, now());

INSERT INTO landlord.permissionscheme_rights (id, permission_scheme_id, right_id, usertype)
VALUES (2004, 1300, 2013, 'COMPANYADMIN');

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200026, 0, 50, 'EUR');

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200027, 0, 550, 'EUR');

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200026, 200026, 200026, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD[], null);

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200027, 200027, 200027, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD[], null);
