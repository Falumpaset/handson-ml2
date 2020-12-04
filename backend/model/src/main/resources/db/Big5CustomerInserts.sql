INSERT INTO landlord.customer (id, description, name, taxid, paymentmethods, customertype, invoiceemail, preferences, files, address, location, managementunits, created, updated, paymentdetails, pricemultiplier, customersize)
VALUES (2000001, null, 'Deutsche Wohnen AG ', null, '[{"method":"INVOICE","preferred": true}]', 'PROPERTYMANAGEMENT', 'sven.ehlert@dwi.deuwo.com', '{}', '[]', '{"city": "Frankfurt am Main", "region": "Hessen", "street": "Pfaffenwiese", "country": "DE", "zipCode": "65929", "additional": null, "coordinates": null, "houseNumber": "300"}', 'DE', 30000, NOW(), NOW(), '{}', 13, 'ENTERPRISE');

INSERT INTO landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated, preferences, profile, type)
VALUES (3000001, 'sven.ehlert@dwi.deuwo.com', null, 2000001, true, false, false, null, NOW(), NOW(), '{}', '{"name": "Ehlert", "phone": "", "title": null, "gender": "MALE", "portrait": null, "firstname": "Sven"}', 'COMPANYADMIN');

INSERT INTO landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated, preferences, profile, type)
VALUES (3000002, 'njacobi+20@immomio.de', null, 2000001, true, false, false, null, NOW(), NOW(), '{}', '{"name": "Jacobi", "phone": "", "title": null, "gender": "MALE", "portrait": null, "firstname": "Nico"}', 'COMPANYADMIN');

INSERT INTO shared.discount (id, name, startdate, enddate, created, updated, value)
VALUES (7000001, 'Deutsche Wohnen AG Beta Discount', NOW(), NOW() + interval '100 year', NOW(), NOW(), 0.2443);

INSERT INTO landlord.discount_customer (discount_id, customer_id)
VALUES (7000001, 2000001);

INSERT INTO landlord.productbasket (id, customer_id, status, properties, checkoutdate, created, updated, trial)
VALUES (4000001, 2000001, 'PENDING', '{}', null, NOW(), NOW(), false);

--monthly customer product
INSERT INTO landlord.productbasket_product (id, productbasket_id, product_id, quantity, created, updated)
VALUES (5000001, 4000001, 100000, 1, NOW(), NOW());

--10Agents monthly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000001, 4000001, 200014, 10, NOW(), NOW());

--email editor monthly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000002, 4000001, 200000, 1, NOW(), NOW());

--FTP inventory import monthly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000003, 4000001, 200010, 1, NOW(), NOW());




INSERT INTO landlord.customer (id, description, name, taxid, paymentmethods, customertype, invoiceemail, preferences, files, address, location, managementunits, created, updated, paymentdetails, pricemultiplier, customersize)
VALUES (2000002, null, 'IMW Wohnen & Services Immobilien GmbH ', null, '[{"method":"INVOICE","preferred": true}]', 'PROPERTYMANAGEMENT', 'seiring@imw-wus.de', '{}', '[]', '{"city": "Berlin", "region": "Berlin-Brandenburg", "street": "Hausvogteiplatz", "country": "DE", "zipCode": "10117", "additional": null, "coordinates": null, "houseNumber": "11a"}', 'DE', 4250, NOW(), NOW(), '{}', 3, 'LARGE');

INSERT INTO landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated, preferences, profile, type)
VALUES (3000003, 'seiring@imw-wus.de', null, 2000002, true, false, false, null, NOW(), NOW(), '{}', '{"name": "Eiring", "phone": "", "title": null, "gender": "FEMALE", "portrait": null, "firstname": "Stefanie"}', 'COMPANYADMIN');

INSERT INTO landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated, preferences, profile, type)
VALUES (3000004, 'njacobi+21@immomio.de', null, 2000002, true, false, false, null, NOW(), NOW(), '{}', '{"name": "Jacobi", "phone": "", "title": null, "gender": "MALE", "portrait": null, "firstname": "Nico"}', 'COMPANYADMIN');

INSERT INTO landlord.productbasket (id, customer_id, status, properties, checkoutdate, created, updated, trial)
VALUES (4000002, 2000002, 'PENDING', '{}', null, NOW(), NOW(), false);

--monthly customer product
INSERT INTO landlord.productbasket_product (id, productbasket_id, product_id, quantity, created, updated)
VALUES (5000002, 4000002, 100000, 1, NOW(), NOW());

--10Agents monthly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000004, 4000002, 200014, 4, NOW(), NOW());

--email editor monthly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000005, 4000002, 200000, 1, NOW(), NOW());

--Branding monthly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000006, 4000002, 200008, 1, NOW(), NOW());

--portals monthly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000007, 4000002, 200004, 1, NOW(), NOW());





INSERT INTO landlord.customer (id, description, name, taxid, paymentmethods, customertype, invoiceemail, preferences, files, address, location, managementunits, created, updated, paymentdetails, pricemultiplier, customersize)
VALUES (2000003, null, 'Gesellschaft für Bauen und Wohnen Hannover mbH (GBH) - hanova', null, '[{"method":"INVOICE","preferred": true}]', 'PROPERTYMANAGEMENT', 'denis.hirschmann@hanova.de', '{}', '[]', '{"city": "Hannover", "region": "Niedersachsen", "street": "Otto-Brenner-Straße", "country": "DE", "zipCode": "30159", "additional": null, "coordinates": null, "houseNumber": "4"}', 'DE', 13500, NOW(), NOW(), '{}', 7, 'ENTERPRISE');

INSERT INTO landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated, preferences, profile, type)
VALUES (3000005, 'denis.hirschmann@hanova.de', null, 2000003, true, false, false, null, NOW(), NOW(), '{}', '{"name": "Hirschmann", "phone": "", "title": null, "gender": "MALE", "portrait": null, "firstname": "Denis"}', 'COMPANYADMIN');

INSERT INTO landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated, preferences, profile, type)
VALUES (3000006, 'njacobi+22@immomio.de', null, 2000003, true, false, false, null, NOW(), NOW(), '{}', '{"name": "Jacobi", "phone": "", "title": null, "gender": "MALE", "portrait": null, "firstname": "Nico"}', 'COMPANYADMIN');

INSERT INTO shared.discount (id, name, startdate, enddate, created, updated, value)
VALUES (7000002, 'hanova Beta Discount', NOW(), NOW() + interval '100 year', NOW(), NOW(), 0.2727);

INSERT INTO landlord.discount_customer (discount_id, customer_id)
VALUES (7000002, 2000003);

INSERT INTO landlord.productbasket (id, customer_id, status, properties, checkoutdate, created, updated, trial)
VALUES (4000003, 2000003, 'PENDING', '{}', null, NOW(), NOW(), false);

--monthly customer product
INSERT INTO landlord.productbasket_product (id, productbasket_id, product_id, quantity, created, updated)
VALUES (5000003, 4000003, 100000, 1, NOW(), NOW());

--10Agents monthly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000008, 4000003, 200014, 13, NOW(), NOW());

--email editor monthly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000009, 4000003, 200000, 1, NOW(), NOW());

--Branding monthly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000010, 4000003, 200008, 1, NOW(), NOW());

--portals monthly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000011, 4000003, 200004, 1, NOW(), NOW());






INSERT INTO landlord.customer (id, description, name, taxid, paymentmethods, customertype, invoiceemail, preferences, files, address, location, managementunits, created, updated, paymentdetails, pricemultiplier, customersize)
VALUES (2000004, null, 'Viersener Aktien-Baugesellschaft AG', null, '[{"method":"INVOICE","preferred": true}]', 'PROPERTYMANAGEMENT', 'marcus.rautzenberg@vab-viersen.de', '{}', '[]', '{"city": "Viersen", "region": "Nordrhein-Westfalen", "street": "Rathausmarkt", "country": "DE", "zipCode": "41747", "additional": null, "coordinates": null, "houseNumber": "1"}', 'DE', 3600, NOW(), NOW(), '{}', 2, 'MEDIUM');

INSERT INTO landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated, preferences, profile, type)
VALUES (3000007, 'marcus.rautzenberg@vab-viersen.de', null, 2000004, true, false, false, null, NOW(), NOW(), '{}', '{"name": "Rautzenberg", "phone": "", "title": null, "gender": "MALE", "portrait": null, "firstname": "Marcus"}', 'COMPANYADMIN');

INSERT INTO landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated, preferences, profile, type)
VALUES (3000008, 'njacobi+23@immomio.de', null, 2000004, true, false, false, null, NOW(), NOW(), '{}', '{"name": "Jacobi", "phone": "", "title": null, "gender": "MALE", "portrait": null, "firstname": "Nico"}', 'COMPANYADMIN');

INSERT INTO shared.discount (id, name, startdate, enddate, created, updated, value)
VALUES (7000003, 'Viersener Beta Discount', NOW(), NOW() + interval '100 year', NOW(), NOW(), 0.0909);

INSERT INTO landlord.discount_customer (discount_id, customer_id)
VALUES (7000003, 2000004);

INSERT INTO landlord.productbasket (id, customer_id, status, properties, checkoutdate, created, updated, trial)
VALUES (4000004, 2000004, 'PENDING', '{}', null, NOW(), NOW(), false);

--yearly customer product
INSERT INTO landlord.productbasket_product (id, productbasket_id, product_id, quantity, created, updated)
VALUES (5000004, 4000004, 100001, 1, NOW(), NOW());

--10Agents yearly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000012, 4000004, 200015, 5, NOW(), NOW());

--email editor yearly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000013, 4000004, 200001, 1, NOW(), NOW());

--Branding yearly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000014, 4000004, 200009, 1, NOW(), NOW());

--portals yearly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000015, 4000004, 200005, 1, NOW(), NOW());

--FTP inventory import yearly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000016, 4000004, 200011, 1, NOW(), NOW());





INSERT INTO landlord.customer (id, description, name, taxid, paymentmethods, customertype, invoiceemail, preferences, files, address, location, managementunits, created, updated, paymentdetails, pricemultiplier, customersize)
VALUES (2000005, null, 'Wohnbau GmbH', null, '[{"method":"INVOICE","preferred": true}]', 'PROPERTYMANAGEMENT', 'stefan.henseler@wohnbau-service.de', '{}', '[]', '{"city": "Bonn", "region": "Nordrhein-Westfalen", "street": "Philosophenring", "country": "DE", "zipCode": "53177", "additional": null, "coordinates": null, "houseNumber": "2"}', 'DE', 21890, NOW(), NOW(), '{}', 11, 'ENTERPRISE');

INSERT INTO landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated, preferences, profile, type)
VALUES (3000009, 'stefan.henseler@wohnbau-service.de', null, 2000005, true, false, false, null, NOW(), NOW(), '{}', '{"name": "Henseler", "phone": "", "title": null, "gender": "MALE", "portrait": null, "firstname": "Stefan"}', 'COMPANYADMIN');

INSERT INTO landlord."user" (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated, preferences, profile, type)
VALUES (3000010, 'njacobi+24@immomio.de', null, 2000005, true, false, false, null, NOW(), NOW(), '{}', '{"name": "Jacobi", "phone": "", "title": null, "gender": "MALE", "portrait": null, "firstname": "Nico"}', 'COMPANYADMIN');

INSERT INTO shared.discount (id, name, startdate, enddate, created, updated, value)
VALUES (7000004, 'Wohnbau GmbH Beta Discount', NOW(), NOW() + interval '100 year', NOW(), NOW(), 0.5982);

INSERT INTO landlord.discount_customer (discount_id, customer_id)
VALUES (7000004, 2000005);

INSERT INTO landlord.productbasket (id, customer_id, status, properties, checkoutdate, created, updated, trial)
VALUES (4000005, 2000005, 'PENDING', '{}', null, NOW(), NOW(), false);

--yearly customer product
INSERT INTO landlord.productbasket_product (id, productbasket_id, product_id, quantity, created, updated)
VALUES (5000005, 4000005, 100001, 1, NOW(), NOW());

--10Agents yearly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000017, 4000005, 200015, 10, NOW(), NOW());

--email editor yearly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000018, 4000005, 200001, 1, NOW(), NOW());

--Branding yearly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000019, 4000005, 200009, 1, NOW(), NOW());

--portals yearly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000020, 4000005, 200005, 1, NOW(), NOW());

--FTP inventory import yearly
INSERT INTO landlord.productbasket_productaddon (id, productbasket_id, productaddon_id, quantity, created, updated)
VALUES (6000021, 4000005, 200011, 1, NOW(), NOW());









