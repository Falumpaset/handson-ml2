ALTER TYPE landlord.addontype RENAME TO addontype_old;

create type landlord.addontype as enum ('EMAILEDITOR', 'DATAINSIGHTS', 'PORTALPUBLISH', 'SHORTLIST', 'BRANDING', 'IMPORT', 'HPMODULE', 'AGENT', 'CUSTOM_QUESTIONS', 'SCHUFA', 'SELF_DISCLOSURE', 'PREMIUM_SUPPORT', 'REPORTING');

ALTER TABLE landlord.addonproduct
    ALTER COLUMN "addontype" TYPE landlord.addontype USING "addontype"::text::landlord.addontype;

DROP TYPE landlord.addontype_old;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200024, 'ADDON_TITLE_REPORTING_L', 'ADDON_DESCRIPTION_REPORTING_L', 'SUBSCRIPTION', NOW(), NOW(),
        'REPORTING');

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200025, 'ADDON_TITLE_REPORTING_L', 'ADDON_DESCRIPTION_REPORTING_L', 'SUBSCRIPTION', NOW(), NOW(),
        'REPORTING');

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200024, 100000, 200024);

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200025, 100001, 200025);

INSERT INTO landlord.permission_scheme (id, description, name, created, updated)
VALUES (1200, 'Reporting', 'Reporting', NOW(), NOW());

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200024, 1200, 200024);

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200025, 1200, 200025);

insert into shared."right" (id, description, name, shortcode, "group", created, updated)
VALUES (2012, 'show reporting', 'Show-Reporting', 'reporting', 'addon', now(), now());

INSERT INTO landlord.right (id, right_id, created)
VALUES (2012, 2012, now());

INSERT INTO landlord.permissionscheme_rights (id, permission_scheme_id, right_id, usertype)
VALUES (2002, 1200, 2012, 'COMPANYADMIN');

INSERT INTO landlord.permissionscheme_rights (id, permission_scheme_id, right_id, usertype)
VALUES (2003, 1200, 2012, 'EMPLOYEE');

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200024, 0, 50, 'EUR');

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200025, 0, 550, 'EUR');

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200024, 200024, 200024, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD[], null);

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200025, 200025, 200025, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD[], null);
