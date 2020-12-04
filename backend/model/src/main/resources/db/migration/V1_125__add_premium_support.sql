ALTER TYPE landlord.addontype RENAME TO addontype_old;

create type landlord.addontype as enum ('EMAILEDITOR', 'DATAINSIGHTS', 'PORTALPUBLISH', 'SHORTLIST', 'BRANDING', 'IMPORT', 'HPMODULE', 'AGENT', 'CUSTOM_QUESTIONS', 'SCHUFA', 'SELF_DISCLOSURE', 'PREMIUM_SUPPORT');

ALTER TABLE landlord.addonproduct
    ALTER COLUMN "addontype" TYPE landlord.addontype USING "addontype"::text::landlord.addontype;

DROP TYPE landlord.addontype_old;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200022, 'ADDON_TITLE_PREMIUM_SUPPORT_L', 'ADDON_DESCRIPTION_PREMIUM_SUPPORT_L', 'SUBSCRIPTION', NOW(), NOW(),
        'PREMIUM_SUPPORT');

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200023, 'ADDON_TITLE_PREMIUM_SUPPORT_L', 'ADDON_DESCRIPTION_PREMIUM_SUPPORT_L', 'SUBSCRIPTION', NOW(), NOW(),
        'PREMIUM_SUPPORT');

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200022, 100000, 200022);

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200023, 100001, 200023);

INSERT INTO landlord.permission_scheme (id, description, name, created, updated)
VALUES (1100, 'Premium support', 'Premium support', NOW(), NOW());

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200022, 1100, 200022);

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200023, 1100, 200023);

insert into shared."right" (id, description, name, shortcode, "group", created, updated)
VALUES (2011, 'show premium support', 'Show-Premium-Support', 'premium_support', 'addon', now(), now());

INSERT INTO landlord.right (id, right_id, created)
VALUES (2011, 2011, now());

INSERT INTO landlord.permissionscheme_rights (id, permission_scheme_id, right_id, usertype)
VALUES (2001, 1100, 2011, 'COMPANYADMIN');

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200022, 0, 30, 'EUR');

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200023, 0, 330, 'EUR');

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200022, 200022, 200022, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD[], null);

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200023, 200023, 200023, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD[], null);
