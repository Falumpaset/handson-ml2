INSERT INTO landlord.product (id, name, description, subscriptionperiod, created, updated)
VALUES (100000, 'Flatrate-Produkt', 'Flatrate-Produkt mit einer Vertragslaufzeit von einem Monat.', 'MONTHLY', NOW(), NOW())
ON CONFLICT DO NOTHING;

INSERT INTO landlord.product (id, name, description, subscriptionperiod, created, updated)
VALUES (100001, 'Flatrate-Produkt', 'Flatrate-Produkt mit einer Vertragslaufzeit von einem Jahr.', 'YEARLY', NOW(), NOW())
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200004, 'ADDON_TITLE_PUBLISH_TO_PORTALS_L', 'ADDON_DESCRIPTION_PUBLISH_TO_PORTALS_L', 'SUBSCRIPTION', NOW(), NOW(), 'PORTALPUBLISH')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200005, 'ADDON_TITLE_PUBLISH_TO_PORTALS_L', 'ADDON_DESCRIPTION_PUBLISH_TO_PORTALS_L', 'SUBSCRIPTION', NOW(), NOW(), 'PORTALPUBLISH')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200014, 'ADDON_TITLE_AGENT_L', 'ADDON_DESCRIPTION_AGENT_L', 'SUBSCRIPTION', NOW(), NOW(), 'AGENT1')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200006, 'ADDON_TITLE_SHORTLIST_L', 'ADDON_DESCRIPTION_SHORTLIST_L', 'SUBSCRIPTION', NOW(), NOW(), 'SHORTLIST')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200007, 'ADDON_TITLE_SHORTLIST_L', 'ADDON_DESCRIPTION_SHORTLIST_L', 'SUBSCRIPTION', NOW(), NOW(), 'SHORTLIST')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200015, 'ADDON_TITLE_AGENT_L', 'ADDON_DESCRIPTION_AGENT_L', 'SUBSCRIPTION', NOW(), NOW(), 'AGENT1')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200000, 'ADDON_TITLE_EMAIL_EDITOR_L', 'ADDON_DESCRIPTION_EMAIL_EDITOR_L', 'SUBSCRIPTION', NOW(), NOW(), 'EMAILEDITOR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200001, 'ADDON_TITLE_EMAIL_EDITOR_L', 'ADDON_DESCRIPTION_EMAIL_EDITOR_L', 'SUBSCRIPTION', NOW(), NOW(), 'EMAILEDITOR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200010, 'ADDON_TITLE_INVENTORY_IMPORT_L', 'ADDON_DESCRIPTION_INVENTORY_IMPORT_L', 'SUBSCRIPTION', NOW(), NOW(), 'IMPORT')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200011, 'ADDON_TITLE_INVENTORY_IMPORT_L', 'ADDON_DESCRIPTION_INVENTORY_IMPORT_L', 'SUBSCRIPTION', NOW(), NOW(), 'IMPORT')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200012, 'ADDON_TITLE_HOMEPAGE_MODULE_L', 'ADDON_DESCRIPTION_HOMEPAGE_MODULE_L', 'SUBSCRIPTION', NOW(), NOW(), 'HPMODULE')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200013, 'ADDON_TITLE_HOMEPAGE_MODULE_L', 'ADDON_DESCRIPTION_HOMEPAGE_MODULE_L', 'SUBSCRIPTION', NOW(), NOW(), 'HPMODULE')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200002, 'ADDON_TITLE_DATA_INSIGHTS_L', 'ADDON_DESCRIPTION_DATA_INSIGHTS_L', 'SUBSCRIPTION', NOW(), NOW(), 'DATAINSIGHTS')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200003, 'ADDON_TITLE_DATA_INSIGHTS_L', 'ADDON_DESCRIPTION_DATA_INSIGHTS_L', 'SUBSCRIPTION', NOW(), NOW(), 'DATAINSIGHTS')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200008, 'ADDON_TITLE_CUSTOMISED_BRANDING_L', 'ADDON_DESCRIPTION_CUSTOMISED_BRANDING_L', 'SUBSCRIPTION', NOW(), NOW(), 'BRANDING')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct (id, name, description, producttype, created, updated, addontype)
VALUES (200009, 'ADDON_TITLE_CUSTOMISED_BRANDING_L', 'ADDON_DESCRIPTION_CUSTOMISED_BRANDING_L', 'SUBSCRIPTION', NOW(), NOW(), 'BRANDING')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.permission_scheme (id, description, name, created, updated)
VALUES (100, 'Email Editor', 'Email Editor', NOW(), NOW())
ON CONFLICT DO NOTHING;

INSERT INTO landlord.permission_scheme (id, description, name, created, updated)
VALUES (200, 'Data insights', 'Analytics', NOW(), null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.permission_scheme (id, description, name, created, updated)
VALUES (300, 'Publish to Portals', 'Publish to Portals', NOW(), null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.permission_scheme (id, description, name, created, updated)
VALUES (400, 'Shortlist', 'Shortlist', NOW(), null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.permission_scheme (id, description, name, created, updated)
VALUES (500, 'Customised branding', 'Customised branding', NOW(), null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.permission_scheme (id, description, name, created, updated)
VALUES (600, 'Inventory Import', 'Inventory Import', NOW(), null)
ON CONFLICT DO NOTHING;


INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200000, 100, 200000)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200001, 100, 200001)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200002, 200, 200002)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200003, 200, 200003)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200004, 300, 200004)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200005, 300, 200005)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200006, 400, 200006)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200007, 400, 200007)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200008, 500, 200008)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200009, 500, 200009)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200010, 600, 200010)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id)
VALUES (200011, 600, 200011)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200000, 0, 30, 'EUR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200001, 0, 300, 'EUR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200002, 0, 30, 'EUR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200003, 0, 300, 'EUR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200004, 0, 30, 'EUR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200005, 0, 300, 'EUR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200006, 0, 30, 'EUR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200007, 0, 300, 'EUR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200008, 0, 30, 'EUR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200009, 0, 300, 'EUR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200010, 0, 30, 'EUR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200011, 0, 300, 'EUR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200015, 300, 0, 'EUR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (200014, 30, 0, 'EUR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (100001, 0, 0, 'EUR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.price (id, fixedpart, variablepart, currency)
VALUES (100000, 0, 0, 'EUR')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productprice (id, product_id, price_id, location, paymentmethods, customer_id)
VALUES (100000, 100000, 100000, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productprice (id, product_id, price_id, location, paymentmethods, customer_id)
VALUES (100001, 100001, 100001, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200000, 100000, 200000)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200001, 100001, 200001)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200002, 100000, 200002)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200003, 100001, 200003)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200004, 100000, 200004)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200005, 100001, 200005)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200006, 100000, 200006)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200007, 100001, 200007)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200008, 100000, 200008)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200009, 100001, 200009)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200010, 100000, 200010)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200011, 100001, 200011)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200014, 100000, 200014)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id)
VALUES (200015, 100001, 200015)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct_limitations (id, addonproduct_id, limitation, limitation_value)
VALUES (200000, 200000, 'EMAIL_EDITOR', 'true')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.addonproduct_limitations (id, addonproduct_id, limitation, limitation_value)
VALUES (200001, 200001, 'EMAIL_EDITOR', 'true')
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200000, 200000, 200000, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200004, 200004, 200004, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200008, 200008, 200008, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200010, 200010, 200010, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200014, 200014, 200014, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200015, 200015, 200015, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200001, 200001, 200001, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200011, 200011, 200011, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200002, 200002, 200002, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200006, 200006, 200006, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200005, 200005, 200005, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200009, 200009, 200009, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200003, 200003, 200003, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null)
ON CONFLICT DO NOTHING;

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES (200007, 200007, 200007, 'DE', ARRAY ['STRIPE','INVOICE'] :: shared.PAYMENTMETHOD [], null)
ON CONFLICT DO NOTHING;




