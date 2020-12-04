ALTER TABLE landlord.price ADD setup double precision NULL;

insert into landlord.price(id, fixedpart, variablepart, setup, currency) values(200012, 150, 0, 499, 'EUR');
insert into landlord.price(id, fixedpart, variablepart, setup, currency) values(200013, 1500, 0, 499, 'EUR');

INSERT INTO landlord.permission_scheme (id, description, name) VALUES (700, 'Homepage module', 'Homepage module');

INSERT INTO shared."right" (id, description, name, shortcode, "group", created, updated) VALUES (7001, '', 'Homepage module', 'homepage_module', 'homepage_module', '2018-03-21 11:43:28.029816', '2018-03-21 11:43:28.029816');

INSERT INTO landlord."right" (id, right_id) VALUES (7001, 7001);
INSERT INTO landlord.permissionscheme_rights (id, permission_scheme_id, right_id, usertype) VALUES (701, 700, 7001, 'COMPANYADMIN');

INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id) VALUES (200012, 700, 200012);
INSERT INTO landlord.addonproductpermissionscheme (addonproduct_id, permissionscheme_id, id) VALUES (200013, 700, 200013);

INSERT INTO landlord.productaddon (id, product_id, addonproduct_id) VALUES (200012, 100000, 200012);
INSERT INTO landlord.productaddon (id, product_id, addonproduct_id) VALUES (200013, 100001, 200013);

INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id) VALUES (200012, 200012, 200012, 'DE', '{STRIPE,INVOICE}', null);
INSERT INTO landlord.productaddonprice (id, productaddon_id, price_id, location, paymentmethods, customer_id) VALUES (200013, 200013, 200013, 'DE', '{STRIPE,INVOICE}', null);

ALTER TABLE propertysearcher.price ADD setup double precision NULL;
