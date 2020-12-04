
-- CUSTOMER
INSERT INTO landlord.customer (id, description, name, taxid, paymentmethods, customertype, invoiceemail, preferences, files, address, location, managementunits, created, updated, paymentdetails, pricemultiplier, customersize)
VALUES (
    1070513,
    null,
    'Immomio Demo',
    'DE297626954',
    ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
    'ESTATEAGENT',
    'alunt+43@immomio.de',
    '{}',
    '[]',
    '{"city": "Hamburg", "region": "Hamburg", "street": "Grosser Burstah ", "country": "DE", "zipCode": "20457", "additional": null, "coordinates": null, "houseNumber": "25"}',
    'DE',
    null,
    NOW(),
    NOW(),
    '{"STRIPE_CUSTOMERID": "1070513"}',
    1,
    'SMALL'
);

-- USER
INSERT INTO landlord."user" (id,email,password,customer_id,enabled,expired,locked,lastlogin,created,updated,preferences,profile,type)
VALUES (
    1080123,
    'njacobi+1@immomio.de',
    null,
    1070513,
    true,
    false,
    false,
    null,
    NOW(),
    NOW(),
    '{}',
    '{"name": "Jacobi","phone": "040882159890","title": "NONE","gender": "MALE","portrait": null,"firstname": "Nicolas"}','COMPANYADMIN'
);

INSERT INTO landlord."user" (id,email,password,customer_id,enabled,expired,locked,lastlogin,created,updated,preferences,profile,type)
VALUES (
    1072761,
    'alunt+45@immomio.de',
    null,
    1070513,
    false,
    false,
    false,
    null,
    NOW(),
    NOW(),
    '{}',
    '{"name": "Tengel","phone": "","title": null,"gender": "INTERSEXUAL","portrait": null,"firstname": "Valerie"}','EMPLOYEE'
);

INSERT INTO landlord."user" (id,email,password,customer_id,enabled,expired,locked,lastlogin,created,updated,preferences,profile,type)
VALUES (
    1072764,
    'alunt+46@immomio.de',
    null,
    1070513,
    false,
    false,
    false,
    null,
    NOW(),
    NOW(),
    '{}',
    '{"name": "Humboltd","phone": "","title": null,"gender": "FEMALE","portrait": null,"firstname": "Maria"}','EMPLOYEE'
);

INSERT INTO landlord."user" (id,email,password,customer_id,enabled,expired,locked,lastlogin,created,updated,preferences,profile,type)
VALUES (
    1072767,
    'alunt+47@immomio.de',
    null,
    1070513,
    true,
    false,
    false,
    null,
    NOW(),
    NOW(),
    '{}',
    '{"name": "Rubbel ","phone": "","title": null,"gender": "NA","portrait": null,"firstname": "Peter"}','EMPLOYEE'
);

INSERT INTO landlord."user" (id,email,password,customer_id,enabled,expired,locked,lastlogin,created,updated,preferences,profile,type)
VALUES (
    1070514,
    'alunt+43@immomio.de',
    null,
    1070513,
    true,
    false,
    false,
    null,
    NOW(),
    NOW(),
    '{}',
    '{"name": "Demo","phone": null,"title": null,"gender": null,"portrait": null,"firstname": "Immomio"}','COMPANYADMIN'
);

INSERT INTO landlord."user" (id,email,password,customer_id,enabled,expired,locked,lastlogin,created,updated,preferences,profile,type)
VALUES (
    1072776,
    'alunt+48@immomio.de',
    null,
    1070513,
    false,
    false,
    false,
    null,
    NOW(),
    NOW(),
    '{}',
    '{"name": "Link","phone": "","title": null,"gender": "FEMALE","portrait": null,"firstname": "Fransiska "}','EMPLOYEE'
);

INSERT INTO landlord."user" (id,email,password,customer_id,enabled,expired,locked,lastlogin,created,updated,preferences,profile,type)
VALUES (
    1072779,
    'alunt+49@immomio.de',
    null,
    1070513,
    false,
    false,
    false,
    null,
    NOW(),
    NOW(),
    '{}',
    '{"name": "Stahlkopf","phone": "","title": null,"gender": "MALE","portrait": null,"firstname": "Nikolas"}','EMPLOYEE'
);

INSERT INTO landlord."user" (id,email,password,customer_id,enabled,expired,locked,lastlogin,created,updated,preferences,profile,type)
VALUES (
    1072782,
    'alunt+50@immomio.de',
    null,
    1070513,
    false,
    false,
    false,
    null,
    NOW(),
    NOW(),
    '{}',
    '{"name": "Erhmann","phone": "","title": null,"gender": "FEMALE","portrait": null,"firstname": "Johannes"}','EMPLOYEE'
);

INSERT INTO landlord."user" (id,email,password,customer_id,enabled,expired,locked,lastlogin,created,updated,preferences,profile,type)
VALUES (
    1083695,
    'njacobi+2@immomio.de',
    null,
    1070513,
    false,
    false,
    false,
    null,
    NOW(),
    NOW(),
    '{}',
    '{"name": "Meister","phone": "040882159890","title": "NONE","gender": "MALE","portrait": null,"firstname": "Moritz"}','COMPANYADMIN'
);

INSERT INTO landlord."user" (id,email,password,customer_id,enabled,expired,locked,lastlogin,created,updated,preferences,profile,type)
VALUES (
    1072758,
    'alunt+44@immomio.de',
    null,
    1070513,
    true,
    false,
    false,
    null,
    NOW(),
    NOW(),
    '{}',
    '{"name": "Helm","phone": "","title": null,"gender": "MALE","portrait": null,"firstname": "Christian "}','EMPLOYEE'
);

-- PRIOSET
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1084407,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 0, "children": 10, "residents": {"value": 10, "number": 2}, "householdType": {"value": 10, "choice": ["COUPLE_WITHOUT_CHILDREN"]}, "monthlyIncome": {"value": 10, "lowerBound": 3, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Paar ohne Kinder', 'Voreinstellung für "Paare mit Kindern"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1084427,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 0, "children": 10, "residents": {"value": 10, "number": 2}, "householdType": {"value": 10, "choice": ["COUPLE_WITHOUT_CHILDREN"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Paar ohne Kinder', 'Voreinstellung für "Paare mit Kindern"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1084429,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 0, "children": 10, "residents": {"value": 10, "number": 2}, "householdType": {"value": 10, "choice": ["COUPLE_WITHOUT_CHILDREN"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Paar ohne Kinder', 'Voreinstellung für "Paare mit Kindern"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1084461,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 5, "children": 0, "residents": {"value": 5, "number": 5}, "householdType": {"value": 10, "choice": ["SINGLE_WITH_CHILDREN", "FAMILY"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Familie', 'Voreinstellung für "Familien"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1084469,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": null, "music": null, "animals": 0, "smoking": 0, "children": 0, "residents": {"value": 0, "number": 0}, "householdType": {"value": 0, "choice": []}, "monthlyIncome": {"value": 0, "lowerBound": 0, "upperBound": 0}, "employmentType": {"value": 0, "choice": []}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Kleine Wohnung', 'aa',
	false,
	true
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1085215,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 5, "children": 0, "residents": {"value": 5, "number": 5}, "householdType": {"value": 10, "choice": ["SINGLE_WITH_CHILDREN", "FAMILY"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Familie', 'Voreinstellung für "Familien"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1085058,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 5, "children": 0, "residents": {"value": 5, "number": 5}, "householdType": {"value": 10, "choice": ["SINGLE_WITH_CHILDREN", "FAMILY"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Familie', 'Voreinstellung für "Familien"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1085066,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 5, "children": 0, "residents": {"value": 5, "number": 5}, "householdType": {"value": 10, "choice": ["SINGLE_WITH_CHILDREN", "FAMILY"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Familie', 'Voreinstellung für "Familien"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1094341,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 0, "children": 10, "residents": {"value": 10, "number": 2}, "householdType": {"value": 10, "choice": ["COUPLE_WITHOUT_CHILDREN"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Paar ohne Kinder', 'Voreinstellung für "Paare mit Kindern"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1080136,
	1070513,
	'{"age": {"value": 0, "lowerBound": 1, "upperBound": 1}, "wbs": false, "music": null, "animals": 10, "smoking": 10, "children": 10, "residents": {"value": 10, "number": 4}, "householdType": {"value": 10, "choice": ["STUDENT"]}, "monthlyIncome": {"value": 10, "lowerBound": 3, "upperBound": 10}, "employmentType": {"value": 4, "choice": ["SELF_EMPLOYED", "CIVIL_SERVANT", "EMPLOYED_UNLIMITED"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'test template true 2', 'test template true 2',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1085287,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 5, "children": 0, "residents": {"value": 5, "number": 5}, "householdType": {"value": 10, "choice": ["SINGLE_WITH_CHILDREN", "FAMILY"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Familie', 'Voreinstellung für "Familien"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1094378,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 0, "children": 10, "residents": {"value": 10, "number": 2}, "householdType": {"value": 10, "choice": ["COUPLE_WITHOUT_CHILDREN"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Paar ohne Kinder', 'Voreinstellung für "Paare mit Kindern"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1081309,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": null, "music": null, "animals": 0, "smoking": 0, "children": 0, "residents": {"value": 0, "number": 0}, "householdType": {"value": 0, "choice": []}, "monthlyIncome": {"value": 7, "lowerBound": 2, "upperBound": 9}, "employmentType": {"value": 0, "choice": []}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Single Wohnung', 'Für kleine Wohnungen',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1085310,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 5, "children": 0, "residents": {"value": 5, "number": 5}, "householdType": {"value": 10, "choice": ["SINGLE_WITH_CHILDREN", "FAMILY"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Familie', 'Voreinstellung für "Familien"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1085322,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 5, "children": 0, "residents": {"value": 5, "number": 5}, "householdType": {"value": 10, "choice": ["SINGLE_WITH_CHILDREN", "FAMILY"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Familie', 'Voreinstellung für "Familien"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1085331,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 5, "children": 0, "residents": {"value": 5, "number": 5}, "householdType": {"value": 10, "choice": ["SINGLE_WITH_CHILDREN", "FAMILY"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Familie', 'Voreinstellung für "Familien"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1085353,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 0, "children": 10, "residents": {"value": 10, "number": 2}, "householdType": {"value": 10, "choice": ["COUPLE_WITHOUT_CHILDREN"]}, "monthlyIncome": {"value": 10, "lowerBound": 2, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Paar ohne Kinder', 'Voreinstellung für "Paare mit Kindern"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1085362,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 5, "children": 0, "residents": {"value": 5, "number": 5}, "householdType": {"value": 10, "choice": ["SINGLE_WITH_CHILDREN", "FAMILY"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Familie', 'Voreinstellung für "Familien"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1082474,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 0, "children": 6, "residents": {"value": 0, "number": 0}, "householdType": {"value": 10, "choice": ["FLATSHARE"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 5, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT", "STUDENT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Wohngemeinschaft/en', 'Voreinstellung für "Wohngemeinschaft/en"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1085371,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 5, "children": 0, "residents": {"value": 5, "number": 5}, "householdType": {"value": 10, "choice": ["SINGLE_WITH_CHILDREN", "FAMILY"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Familie', 'Voreinstellung für "Familien"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1085380,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 5, "children": 0, "residents": {"value": 5, "number": 5}, "householdType": {"value": 10, "choice": ["SINGLE_WITH_CHILDREN", "FAMILY"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Familie', 'Voreinstellung für "Familien"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1083722,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": null, "music": null, "animals": 0, "smoking": 0, "children": 0, "residents": {"value": 0, "number": 0}, "householdType": {"value": 0, "choice": []}, "monthlyIncome": {"value": 0, "lowerBound": 0, "upperBound": 0}, "employmentType": {"value": 0, "choice": []}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Winterhude', 'Test',
	false,
	true
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1083723,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": null, "music": null, "animals": 0, "smoking": 0, "children": 0, "residents": {"value": 0, "number": 0}, "householdType": {"value": 0, "choice": []}, "monthlyIncome": {"value": 0, "lowerBound": 0, "upperBound": 0}, "employmentType": {"value": 0, "choice": []}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Mailand Quartier', 'aa',
	false,
	true
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1090922,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 0, "children": 10, "residents": {"value": 10, "number": 2}, "householdType": {"value": 10, "choice": ["COUPLE_WITHOUT_CHILDREN"]}, "monthlyIncome": {"value": 10, "lowerBound": 2, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Paar ohne Kinder', 'Voreinstellung für "Paare mit Kindern"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1083731,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 0, "children": 10, "residents": {"value": 10, "number": 2}, "householdType": {"value": 10, "choice": ["COUPLE_WITHOUT_CHILDREN"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Paar ohne Kinder', 'Voreinstellung für "Paare mit Kindern"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1083733,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 0, "children": 10, "residents": {"value": 10, "number": 2}, "householdType": {"value": 10, "choice": ["COUPLE_WITHOUT_CHILDREN"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Paar ohne Kinder', 'Voreinstellung für "Paare mit Kindern"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1083735,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 0, "children": 10, "residents": {"value": 10, "number": 2}, "householdType": {"value": 10, "choice": ["COUPLE_WITHOUT_CHILDREN"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Paar ohne Kinder', 'Voreinstellung für "Paare mit Kindern"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1090924,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 0, "children": 10, "residents": {"value": 10, "number": 2}, "householdType": {"value": 10, "choice": ["COUPLE_WITHOUT_CHILDREN"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Paar ohne Kinder', 'Voreinstellung für "Paare mit Kindern"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1082466,
	1070513,
	'{"age": {"value": 4, "lowerBound": 9, "upperBound": 14}, "wbs": true, "music": null, "animals": 5, "smoking": 5, "children": 5, "residents": {"value": 7, "number": 3}, "householdType": {"value": 5, "choice": ["COUPLE_WITHOUT_CHILDREN", "STUDENT", "SINGLE_WITH_CHILDREN", "COUPLE", "FAMILY", "FLATSHARE", "SINGLE"]}, "monthlyIncome": {"value": 5, "lowerBound": 2, "upperBound": 9}, "employmentType": {"value": 5, "choice": ["CIVIL_SERVANT", "HOUSEHOLD_MANAGER", "APPRENTICE", "RETIRED", "LOOKING_FOR_WORK"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Some test with template equals true22 2', 'Some test with template equals true22',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1091315,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 5, "children": 0, "residents": {"value": 5, "number": 5}, "householdType": {"value": 10, "choice": ["SINGLE_WITH_CHILDREN", "FAMILY"]}, "monthlyIncome": {"value": 10, "lowerBound": 2, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'Familie', 'Voreinstellung für "Familien"',
	false,
	false
);
INSERT INTO landlord.prioset (id, customer_id, data, created, updated, name, description, locked, template)
VALUES (
	1083206,
	1070513,
	'{"age": {"value": 0, "lowerBound": 0, "upperBound": 0}, "wbs": false, "music": null, "animals": 0, "smoking": 5, "children": 0, "residents": {"value": 5, "number": 5}, "householdType": {"value": 10, "choice": ["SINGLE_WITH_CHILDREN", "FAMILY"]}, "monthlyIncome": {"value": 10, "lowerBound": 1, "upperBound": 5}, "employmentType": {"value": 7, "choice": ["SELF_EMPLOYED", "EMPLOYED_LIMITED", "EMPLOYED_UNLIMITED", "CIVIL_SERVANT"]}, "personalStatus": null}',
	NOW(),
	NOW(),
	'PRIOSET_FAMILY_NAME 2', 'PRIOSET_FAMILY_DESCRIPTION',
	false,
	false
);

-- PROPERTY

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084269,
	1070513,
	'{"name": "Gemütliche 2 Zi-Dachgeschosswohnung sucht nette Mieter", "size": 59.5, "floor": 1, "rooms": 2.0, "garden": false, "ground": null, "heater": "CENTRAL", "address": {"city": "Morsum/Sylt", "region": "Schleswig-Holstein", "street": "Gurtmuasem", "country": "DE", "zipCode": "25980", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "2 d"}, "balcony": false, "contact": {"name": "Putzler", "email": "hausbetreuung-putzler@t-online.de", "phone": null, "mobile": "0171-6253514", "address": {"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}, "firstName": null}, "bailment": 1261.0, "elevator": false, "flatType": "ROOF_STOREY", "basePrice": 420.66, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-679b060ff0bd4c2b90b202134dc8476c.jpg", "type": "IMG", "title": "Übergabe Herbst 04 001.JPG", "encrypted": false, "extension": "jpeg", "identifier": "679b060ff0bd4c2b90b202134dc8476c"}], "barrierFree": false, "heatingCost": 190.0, "kitchenette": false, "referenceId": null, "showAddress": false, "showContact": true, "basementSize": null, "parkingSpace": false, "availableFrom": "Tue Nov 01 13:44:45 CET 2016", "guestToilette": false, "serviceCharge": 130.0, "historicBuilding": false, "shortDescription": "Die Wohnungen wurden 2004 aufwendig und hochwertig erbaut. Das Badezimmer ist weiß gefliest. Die Küche ist mit einem Herd und einem Spülenschrank ausgestattet. \nZu der Wohnung gehört ein Dachboden.", "basementAvailable": false, "buildingCondition": null, "energyCertificate": {"creationDate": "APRIL_2014", "usageCertificate": {"energyConsumption": null, "energyEfficiencyClass": null, "inludesHeatConsumption": true, "energyConsumptionParameter": "92"}, "demandCertificate": null, "yearOfConstruction": 2004, "energyCertificateType": "USAGE_IDENTIFICATION", "primaryEnergyProvider": "GAS"}, "lastRefurbishment": null, "objectDescription": null, "objectLocationText": "Ruhige Lage in Sylt/Morsum.", "heatingCostIncluded": false, "objectMiscellaneousText": "Die Mieterhöhung erfolgt über eine Staffel; die Nettokaltmiete erhöht sich alle 3 Jahre um 9%. Die Wohnung wird nur an Interessenten mit Wohnberechtigungsschein vermietet"}',
	1085066,
	NOW(),
	NOW(),
	1080123,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1085067,
	1070513,
	'{"name": "4 Zimmer in Berlin", "size": 98.0, "floor": null, "rooms": 4.0, "garden": false, "ground": null, "heater": null, "address": {"city": "Berlin", "region": "Berlin", "street": "Chausseestr. ", "country": "DE", "zipCode": "10115", "additional": null, "coordinates": null, "houseNumber": "100"}, "balcony": false, "contact": null, "bailment": null, "elevator": false, "flatType": null, "basePrice": 750.0, "bathRooms": null, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-a5eba80e90f746aa883af245c1d6f9a9.jpg", "type": "IMG", "title": null, "encrypted": false, "extension": "jpg", "identifier": "a5eba80e90f746aa883af245c1d6f9a9"}], "barrierFree": false, "heatingCost": null, "kitchenette": false, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": null, "guestToilette": false, "serviceCharge": null, "historicBuilding": false, "shortDescription": null, "basementAvailable": null, "buildingCondition": null, "energyCertificate": null, "lastRefurbishment": null, "objectDescription": null, "objectLocationText": null, "heatingCostIncluded": false, "objectMiscellaneousText": null}',
	1085066,
	NOW(),
	NOW(),
	1070514,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1085288,
	1070513,
	'{"name": "4 Zimmer in Berlin", "size": 80.0, "floor": null, "rooms": 4.0, "garden": false, "ground": null, "heater": null, "address": {"city": "Berlin", "region": "Berlin", "street": "Chausseestr. ", "country": "DE", "zipCode": "10115", "additional": null, "coordinates": null, "houseNumber": "100"}, "balcony": false, "contact": null, "bailment": null, "elevator": false, "flatType": null, "basePrice": 500.0, "bathRooms": null, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-37d28ec76e8342fda53267d07bde34eb.jpg", "type": "IMG", "title": null, "encrypted": false, "extension": "jpg", "identifier": "37d28ec76e8342fda53267d07bde34eb"}], "barrierFree": false, "heatingCost": null, "kitchenette": false, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": null, "guestToilette": false, "serviceCharge": null, "historicBuilding": false, "shortDescription": null, "basementAvailable": null, "buildingCondition": null, "energyCertificate": null, "lastRefurbishment": null, "objectDescription": null, "objectLocationText": null, "heatingCostIncluded": false, "objectMiscellaneousText": null}',
	1085287,
	NOW(),
	NOW(),
	1070514,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1081310,
	1070513,
	'{"name": "3.5 Zimmer in Hamburg", "size": 60.0, "floor": null, "rooms": 3.5, "garden": false, "ground": null, "heater": null, "address": {"city": "Hamburg", "region": "Hamburg", "street": "Blumenstrasse", "country": "DE", "zipCode": "22301", "additional": null, "coordinates": null, "houseNumber": "26"}, "balcony": false, "contact": null, "bailment": null, "elevator": false, "flatType": null, "basePrice": 900.0, "bathRooms": null, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-0706d9f293c94dfbaed13e4f46663eeb.jpg", "type": "IMG", "title": null, "encrypted": false, "extension": "jpg", "identifier": "0706d9f293c94dfbaed13e4f46663eeb"}], "barrierFree": false, "heatingCost": null, "kitchenette": false, "referenceId": null, "showAddress": true, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": null, "guestToilette": false, "serviceCharge": null, "historicBuilding": false, "shortDescription": null, "basementAvailable": null, "buildingCondition": null, "energyCertificate": null, "lastRefurbishment": null, "objectDescription": null, "objectLocationText": null, "heatingCostIncluded": false, "objectMiscellaneousText": null}',
	1081309,
	NOW(),
	NOW(),
	1070514,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1085354,
	1070513,
	'{"name": "2 Zimmer in Hamburg", "size": 60.0, "floor": null, "rooms": 2.0, "garden": false, "ground": null, "heater": null, "address": {"city": "Hamburg", "region": "Hamburg", "street": "Hofweg", "country": "DE", "zipCode": "22085", "additional": null, "coordinates": null, "houseNumber": "10"}, "balcony": false, "contact": null, "bailment": null, "elevator": false, "flatType": null, "basePrice": 600.0, "bathRooms": null, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-823ec633981141edacd1b566a5055430.jpg", "type": "IMG", "title": null, "encrypted": false, "extension": "jpg", "identifier": "823ec633981141edacd1b566a5055430"}], "barrierFree": false, "heatingCost": null, "kitchenette": false, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": null, "guestToilette": false, "serviceCharge": null, "historicBuilding": false, "shortDescription": null, "basementAvailable": null, "buildingCondition": null, "energyCertificate": null, "lastRefurbishment": null, "objectDescription": null, "objectLocationText": null, "heatingCostIncluded": false, "objectMiscellaneousText": null}',
	1085353,
	NOW(),
	NOW(),
	1070514,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084224,
	1070513,
	'{"name": "Schöne 3 Zimmer im Herzen von Eppendorf", "size": 82.53, "floor": 1, "rooms": 3.0, "garden": false, "ground": "PARQUET", "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Schrammsweg", "country": "DE", "zipCode": "20249", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "27"}, "balcony": true, "contact": null, "bailment": 2574.0, "elevator": false, "flatType": "FLOOR", "basePrice": 858.0, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-f7fe177855ec4e5d83ed4bd4d32401ab.jpg", "type": "IMG", "title": "090508.jpg", "encrypted": false, "extension": "jpeg", "identifier": "f7fe177855ec4e5d83ed4bd4d32401ab"}], "barrierFree": false, "heatingCost": 80.0, "kitchenette": true, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": "Wed Jun 01 09:14:43 CEST 2016", "guestToilette": true, "serviceCharge": 129.0, "historicBuilding": false, "shortDescription": "Die hier zur Vermietung stehende Wohnung zeichnet sich durch Ihren guten Grundriss und Ihre Helligkeit aus. \n\nDie Wohnung verfügt über einen großen Balkon, eine moderne Einbauküche ist Bestandteil der Mietsache. \n\nDie Wohnung verfügt über ein hell gefliestes Vollbad und ein separates Gäste WC.", "basementAvailable": false, "buildingCondition": null, "energyCertificate": {"creationDate": "APRIL_2014", "usageCertificate": {"energyConsumption": null, "energyEfficiencyClass": null, "inludesHeatConsumption": true, "energyConsumptionParameter": "187"}, "demandCertificate": null, "yearOfConstruction": 1968, "energyCertificateType": "USAGE_IDENTIFICATION", "primaryEnergyProvider": "OIL"}, "lastRefurbishment": null, "objectDescription": null, "objectLocationText": "Die Immobilie befindet sich im Herzen von Eppendorf, die U-Bahn Station Kellinghusen ist fußläufig zu erreichen.  Durch die Nähe zur Eppendorfer Landstaße und dem Eppendorfer Marktplatz, sind viele Einkaufsmöglichkeiten sowie Cafés und Ärzte schnell fußläufig erreichbar.", "heatingCostIncluded": false, "objectMiscellaneousText": "Die Vorauszahlungen für Wasser- und Strom werden direkt an die Versorger geleistet.\n\nBei Vertragsabschluss ist eine Gebühr in Höhe von € 150,00 zzgl. MwSt. zu zahlen.\n\nIm Mietvertrag wird eine Anpassung der Miete über Lebenshaltungskostenindex vereinbart."}',
	1085353,
	NOW(),
	NOW(),
	1072758,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084226,
	1070513,
	'{"name": "sonnige 2 Zi-DG-Wohnung in Eppendorf", "size": 53.5, "floor": 4, "rooms": 2.0, "garden": false, "ground": null, "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Schrammsweg", "country": "DE", "zipCode": "20249", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "25"}, "balcony": false, "contact": null, "bailment": 2097.0, "elevator": false, "flatType": "ROOF_STOREY", "basePrice": 699.9, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-e326a258511d4389bfa65b6465dc3cb6.jpg", "type": "IMG", "title": "IMG_1037.JPG", "encrypted": false, "extension": "jpeg", "identifier": "e326a258511d4389bfa65b6465dc3cb6"}], "barrierFree": false, "heatingCost": 50.0, "kitchenette": true, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": null, "guestToilette": false, "serviceCharge": 65.0, "historicBuilding": false, "shortDescription": "Die Wohnung befindet sich in einem Mehrfamilienhaus in einer ruhigen Seitenstraße.", "basementAvailable": false, "buildingCondition": null, "energyCertificate": {"creationDate": null, "usageCertificate": null, "demandCertificate": null, "yearOfConstruction": 1900, "energyCertificateType": "NO_AVAILABLE", "primaryEnergyProvider": "LONG_DISTANCE"}, "lastRefurbishment": null, "objectDescription": "Die Wohnräume sind mit Stäbchenparkett ausgestattet. Vom Flur geht ein Abstellraum ab. Die Wohnräume sind weiß gestrichen. Im Bad ist ein Waschmaschinenanschluss vorhanden.", "objectLocationText": "Das Objekt liegt in Eppendorf in der Nähe der Bahnstation Kellinghusenstraße (U1 & U3). Durch die Nähe zur Eppendorfer Landstraße und dem Eppendorfer Marktplatz, sind viele Einkaufmöglichkeiten sowie Cafés und Ärzte schnell fußläufig erreichbar.", "heatingCostIncluded": false, "objectMiscellaneousText": "Die Vorauszahlungen für die Wasser- und Stromversorgung sind direkt an die entsprechenden Unternehmen abzurechnen. Bei Vertragsabschluss ist eine Gebühr in Höhe von € 150,00 zzgl. MwSt. zu zahlen. Im Mietvertrag wird eine Indexmiete vereinbart."}',
	1085353,
	NOW(),
	NOW(),
	1072767,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084228,
	1070513,
	'{"name": "schöne 2,5 Zi-Wohnung mit Garten", "size": 52.33, "floor": 1, "rooms": 3.0, "garden": true, "ground": "LAMINATE", "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Begonienweg", "country": "DE", "zipCode": "22047", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "6"}, "balcony": false, "contact": {"name": null, "email": "info@gcv-gmbh.de", "phone": "+4940226480", "mobile": null, "address": {"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}, "firstName": null}, "bailment": 1374.0, "elevator": false, "flatType": "FLOOR", "basePrice": 465.0, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-b23acbaa6b1640ec8ea656961441c644.jpg", "type": "IMG", "title": "IMG_1820.JPG", "encrypted": false, "extension": "jpeg", "identifier": "b23acbaa6b1640ec8ea656961441c644"}], "barrierFree": false, "heatingCost": 90.0, "kitchenette": true, "referenceId": null, "showAddress": false, "showContact": true, "basementSize": null, "parkingSpace": false, "availableFrom": "Thu Dec 01 18:29:37 CET 2016", "guestToilette": false, "serviceCharge": 100.0, "historicBuilding": false, "shortDescription": "Es handelt sich bei dem, Objekt um ein gepflegtes Mehrfamilienhaus in einer ruhigen Wohnanlage. Zu der Wohnung gehört ein abgetrennter Teil des Gartens, der von jedem Mieter selbst zu pflegen ist.", "basementAvailable": true, "buildingCondition": null, "energyCertificate": {"creationDate": "APRIL_2014", "usageCertificate": null, "demandCertificate": {"endEnergyConsumption": "179", "energyEfficiencyClass": null}, "yearOfConstruction": 1930, "energyCertificateType": "DEMAND_IDENTIFICATION", "primaryEnergyProvider": "GAS"}, "lastRefurbishment": null, "objectDescription": "Die Wohnung  befindet sich in einem gepflegten Zustand.  Ausgestattet ist die Wohnung mit einem schönen Laminat, einem hell gefliesten Vollbad mit Fenster und einem geräumigen Wohnzimmer. Die Küche ist mit einer Einbauküche ausgestattet, welche zur Nutzung überlassen wird.", "objectLocationText": "Das Objekt liegt in einer wenig befahrenen Seitenstraße in einer grünen Umgebung. Die nah gelegene Bushaltstelle bietet bei Bedarf eine gute Anbindung in die Hamburger Innenstadt. Einkaufsmöglichkeiten für den täglichen Bedarf befinden sich in direkter Umgebung.", "heatingCostIncluded": false, "objectMiscellaneousText": null}',
	1085353,
	NOW(),
	NOW(),
	1072767,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084222,
	1070513,
	'{"name": "Zwischen Alster und Eppendorf, 3 Zimmer in Harvestehude suchen neuen Bewohner", "size": 92.0, "floor": 2, "rooms": 3.0, "garden": false, "ground": "PARQUET", "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "St. Benedictstraße", "country": "DE", "zipCode": "20149", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "38"}, "balcony": true, "contact": null, "bailment": 3552.0, "elevator": false, "flatType": "FLOOR", "basePrice": 1184.0, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-a6d455fae55c444281e8e17c6f12991c.jpg", "type": "IMG", "title": "IMG_0996.JPG", "encrypted": false, "extension": "jpeg", "identifier": "a6d455fae55c444281e8e17c6f12991c"}], "barrierFree": false, "heatingCost": null, "kitchenette": true, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": true, "availableFrom": "Wed Jun 01 07:31:55 CEST 2016", "guestToilette": true, "serviceCharge": 280.0, "historicBuilding": false, "shortDescription": "Die zu vermietende Wohnung befindet sich in einer 1970 erbauten, laufend gepflegten Eigentumsanlage.\n\nDie Wohnung wurde Anfang 2000 umfangreich modernisiert. Die Elektroinstallationen, sowie das Vollbad und separate WC wurden komplett modernisiert und mit hochwertigen Fliesen, Sanitärobjekten und Möbeln ausgestattet. Eine vollständig ausgestattet Einbauküche mit Elektrogeräten rundet das Bild ab. Die Wohnung wird vor Übergabe an einen neuen Mieter komplett gestrichen - der Mietbeginn kann sich aufgrund dessen etwas verschieben.", "basementAvailable": true, "buildingCondition": null, "energyCertificate": {"creationDate": "APRIL_2014", "usageCertificate": {"energyConsumption": null, "energyEfficiencyClass": null, "inludesHeatConsumption": true, "energyConsumptionParameter": "187.7"}, "demandCertificate": null, "yearOfConstruction": 1991, "energyCertificateType": "USAGE_IDENTIFICATION", "primaryEnergyProvider": "GAS"}, "lastRefurbishment": null, "objectDescription": "Die Wohnung verfügt über einen schönen Balkon mit Blick ins Grüne.\n\nDie Wohnanlage verfügt über eine Tiefgarage. Ein Stellplatz kann für € 52,00/Monat separat mit angemietet werden.", "objectLocationText": "Die Wohnung befindet sich in einer der beliebtesten Wohngegenden Harvestehudes. Die Alster, der Bolivarpark und der Eppendorfer Baum sind fußläufig schnell zu erreichen.  \nDie Anbindung an die öffentlichen Nahverkehrsmittel ist gegeben.", "heatingCostIncluded": true, "objectMiscellaneousText": null}',
	1085353,
	NOW(),
	NOW(),
	1072758,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084230,
	1070513,
	'{"name": "Schöne 5 1/2 Zimmer Altbauwohnung in bester Lage Eppendorfs", "size": 174.18, "floor": 3, "rooms": 5.5, "garden": false, "ground": "PARQUET", "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Loogestieg", "country": "DE", "zipCode": "20249", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "7"}, "balcony": true, "contact": {"name": "Reiher", "email": "dreiher@gcv-gmbh.de", "phone": "+4940226480", "mobile": null, "address": {"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}, "firstName": null}, "bailment": 6786.0, "elevator": true, "flatType": "FLOOR", "basePrice": 2262.0, "bathRooms": 2, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-1fcf13641e3f42339b8089f5ad14285c.jpg", "type": "IMG", "title": "006.JPG", "encrypted": false, "extension": "jpeg", "identifier": "1fcf13641e3f42339b8089f5ad14285c"}], "barrierFree": false, "heatingCost": null, "kitchenette": true, "referenceId": null, "showAddress": true, "showContact": true, "basementSize": null, "parkingSpace": false, "availableFrom": "Sun Jan 01 11:32:18 CET 2017", "guestToilette": true, "serviceCharge": 430.0, "historicBuilding": false, "shortDescription": "Klassische Hamburger Altbauwohnung in einem 1912 erbauten 12 Familienhaus,  mit Stuck und hochwertigem Parkettboden. Hohe Decken, langer Flur, zwei hochwertige Bäder (1x Vollbad, 1x Duschbad), sep. WC, schöne Wohnküche, 2 Balkone", "basementAvailable": true, "buildingCondition": null, "energyCertificate": {"creationDate": null, "usageCertificate": null, "demandCertificate": null, "yearOfConstruction": 1912, "energyCertificateType": "NO_AVAILABLE", "primaryEnergyProvider": "LONG_DISTANCE"}, "lastRefurbishment": null, "objectDescription": null, "objectLocationText": "Das Objekt liegt in einer der beliebtesten Wohnstraßen Eppendorfs. Die U-Bahn Kellinghustenstraße ist ca. 10 Gehminuten entfernt, Einkaufsmöglichkeiten, Cafés, Restaurants sowie Grünanlagen (Kellinghusen und Eppendorfer Park) sind alle fußläufig erreichbar.", "heatingCostIncluded": true, "objectMiscellaneousText": "Die Kosten für die Stromversorgung sind nicht in der Miete enthalten. \n\nIm Mietvertrag wird eine Indexmiete vereinbart."}',
	1085353,
	NOW(),
	NOW(),
	1072767,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084220,
	1070513,
	'{"name": "Top sanierte 2 Zi-Wohnung in Eppendorf", "size": 37.25, "floor": 0, "rooms": 2.0, "garden": false, "ground": null, "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Schrammsweg", "country": "DE", "zipCode": "20249", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "27e"}, "balcony": false, "contact": null, "bailment": 1452.0, "elevator": false, "flatType": "FLOOR", "basePrice": 484.0, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-4b20784d5bde46b391c2e781e02e04cd.jpg", "type": "IMG", "title": "Innenhof Mai 2016.jpg", "encrypted": false, "extension": "jpeg", "identifier": "4b20784d5bde46b391c2e781e02e04cd"}], "barrierFree": false, "heatingCost": null, "kitchenette": false, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": "Wed Jun 15 09:42:33 CEST 2016", "guestToilette": false, "serviceCharge": 130.0, "historicBuilding": false, "shortDescription": "Die Wohnung befindet sich in einem Mehrfamilienhaus im ruhiger Hoflage. Die Wohnhäuser werden zurzeit saniert, daher kann es in den kommenden Monaten zu Lärm- und Schmutzbelästigungen kommen.", "basementAvailable": false, "buildingCondition": null, "energyCertificate": {"creationDate": "APRIL_2014", "usageCertificate": {"energyConsumption": null, "energyEfficiencyClass": null, "inludesHeatConsumption": false, "energyConsumptionParameter": "151"}, "demandCertificate": null, "yearOfConstruction": 1900, "energyCertificateType": "USAGE_IDENTIFICATION", "primaryEnergyProvider": "LONG_DISTANCE"}, "lastRefurbishment": null, "objectDescription": "Die Wohnung ist komplett weiß gestrichen, das kl. Duschbad ist weiß gefliest, in der Küche sind Spüle & Herd vorhanden. Zu der Wohnung gehört ein Abstellraum auf dem Dachboden des Hauses. Über die Küche hat man Zugang zur kl. Terrasse.", "objectLocationText": "Das Objekt liegt in Eppendorf in der Nähe der Bahnstation Kellinghusenstraße (U1 & U3), diese ist in ca. 5-10 Gehminuten erreichbar. Durch die Nähe zur Eppendorfer Landstraße und dem Eppendorfer Marktplatz, sind alle Einkaufmöglichkeiten schnell fußläufig erreichbar.", "heatingCostIncluded": true, "objectMiscellaneousText": "Die Vorauszahlungen für die Wasser- und Stromversorgung sind direkt an die entsprechenden Unternehmen abzurechnen. Im Mietvertrag wird eine Indexmiete vereinbart. Bei Vertragsabschluss ist eine Gebühr in Höhe von € 150,00 zzgl. MwSt. zu zahlen."}',
	1085353,
	NOW(),
	NOW(),
	1072758,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084232,
	1070513,
	'{"name": "Top 1 Zi-DG-Wohnung in Eppendorf", "size": 34.32, "floor": 4, "rooms": 1.0, "garden": false, "ground": "PARQUET", "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Schrammsweg", "country": "DE", "zipCode": "20249", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "27"}, "balcony": false, "contact": null, "bailment": 1158.0, "elevator": false, "flatType": "ROOF_STOREY", "basePrice": 386.0, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-349f30f9b9e5428c9e9134585db7f707.jpg", "type": "IMG", "title": "IMG_1128.JPG", "encrypted": false, "extension": "jpeg", "identifier": "349f30f9b9e5428c9e9134585db7f707"}], "barrierFree": false, "heatingCost": 53.0, "kitchenette": true, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": null, "guestToilette": false, "serviceCharge": 62.0, "historicBuilding": false, "shortDescription": "Die Wohnung befindet sich in einem Mehrfamilienhaus in einer ruhigen Seitenstraße.", "basementAvailable": false, "buildingCondition": null, "energyCertificate": {"creationDate": null, "usageCertificate": null, "demandCertificate": null, "yearOfConstruction": 1968, "energyCertificateType": "NO_AVAILABLE", "primaryEnergyProvider": "LONG_DISTANCE"}, "lastRefurbishment": null, "objectDescription": "Der Wohnraum der Wohnung ist mit Stäbchenparkett ausgestattet. Im Wohnraum gibt es einen kleinen Abstellraum/Kleiderschrank. Zu der Wohnung gehört noch ein Abstellraum auf dem Dachboden des Hauses.", "objectLocationText": "Das Objekt liegt in Eppendorf in der Nähe der Bahnstation Kellinghusenstraße (U1 & U3). Durch die Nähe zur Eppendorfer Landstraße und dem Eppendorfer Marktplatz, sind viele Einkaufmöglichkeiten sowie Cafés und Ärzte schnell fußläufig erreichbar.", "heatingCostIncluded": false, "objectMiscellaneousText": "Die Vorauszahlungen für die Wasser- und Stromversorgung sind direkt an die entsprechenden Unternehmen abzurechnen. Bei Vertragsabschluss ist eine Gebühr in Höhe von € 150,00 zzgl. MwSt. zu zahlen. Im Mietvertrag wird eine Indexmiete vereinbart"}',
	1085353,
	NOW(),
	NOW(),
	1072767,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084245,
	1070513,
	'{"name": "Schöne DG-Wohnung in Barmbek-Süd", "size": 62.25, "floor": 4, "rooms": 3.0, "garden": false, "ground": "OTHER", "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Kraepelinweg", "country": "DE", "zipCode": "22081", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "42"}, "balcony": false, "contact": null, "bailment": 1710.0, "elevator": false, "flatType": "ROOF_STOREY", "basePrice": 570.0, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-67e6ba4982544897b338ad22418c6ad5.jpg", "type": "IMG", "title": "IMG_1300.JPG", "encrypted": false, "extension": "jpeg", "identifier": "67e6ba4982544897b338ad22418c6ad5"}], "barrierFree": false, "heatingCost": null, "kitchenette": true, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": "Sat Oct 01 16:05:26 CEST 2016", "guestToilette": true, "serviceCharge": 180.0, "historicBuilding": false, "shortDescription": "Es handelt sich hierbei um ein 4 Geschossiges Mehrfamilienhaus.\nDas Haus wurde in Rotklinkeroptik erbaut und befindet sich in einem gepflegten Zustand.", "basementAvailable": true, "buildingCondition": null, "energyCertificate": {"creationDate": "APRIL_2014", "usageCertificate": {"energyConsumption": null, "energyEfficiencyClass": null, "inludesHeatConsumption": true, "energyConsumptionParameter": "136"}, "demandCertificate": null, "yearOfConstruction": 1939, "energyCertificateType": "USAGE_IDENTIFICATION", "primaryEnergyProvider": "LONG_DISTANCE"}, "lastRefurbishment": null, "objectDescription": "Die Wohnung ist in den Wohnräumen mit Dielenboden ausgestattet. Das Vollbad, das Gäste-WC sowie die Küche sind hell gefliest. Die Wohnung verfügt über einen Abstellraum mit Fenster. Das Wohn- und Esszimmer ist mit einem offenen Durchgang verbunden. Der Waschmaschinenanschluss befindet sich im Badezimmer.", "objectLocationText": "Das Objekt liegt in dem schönen, grünen und familienfreundlichen Stadtteil Barmbek-Süd. Mit gutem Anschluss an die Nahverkehrslinien. Fußläufig ist der nächste Bahnhof Friedrichsberg (Linie S1/S11)  innerhalb von 10 Minuten erreichbar. Die Fahrtzeit zum Hamburger Hauptbahnhof beträgt 10 Minuten. In Barmbek sind alle Einkaufsmöglichkeiten für den täglichen Bedarf sowie Ärzte und Restaurants vorhanden.", "heatingCostIncluded": true, "objectMiscellaneousText": "Die Kosten für das Kabelfernsehen sind in den Nebenkosten enthalten. Wasser- und Stromkosten werden direkt durch den Mieter mit den Versorgungsunternehmen abgerechnet. Im Mietvertrag wird eine Indexmiete vereinbart."}',
	1085353,
	NOW(),
	NOW(),
	1072767,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084249,
	1070513,
	'{"name": "Schöne 2 Zi-Whg. im Schanzenviertel", "size": 53.53, "floor": 0, "rooms": 2.0, "garden": false, "ground": "LAMINATE", "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Amandastraße", "country": "DE", "zipCode": "20357", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "66 a"}, "balcony": false, "contact": null, "bailment": 1873.0, "elevator": false, "flatType": "FLOOR", "basePrice": 624.34, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-d0209adf5f3245709c1b27688aac8a4e.jpg", "type": "IMG", "title": "Haus 66a.JPG", "encrypted": false, "extension": "jpeg", "identifier": "d0209adf5f3245709c1b27688aac8a4e"}], "barrierFree": false, "heatingCost": 50.0, "kitchenette": false, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": "Tue Nov 01 12:27:53 CET 2016", "guestToilette": false, "serviceCharge": 63.0, "historicBuilding": false, "shortDescription": "Gepflegte Wohnung mit franz. Balkon im Hochparterre des Rotklinker-Mehrfamilienhauses von 1956.", "basementAvailable": true, "buildingCondition": null, "energyCertificate": {"creationDate": "APRIL_2014", "usageCertificate": null, "demandCertificate": {"endEnergyConsumption": "159.5", "energyEfficiencyClass": null}, "yearOfConstruction": 1956, "energyCertificateType": "DEMAND_IDENTIFICATION", "primaryEnergyProvider": "LONG_DISTANCE"}, "lastRefurbishment": null, "objectDescription": "Wohnung mit Vollbad und Abstellkammer in der Küche.", "objectLocationText": "Top Lage im Schanzenviertel. Einkaufsmöglichkeiten, Cafés, die öffentlichen Verkehrsmittel etc. sind fußläufig erreichbar.", "heatingCostIncluded": false, "objectMiscellaneousText": "Im Mietvertrag wird eine Indexmiete vereinbart."}',
	1085353,
	NOW(),
	NOW(),
	1072767,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084255,
	1070513,
	'{"name": "Schöne 2 Zi-Wohnung nähe Wandsbek-Markt", "size": 65.0, "floor": 2, "rooms": 2.0, "garden": false, "ground": "OTHER", "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Wandsbeker Zollstraße", "country": "DE", "zipCode": "22041", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "3"}, "balcony": true, "contact": null, "bailment": 2004.0, "elevator": false, "flatType": "FLOOR", "basePrice": 668.0, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-f8317fbc6ab646018e2c31b311239034.jpg", "type": "IMG", "title": "IMG_9401.JPG", "encrypted": false, "extension": "jpeg", "identifier": "f8317fbc6ab646018e2c31b311239034"}], "barrierFree": false, "heatingCost": 65.0, "kitchenette": true, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": "Sat Oct 15 14:40:02 CEST 2016", "guestToilette": false, "serviceCharge": 95.0, "historicBuilding": false, "shortDescription": "Die schön geschnittene Wohnung verfügt über ein Duschbad und ist mit einer Einbauküche ausgestattet.", "basementAvailable": false, "buildingCondition": null, "energyCertificate": {"creationDate": "APRIL_2014", "usageCertificate": null, "demandCertificate": {"endEnergyConsumption": "323", "energyEfficiencyClass": null}, "yearOfConstruction": 1951, "energyCertificateType": "DEMAND_IDENTIFICATION", "primaryEnergyProvider": "LONG_DISTANCE"}, "lastRefurbishment": null, "objectDescription": "Die Wohnung ist in den Wohnräumen mit Dielenboden ausgestattet.", "objectLocationText": "Das Objekt liegt an der Ecke Wandsbeker Zollstraße / Kattunbleiche gegenüber der Wandsbeker Sporthalle. Der Bahnhof Wandsbek sowie zahlreiche Einkaufsmöglichkeiten sind fußläufig erreichbar. Das Objekt liegt zurückversetzt und liegt nicht direkt an der Straße.", "heatingCostIncluded": false, "objectMiscellaneousText": "Die Kosten für Wasser- Stromversorgung sind nicht in den Nebenkostenvorauszahlungen enthalten."}',
	1085353,
	NOW(),
	NOW(),
	1070514,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084253,
	1070513,
	'{"name": "Schöne 3-Zimmer-Wohnung mit Blick ins Grüne - nur mit Wohnberechtigungsschein", "size": 80.99, "floor": 0, "rooms": 3.0, "garden": false, "ground": null, "heater": "GAS", "address": {"city": "Sylt", "region": "Schleswig-Holstein", "street": "Lüng Wai", "country": "DE", "zipCode": "25996", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "8"}, "balcony": false, "contact": {"name": "Putzler", "email": "hausbetreuung-putzler@t-online.de", "phone": null, "mobile": "0171-6253514", "address": {"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}, "firstName": null}, "bailment": 1800.0, "elevator": false, "flatType": "FLOOR", "basePrice": 600.13, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-861a2c00a351495bac951908ac3582a9.jpg", "type": "IMG", "title": "Scannen0004.jpg", "encrypted": false, "extension": "jpeg", "identifier": "861a2c00a351495bac951908ac3582a9"}], "barrierFree": false, "heatingCost": 180.0, "kitchenette": false, "referenceId": null, "showAddress": true, "showContact": true, "basementSize": null, "parkingSpace": false, "availableFrom": "Thu Sep 01 10:16:57 CEST 2016", "guestToilette": false, "serviceCharge": 110.0, "historicBuilding": false, "shortDescription": "Die hier zu vermietende drei Zimmer Wohnung liegt in einer großzügigen Anlage bestehend aus 6 Häusern welche 2001 im Friesenstil erbaut wurden. \n\nDie Häuser liegen eingebettet zwischen Friesenwällen und großzügigen Gartenanlagen. Pro Hauseingang wohnen 4 Familien.", "basementAvailable": false, "buildingCondition": null, "energyCertificate": {"creationDate": "APRIL_2014", "usageCertificate": {"energyConsumption": null, "energyEfficiencyClass": null, "inludesHeatConsumption": false, "energyConsumptionParameter": "78"}, "demandCertificate": null, "yearOfConstruction": 2001, "energyCertificateType": "USAGE_IDENTIFICATION", "primaryEnergyProvider": "GAS"}, "lastRefurbishment": null, "objectDescription": "Die Wohnungen wurden 2001 aufwendig und hochwertig erbaut. Das Vollbad ist weiß gefliest. Die Küche ist mit einer Einbauküche ausgestattet. \n\nZu jeder Wohnung gehört ein Dachboden.", "objectLocationText": "Die Wohnanlage liegt an einer Privatstraße, zentral aber ruhig belegen. Der Golf Club Sylt grenzt direkt an den rückwärtigen Grundstücksteil. Der Ortskern von Wenningstedt ist ca. 1 km entfernt.", "heatingCostIncluded": false, "objectMiscellaneousText": "Im Mietvertrag wird eine Staffelmiete vereinbart. Für die Anmietung ist ein Wohnberechtigungsschein notwendig."}',
	1085353,
	NOW(),
	NOW(),
	1070514,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084251,
	1070513,
	'{"name": "Gemütliche 1,5 Zi-DG-Wohnung mit Vollbad & Balkon in Eilbek", "size": 50.0, "floor": 3, "rooms": 1.5, "garden": false, "ground": "PARQUET", "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Ritterstraße", "country": "DE", "zipCode": "22089", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "76"}, "balcony": true, "contact": null, "bailment": 1620.0, "elevator": false, "flatType": "ROOF_STOREY", "basePrice": 540.0, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-a4b7edf5eff642b5a3c798aceb91cb7d.jpg", "type": "IMG", "title": "IMG_5747.jpg", "encrypted": false, "extension": "jpeg", "identifier": "a4b7edf5eff642b5a3c798aceb91cb7d"}], "barrierFree": false, "heatingCost": null, "kitchenette": true, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": "Thu Sep 15 09:21:06 CEST 2016", "guestToilette": false, "serviceCharge": 150.0, "historicBuilding": false, "shortDescription": "Das gepflegte Rotklinker-Haus wurde 1957 erbaut und besteht aus 12 Wohneinheiten.", "basementAvailable": false, "buildingCondition": null, "energyCertificate": {"creationDate": "APRIL_2014", "usageCertificate": {"energyConsumption": null, "energyEfficiencyClass": null, "inludesHeatConsumption": false, "energyConsumptionParameter": "105"}, "demandCertificate": null, "yearOfConstruction": 1957, "energyCertificateType": "USAGE_IDENTIFICATION", "primaryEnergyProvider": "LONG_DISTANCE"}, "lastRefurbishment": null, "objectDescription": "Der Boden in Flur, Wohn- und Schlafzimmer ist mit Stäbchenparkett versehen. Der Wohnraum ist mit einer offenen Küche ausgestattet und verfügt über hohe Decken. Im Schlafzimmer sowie im Flur sind jeweils schmale Einbauschränke vorhanden. Von der Wohnung hat man direkten Zugang zum eigenem Dachboden mit viel Stauraum.", "objectLocationText": "Das Objekt liegt im Stadtteil Hamm-Nord an der Grenze zum Stadtteil Eilbek. Die nächste Bahnstationen sind die S1/S11 Landwehr und U1 Ritterstraße diese ist fußläufig in wenigen Minuten erreichbar die Fahrzeit zum Hauptbahnhof beträgt hier 5-10 Minuten. Alle Einkaufsmöglichkeiten für den täglichen Bedarf sind in unmittelbarer Nähe vorhanden.", "heatingCostIncluded": true, "objectMiscellaneousText": "Die Kosten für die Wasser- und Stromversorgung sind nicht in den Betriebskosten enthalten und müssen somit vom Mieter direkt an die jeweiligen Versorgungsunternehmen gezahlt werden.\n\nIm Keller ist eine Gemeinschaftswaschmaschine vorhanden. Sollte die Nutzung der Waschmaschine gewünscht sein, sind monatlich pauschal € 10,00 an den Vermieter zu entrichten. Im Mietvertrag wird eine Indexmiete vereinbart.\n\nDie Kosten für das Kabelfernsehen sind in der Miete enthalten."}',
	1085353,
	NOW(),
	NOW(),
	1070514,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084257,
	1070513,
	'{"name": "gemütliche 2,5 Zi-Wohnung in Neugraben", "size": 45.0, "floor": 2, "rooms": 2.5, "garden": false, "ground": null, "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Scheideholzstieg", "country": "DE", "zipCode": "21149", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "9"}, "balcony": false, "contact": null, "bailment": 934.0, "elevator": false, "flatType": "ROOF_STOREY", "basePrice": 311.36, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-33fd24e1bc414fb286df3c2a8d6c7cf1.jpg", "type": "IMG", "title": "100830 001.jpg", "encrypted": false, "extension": "jpeg", "identifier": "33fd24e1bc414fb286df3c2a8d6c7cf1"}], "barrierFree": false, "heatingCost": null, "kitchenette": true, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": null, "guestToilette": false, "serviceCharge": 160.0, "historicBuilding": false, "shortDescription": "Die angebotene Wohnung befindet sich im II. Obergeschoss eines ruhig belegenen Mehrfamilienhauses. Die Anmietung eines Stellplatzes ist optional.", "basementAvailable": true, "buildingCondition": null, "energyCertificate": {"creationDate": "APRIL_2014", "usageCertificate": {"energyConsumption": null, "energyEfficiencyClass": null, "inludesHeatConsumption": true, "energyConsumptionParameter": "169"}, "demandCertificate": null, "yearOfConstruction": 1965, "energyCertificateType": "USAGE_IDENTIFICATION", "primaryEnergyProvider": "GAS"}, "lastRefurbishment": null, "objectDescription": "Die Wohnung 2,5 Zimmer, ein Vollbad sowie eine kleine Küche. Zu der Wohnung gehört ein geräumiger Abstellraum im Keller.", "objectLocationText": "Neugraben-Fischbek bietet neben diversen fußläufig erreichbaren Einkaufsmöglichkeiten auch eine gute Anbindung an den Hamburger Verkehrsverbund (S-Bahn-Station Neugraben).", "heatingCostIncluded": true, "objectMiscellaneousText": "Die Heizkosten sind in den Nebenkostenvorauszahlungen enthalten."}',
	1085353,
	NOW(),
	NOW(),
	1070514,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084234,
	1070513,
	'{"name": "Schöne 3 Zi-Wohnung mitten in Eppendorf", "size": 60.9, "floor": 4, "rooms": 3.0, "garden": false, "ground": "LAMINATE", "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Schrammsweg", "country": "DE", "zipCode": "20249", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "25"}, "balcony": false, "contact": {"name": "Schulze", "email": "info@gcv-gmbh.de", "phone": null, "mobile": null, "address": {"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}, "firstName": "Kai"}, "bailment": 2394.0, "elevator": false, "flatType": "FLOOR", "basePrice": 798.0, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-2a1f9e9c84d14e0e85ee711453cf785d.jpg", "type": "IMG", "title": "0905081.jpg", "encrypted": false, "extension": "jpeg", "identifier": "2a1f9e9c84d14e0e85ee711453cf785d"}], "barrierFree": false, "heatingCost": 55.0, "kitchenette": true, "referenceId": null, "showAddress": false, "showContact": true, "basementSize": null, "parkingSpace": false, "availableFrom": null, "guestToilette": false, "serviceCharge": 84.0, "historicBuilding": false, "shortDescription": "Die Wohnung befindet sich in einem gepflegten Mehrfamilienhaus einer ruhigen beliebten Seitenstraße Eppendorfs.", "basementAvailable": false, "buildingCondition": null, "energyCertificate": {"creationDate": null, "usageCertificate": null, "demandCertificate": null, "yearOfConstruction": 1900, "energyCertificateType": "NO_AVAILABLE", "primaryEnergyProvider": "LONG_DISTANCE"}, "lastRefurbishment": null, "objectDescription": null, "objectLocationText": "Das Objekt liegt in Eppendorf in der nähe der Bahnstation Kellinghusenstraße (U1&U3). Durch die Nähe zur Eppendorfer Landstraße und dem Eppendorfer Marktplatz, sind viele Einkaufmöglichkeiten sowie Cafés und Ärzte schnell fußläufig erreichbar.", "heatingCostIncluded": false, "objectMiscellaneousText": "Die Vorauszahlungen für die Wasser- und Stromversorgung sind direkt an die entsprechenden Unternehmen abzurechnen. Bei Vertragsabschluss ist eine Gebühr in Höhe von € 150,00 zzgl. MwSt. zu zahlen."}',
	1085353,
	NOW(),
	NOW(),
	1072767,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084259,
	1070513,
	'{"name": "Helle 2-Zimmerwohnung mit schönem Holzdielen", "size": 45.87, "floor": 1, "rooms": 2.0, "garden": false, "ground": "OTHER", "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Kraepelinweg", "country": "DE", "zipCode": "22081", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "42"}, "balcony": true, "contact": null, "bailment": 1209.0, "elevator": false, "flatType": "FLOOR", "basePrice": 403.2, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-c6239fc0906b4567936790ac7780f624.jpg", "type": "IMG", "title": "Ansicht Haus 40.JPG", "encrypted": false, "extension": "jpeg", "identifier": "c6239fc0906b4567936790ac7780f624"}], "barrierFree": false, "heatingCost": null, "kitchenette": true, "referenceId": null, "showAddress": true, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": "Tue Nov 01 15:13:53 CET 2016", "guestToilette": false, "serviceCharge": 140.0, "historicBuilding": false, "shortDescription": "Die Wohnung verfügt über einen schönen Dielenboden in den Wohnräumen. Ein hell gefliestes Duschbad mit Fenster ist vorhanden.\nDie große Küche verfügt über eine Einbauküchenzeile sowie Platz für einen Essbereich.\nEs sind zwei etwa gleich große Zimmer vorhanden, von denen das Zimmer ohne Balkon ein Durchgangszimmer ist.\nDer Balkon mit Südausrichtung bietet genug Platz um gemütlich den Feierabend ausklingen zu lassen.", "basementAvailable": true, "buildingCondition": null, "energyCertificate": {"creationDate": "APRIL_2014", "usageCertificate": {"energyConsumption": "136", "energyEfficiencyClass": null, "inludesHeatConsumption": true, "energyConsumptionParameter": "136"}, "demandCertificate": null, "yearOfConstruction": 1939, "energyCertificateType": "USAGE_IDENTIFICATION", "primaryEnergyProvider": "LONG_DISTANCE"}, "lastRefurbishment": null, "objectDescription": null, "objectLocationText": "Das Mehrfamilienhaus liegt in einer ruhigen Einbahnstraße im Stadtteil Barmbek-Süd. Es besteht eine gute Anbindung an den öffentlichen Nahverkehr (S-Bahn Friedrichsberg/U Dehnhaide sowie Buslinien). Der Hauptbahnhof ist mit den öffentlichen Verkehrsmitteln in 10 Minuten erreichbar.\nEinkaufsmöglichkeiten sind fußläufig zu erreichen.", "heatingCostIncluded": true, "objectMiscellaneousText": "Die Kosten für das Kabelfernsehen sind in den Nebenkosten enthalten.\nWasser und Strom werden durch den Mieter direkt mit den Anbietern abgerechnet.\nIm Mietvertrag wird eine Indexmiete vereinbart."}',
	1085353,
	NOW(),
	NOW(),
	1070514,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084261,
	1070513,
	'{"name": "Schöne 2 Zi-Whg. mit EBK, Vollbad, Balkon und Dielen in Winterhude", "size": 51.73, "floor": 3, "rooms": 2.0, "garden": false, "ground": null, "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Dorotheenstr.", "country": "DE", "zipCode": "22299", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "133"}, "balcony": true, "contact": null, "bailment": 1862.0, "elevator": false, "flatType": "FLOOR", "basePrice": 620.76, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-8b3ea405d3434ddc8ed5a6b4778a8a8c.jpg", "type": "IMG", "title": "Hausansicht.jpg", "encrypted": false, "extension": "jpeg", "identifier": "8b3ea405d3434ddc8ed5a6b4778a8a8c"}], "barrierFree": false, "heatingCost": null, "kitchenette": true, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": "Thu Dec 01 10:20:16 CET 2016", "guestToilette": false, "serviceCharge": 145.0, "historicBuilding": false, "shortDescription": "Die Wohnung liegt im 3. OG des Hauses und ist über den Laubengang erreichbar. Die Wohnung verfügt über einen schönen Dielenboden im Flur, Wohn- und Schlafzimmer. Das Vollbad mit Fenster ist weiß gefliest. Die Küche ist mit einer weißen Küchenzeile ausgestattet und verfügt ebenfalls über ein Fenster.", "basementAvailable": true, "buildingCondition": null, "energyCertificate": {"creationDate": "APRIL_2014", "usageCertificate": {"energyConsumption": null, "energyEfficiencyClass": null, "inludesHeatConsumption": false, "energyConsumptionParameter": "163"}, "demandCertificate": null, "yearOfConstruction": 1951, "energyCertificateType": "USAGE_IDENTIFICATION", "primaryEnergyProvider": "GAS"}, "lastRefurbishment": null, "objectDescription": null, "objectLocationText": "Das Mehrfamilienhaus liegt im schönen Stadtteil Winterhude in der Dorotheenstraße. Die U-Bahnstation Sierichstraße ist fußläufig in 5 Minuten erreichbar. Die Bushaltestelle (Buslienien 25 und 109) liegt direkt vor der Haustür. Durch die Nähe zum Winterhuder Marktplatz und den Mühlenkamp sind alle Einkaufsmöglichkeiten, Cafés, Restaurants, Bäcker und Ärzte fußläufig erreichbar.", "heatingCostIncluded": true, "objectMiscellaneousText": "Die Vorauszahlungen für die Wasser- und Stromversorgung sind nicht in der Miete enthalten und müssen somit direkt abgerechnet werden.  Im Mietvertrag wird eine Indexmiete vereinbart. Bei Vertragsabschluss ist eine Ausfertigungsgebühr in Höhe von € 150,00 zzgl. MwSt. zu zahlen."}',
	1085353,
	NOW(),
	NOW(),
	1070514,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084267,
	1070513,
	'{"name": "modernisierte 3 Zi-Whg. mit Balkon in ruhiger Lage", "size": 73.06, "floor": 1, "rooms": 3.0, "garden": false, "ground": "LAMINATE", "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Rudolf-Klug-Weg", "country": "DE", "zipCode": "22455", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "13"}, "balcony": true, "contact": null, "bailment": 2070.0, "elevator": false, "flatType": "FLOOR", "basePrice": 690.0, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-171dda7e11ee45df8856e1fc448ec15d.jpg", "type": "IMG", "title": "IMG_0972.JPG", "encrypted": false, "extension": "jpeg", "identifier": "171dda7e11ee45df8856e1fc448ec15d"}], "barrierFree": false, "heatingCost": null, "kitchenette": true, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": "Tue Nov 01 11:56:13 CET 2016", "guestToilette": false, "serviceCharge": 180.0, "historicBuilding": false, "shortDescription": "Die Wohnung wird derzeit modernisiert und ist zum 01.11.2016 fertig. Das Bad und Küche wurden komplett erneuert. In den Wohnräumen wird Laminatboden verlegt.", "basementAvailable": true, "buildingCondition": null, "energyCertificate": {"creationDate": "APRIL_2014", "usageCertificate": {"energyConsumption": null, "energyEfficiencyClass": null, "inludesHeatConsumption": true, "energyConsumptionParameter": "108"}, "demandCertificate": null, "yearOfConstruction": 1986, "energyCertificateType": "USAGE_IDENTIFICATION", "primaryEnergyProvider": "GAS"}, "lastRefurbishment": null, "objectDescription": null, "objectLocationText": "Der Rudolf-Klug-Weg ist eine Seitenstraße in der Nähe der U-Bahn Niendorf Nord. Einkaufmöglichkeiten, Schulen, Kindergärten und Ärzte sind fußläufig erreichbar.", "heatingCostIncluded": true, "objectMiscellaneousText": "Die Vorauszahlungen für das Kabelfernsehen sind in den Nebenkosten enthalten. Die Vorauszahlungen für den Wasser- und Stromverbrauch müssen direkt abgerechnet werden."}',
	1085353,
	NOW(),
	NOW(),
	1080123,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084265,
	1070513,
	'{"name": "Schöne 2 Zi-Single-Wohnung in der Nähe vom UKE", "size": 38.9, "floor": 0, "rooms": 2.0, "garden": false, "ground": "OTHER", "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Itzehoer Weg", "country": "DE", "zipCode": "20251", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "4"}, "balcony": false, "contact": {"name": null, "email": null, "phone": null, "mobile": null, "address": {"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}, "firstName": null}, "bailment": 1752.0, "elevator": false, "flatType": "GROUND_FLOOR", "basePrice": 584.0, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-494dbdb05a3c452e8ab1aebacc50595d.jpg", "type": "IMG", "title": "IMG_4684.JPG", "encrypted": false, "extension": "jpeg", "identifier": "494dbdb05a3c452e8ab1aebacc50595d"}], "barrierFree": false, "heatingCost": null, "kitchenette": true, "referenceId": null, "showAddress": false, "showContact": true, "basementSize": null, "parkingSpace": false, "availableFrom": "Tue Nov 01 16:02:11 CET 2016", "guestToilette": false, "serviceCharge": 100.0, "historicBuilding": false, "shortDescription": "Die Wohnung befindet sich im Erdgeschoss des schönen Mehrfamilienhauses. Die Küche bietet Platz für einen kleinen Esstisch. Die Räume sind größtenteils mit Dielenboden versehen. Über das Wohnzimmer hat man Zugang zum Schlafzimmer mit kleinem Austritt zum hinteren Bereich des Hauses.", "basementAvailable": false, "buildingCondition": null, "energyCertificate": {"creationDate": "MAY_2014", "usageCertificate": null, "demandCertificate": {"endEnergyConsumption": "86", "energyEfficiencyClass": "C"}, "yearOfConstruction": 1900, "energyCertificateType": "DEMAND_IDENTIFICATION", "primaryEnergyProvider": "LONG_DISTANCE"}, "lastRefurbishment": null, "objectDescription": "Die Wohnung wurde im Jahr 2013 komplett modernisiert. Zu der Wohnung gehört ein Abstellraum auf dem Dachboden des Hauses.", "objectLocationText": "Der Itzehoer Weg ist eine ruhige Seitenstraße (Einbahnstraße). Es besteht eine gute Anbindung an den öffentlichen Nahverkehr die Bushaltestelle Gärtnerstraße ist in ca. 5 Minuten fußläufig erreichbar.", "heatingCostIncluded": true, "objectMiscellaneousText": "Die Vorauszahlungen für die Wasser- und Stromversorgung müssen direkt abgerechnet werden. Im Mietvertrag wird eine Indexmiete vereinbart."}',
	1085353,
	NOW(),
	NOW(),
	1080123,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084263,
	1070513,
	'{"name": "Schöne Altbauwohnung in Hoheluft", "size": 62.3, "floor": 1, "rooms": 3.0, "garden": false, "ground": "OTHER", "heater": "GAS", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Itzehoer Weg", "country": "DE", "zipCode": "20251", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "4"}, "balcony": true, "contact": {"name": null, "email": null, "phone": null, "mobile": null, "address": {"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}, "firstName": null}, "bailment": 2803.0, "elevator": false, "flatType": "FLOOR", "basePrice": 934.5, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-cf34aa484c944fb49773eade9a89bfbe.jpg", "type": "IMG", "title": "Hausansicht.JPG", "encrypted": false, "extension": "jpeg", "identifier": "cf34aa484c944fb49773eade9a89bfbe"}], "barrierFree": false, "heatingCost": 60.0, "kitchenette": true, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": "Fri Dec 01 12:52:12 CET 2017", "guestToilette": false, "serviceCharge": 80.0, "historicBuilding": false, "shortDescription": "Die Wohnung liegt in einem Objekt einer ruhigen Seitenstraße im Stadtteil Hamburg-Hoheluft. Das rückwärtige Fassade des Gebäudes ist mit einer Wärmedämmung versehen. Die Kunststofffenster sind vor ca. 6 Jahren installiert worden.", "basementAvailable": false, "buildingCondition": null, "energyCertificate": {"creationDate": "APRIL_2014", "usageCertificate": {"energyConsumption": null, "energyEfficiencyClass": null, "inludesHeatConsumption": false, "energyConsumptionParameter": "86"}, "demandCertificate": null, "yearOfConstruction": 1900, "energyCertificateType": "USAGE_IDENTIFICATION", "primaryEnergyProvider": "LONG_DISTANCE"}, "lastRefurbishment": null, "objectDescription": "Die Wohnung verfügt über eine geräumige Einbauküche, 3 Zimmer, davon sind zwei Zimmer durch eine Flügeltür miteinander verbunden. Alle drei Zimmer sind vom Flur aus zu begehen. \nDie Wohnung wurde vor ca. 3 Jahren grundlegend saniert.", "objectLocationText": "Das Objekt liegt im schönen Stadtteil Hoheluft. Alle Einkaufsmöglichkeiten sind in unmittelbarer Nähe, auf der Hoheluftchaussee vorhanden. Es bestehen gute Anbindungen an den öffentlichen Nahverkehr. Die nächst gelegene Bushaltestelle ist Gärtnerstraße mit Anschluss an die Metrobusse 5, 20, 25.", "heatingCostIncluded": false, "objectMiscellaneousText": "Es soll eine Mindestvertragslaufzeit von 2 Jahren sowie eine Indexmieterhöhung vereinbart werden."}',
	1085353,
	NOW(),
	NOW(),
	1080123,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084271,
	1070513,
	'{"name": "Schöne 2 Zi-Wohnung im Schanzenviertel", "size": 53.53, "floor": 0, "rooms": 2.0, "garden": false, "ground": "LAMINATE", "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Amandastraße", "country": "DE", "zipCode": "20357", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "66 a"}, "balcony": false, "contact": null, "bailment": 1873.0, "elevator": false, "flatType": "RAISED_GROUND_FLOOR", "basePrice": 624.34, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-99f41cee45fb48da9f5dc1fd6ae30dee.jpg", "type": "IMG", "title": "Haus 66a.JPG", "encrypted": false, "extension": "jpeg", "identifier": "99f41cee45fb48da9f5dc1fd6ae30dee"}], "barrierFree": false, "heatingCost": 50.0, "kitchenette": false, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": "Tue Nov 01 15:07:54 CET 2016", "guestToilette": false, "serviceCharge": 63.0, "historicBuilding": false, "shortDescription": "Es handelt sich hierbei um ein gepflegtes Mehrfamilienhaus mit Rotklinkerfassade im Hinterhof gelegen.", "basementAvailable": true, "buildingCondition": null, "energyCertificate": {"creationDate": "APRIL_2014", "usageCertificate": null, "demandCertificate": {"endEnergyConsumption": "159.5", "energyEfficiencyClass": null}, "yearOfConstruction": 1956, "energyCertificateType": "DEMAND_IDENTIFICATION", "primaryEnergyProvider": "LONG_DISTANCE"}, "lastRefurbishment": null, "objectDescription": "Die Wohnung verfügt über ein Vollbad, eine kl. Abstellkammer in der Küche und einen Abstellraum im Keller des Hauses.", "objectLocationText": "Das Objekt befindet sich im beliebten Stadtteil Eimsbüttel.\nDas Objekt ist durch Bus und Bahn hervorragend an die Hamburger Innenstadt angebunden.\nDiverse Einkaufsmöglichkeiten, Cafés, Ärzte etc. befinden sich in der direkten Umgebung.", "heatingCostIncluded": false, "objectMiscellaneousText": "Die Kosten für die Wasser- und Stromversorgung sind nicht in den Nebenkosten enthalten. Im Mietvertrag wird eine Indexmiete vereinbart.\n\nDie Wohnung ist nicht für eine Wohngemeinschaft geeignet."}',
	1085353,
	NOW(),
	NOW(),
	1080123,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084275,
	1070513,
	'{"name": "Schöne 3 Zi-Wohnung mitten in Eppendorf", "size": 82.63, "floor": 2, "rooms": 3.0, "garden": false, "ground": "PARQUET", "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Schrammsweg", "country": "DE", "zipCode": "20249", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "27"}, "balcony": true, "contact": null, "bailment": 3267.0, "elevator": false, "flatType": "FLOOR", "basePrice": 1089.0, "bathRooms": 0, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-2cc8317c01b4443e861b0522a19fa077.jpg", "type": "IMG", "title": "090508.jpg", "encrypted": false, "extension": "jpeg", "identifier": "2cc8317c01b4443e861b0522a19fa077"}], "barrierFree": false, "heatingCost": null, "kitchenette": true, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": "Thu Dec 01 14:33:37 CET 2016", "guestToilette": true, "serviceCharge": 213.0, "historicBuilding": false, "shortDescription": "Die Wohnung verfügt über ein neues Vollbad, ein Gäste-WC sowie eine moderne Einbauküche.  Die Wohnräume sind mit Parkettboden ausgestattet.", "basementAvailable": false, "buildingCondition": null, "energyCertificate": {"creationDate": null, "usageCertificate": null, "demandCertificate": null, "yearOfConstruction": 1968, "energyCertificateType": "NO_AVAILABLE", "primaryEnergyProvider": "LONG_DISTANCE"}, "lastRefurbishment": null, "objectDescription": "Die Wohnung wurde im Jahr 2010 komplett überarbeitet.", "objectLocationText": "Das Mehrfamilienhaus liegt in Eppendorf in einer ruhigen Seitenstraße in der Nähe der Bahnstation Kellinghusenstraße (U1 & U3). Durch die Nähe zur Eppendorfer Landstraße und dem Eppendorfer Marktplatz, sind viele Einkaufmöglichkeiten sowie Cafés und Ärzte schnell fußläufig erreichbar.", "heatingCostIncluded": true, "objectMiscellaneousText": "Die Vorauszahlungen für die Wasser- und Stromversorgung sind direkt an die entsprechenden Unternehmen abzurechnen. Bei Vertragsabschluss ist eine Gebühr in Höhe von € 150,00 zzgl. MwSt. zu zahlen. Im Mietvertrag wird eine Indexmiete vereinbart. Die Vorauszahlungen für die Nutzung der SAT-Anlage sind in den Nebenkosten enthalten."}',
	1085353,
	NOW(),
	NOW(),
	1080123,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084273,
	1070513,
	'{"name": "Schöne 2-Zi-Wohnung in Eimsbüttel", "size": 42.6, "floor": 1, "rooms": 2.0, "garden": false, "ground": "OTHER", "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Marthastraße", "country": "DE", "zipCode": "20259", "additional": "", "coordinates": {"latitude": 0.0, "longitude": 0.0}, "houseNumber": "35c"}, "balcony": false, "contact": null, "bailment": 1980.0, "elevator": false, "flatType": "FLOOR", "basePrice": 660.3, "bathRooms": 0, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-16d15cd95d6844b7b96d98bade37a2b5.jpg", "type": "IMG", "title": "IMG_4391.JPG", "encrypted": false, "extension": "jpeg", "identifier": "16d15cd95d6844b7b96d98bade37a2b5"}], "barrierFree": false, "heatingCost": 50.0, "kitchenette": true, "referenceId": null, "showAddress": true, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": "Thu Dec 01 14:40:21 CET 2016", "guestToilette": false, "serviceCharge": 70.0, "historicBuilding": false, "shortDescription": "Die Wohnung wird gerade grundlegend saniert und soll zum 01.12.2016 fertig gestellt werden.\nEs wird ein schöner Designbodenbelag in Holz-Optik verlegt. Die Wände und die Decken werden frisch weiß angestrichen und eine neue Einbauküche eingebaut.\nDas Objekt verfügt über eine zentrale Abluftanlage.\nDas Duschbad wurde ebenfalls bereits neu gemacht.", "basementAvailable": false, "buildingCondition": null, "energyCertificate": {"creationDate": "MAY_2014", "usageCertificate": null, "demandCertificate": {"endEnergyConsumption": "155.9", "energyEfficiencyClass": "E"}, "yearOfConstruction": 1900, "energyCertificateType": "DEMAND_IDENTIFICATION", "primaryEnergyProvider": "GAS"}, "lastRefurbishment": null, "objectDescription": "Die Wohnung verfügt über ein neues Duschbad und ein separates  WC.", "objectLocationText": "Tolle Lage mitten in Eimsbüttel zwischen Belliancestraße und Eppendorfer Weg. Alle Einkaufsmöglichkeiten für den täglichen Bedarf sowie Cafés sind fußläufig erreichbar. Die nächsten U-Bahnstationen sind Emilienstraße und Christuskirche.", "heatingCostIncluded": false, "objectMiscellaneousText": "Die Kosten für den Wasser- und Stromverbrauch sind nicht in den Nebenkosten enthalten. Im Mietvertrag wird eine Indexmiete sowie eine Mindestlaufzeit von 2 Jahren vereinbart."}',
	1085353,
	NOW(),
	NOW(),
	1080123,
	'UNPROTECTED',
	null,
	0,
	'ACTIVATE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1085059,
	1070513,
	'{"name": "4 Zimmer in Berlin", "size": 50.0, "floor": null, "rooms": 4.0, "garden": false, "ground": null, "heater": "GAS", "address": {"city": "Berlin", "region": "Berlin", "street": "Chausseestr", "country": "DE", "zipCode": "10115", "additional": null, "coordinates": null, "houseNumber": "100"}, "balcony": true, "contact": null, "bailment": 2000.0, "elevator": false, "flatType": "TERRACED_FLAT", "basePrice": 750.0, "bathRooms": null, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-e25ea316bbcf4f7689c2c145c95bd07f.jpg", "type": "IMG", "title": null, "encrypted": false, "extension": "jpg", "identifier": "e25ea316bbcf4f7689c2c145c95bd07f"}], "barrierFree": false, "heatingCost": 100.0, "kitchenette": false, "referenceId": null, "showAddress": true, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": "2018-07-03", "guestToilette": false, "serviceCharge": 100.0, "historicBuilding": false, "shortDescription": null, "basementAvailable": null, "buildingCondition": null, "energyCertificate": {"creationDate": null, "usageCertificate": null, "demandCertificate": null, "yearOfConstruction": null, "energyCertificateType": "NO_AVAILABLE", "primaryEnergyProvider": "GAS"}, "lastRefurbishment": null, "objectDescription": "Super Wohnung", "objectLocationText": "1A Lage", "heatingCostIncluded": false, "objectMiscellaneousText": null}',
	1090922,
	NOW(),
	NOW(),
	1070514,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1090925,
	1070513,
	'{"name": "2 Zimmer in Hamburg", "size": 60.0, "floor": null, "rooms": 2.0, "garden": false, "ground": null, "heater": null, "address": {"city": "Hamburg", "region": "Hamburg", "street": "Hofweg", "country": "DE", "zipCode": "22085", "additional": null, "coordinates": null, "houseNumber": "9"}, "balcony": false, "contact": null, "bailment": null, "elevator": false, "flatType": null, "basePrice": 600.0, "bathRooms": null, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-66b4ccdd21ae4ac1999f80263a70e4b6.jpg", "type": "IMG", "title": null, "encrypted": false, "extension": "jpg", "identifier": "66b4ccdd21ae4ac1999f80263a70e4b6"}], "barrierFree": false, "heatingCost": null, "kitchenette": false, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": null, "guestToilette": false, "serviceCharge": null, "historicBuilding": false, "shortDescription": null, "basementAvailable": null, "buildingCondition": null, "energyCertificate": null, "lastRefurbishment": null, "objectDescription": null, "objectLocationText": null, "heatingCostIncluded": false, "objectMiscellaneousText": null}',
	1090924,
	NOW(),
	NOW(),
	1070514,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

INSERT INTO landlord.property (id, customer_id, data, prioset_id, created, updated, user_id, writeprotection, validuntil, runtimeindays, property_task)
VALUES (
	1084277,
	1070513,
	'{"name": "kleine 2,5 Zi-Wohnung in netter Wohnanlage", "size": 43.5, "floor": 0, "rooms": 2.5, "garden": false, "ground": null, "heater": "CENTRAL", "address": {"city": "Hamburg", "region": "Hamburg", "street": "Schrammsweg", "country": "DE", "zipCode": "20249", "additional": null, "coordinates": null, "houseNumber": "25b"}, "balcony": true, "contact": null, "bailment": 1698.0, "elevator": false, "flatType": "GROUND_FLOOR", "basePrice": 580.0, "bathRooms": 1, "documents": [], "flatShare": false, "externalId": null, "attachments": [{"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-83166d0ec30445df8183537ff3fb1523.jpg", "type": "IMG", "title": "IMG_0656.JPG", "encrypted": false, "extension": "jpeg", "identifier": "83166d0ec30445df8183537ff3fb1523"}, {"url": "https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-16a7f03772744571b25bc0d2511f1155.jpg", "type": "IMG", "title": null, "encrypted": false, "extension": "jpg", "identifier": "16a7f03772744571b25bc0d2511f1155"}], "barrierFree": false, "heatingCost": 100.0, "kitchenette": false, "referenceId": null, "showAddress": false, "showContact": false, "basementSize": null, "parkingSpace": false, "availableFrom": "2018-07-08", "guestToilette": false, "serviceCharge": 110.0, "historicBuilding": false, "shortDescription": null, "basementAvailable": null, "buildingCondition": null, "energyCertificate": {"creationDate": "APRIL_2014", "usageCertificate": {"energyConsumption": "151", "energyEfficiencyClass": null, "inludesHeatConsumption": false, "energyConsumptionParameter": "151"}, "demandCertificate": null, "yearOfConstruction": 1900, "energyCertificateType": "USAGE_IDENTIFICATION", "primaryEnergyProvider": "LONG_DISTANCE"}, "lastRefurbishment": null, "objectDescription": "Die Wohnung wird auf Wunsch mit Spüle & Herd ausgestattet.", "objectLocationText": "Das Objekt liegt in Eppendorf in der Nähe der Bahnstation Kellinghusenstraße (U1 & U3). Durch die Nähe zur Eppendorfer Landstraße und dem Eppendorfer Marktplatz, sind alle Einkaufmöglichkeiten schnell fußläufig erreichbar.", "heatingCostIncluded": false, "objectMiscellaneousText": "Die Vorauszahlungen für die Wasser- und Stromversorgung sind direkt an die entsprechenden Unternehmen abzurechnen. Im Mietvertrag wird eine Indexmiete vereinbart. Bei Vertragsabschluss ist eine Gebühr in Höhe von € 150,00 zzgl. MwSt. zu zahlen. Die Vorauszahlungen für die Nutzung der SAT-Anlage sind bereits in den Nebenkosten enthalten."}',
	1094378,
	NOW(),
	NOW(),
	1080123,
	'UNPROTECTED',
	null,
	0,
	'IDLE'
);

-- PS-CUSTOMER

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	1062819,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	1062821,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	1062823,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	1062825,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000015,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000002,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000016,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000012,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000018,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000009,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000017,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000005,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000010,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000007,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000006,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000020,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000001,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000003,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000004,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000014,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000019,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000011,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000013,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

INSERT INTO propertysearcher.customer (id, paymentmethods, files, location, created, updated, paymentdetails)
VALUES (
	2000008,
	ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
	'[]',
	'DE',
	NOW(),
	NOW(),
	'{}'
);

-- PS-USER
INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000018,
	'alink+21@immomio.de',
	null,
	2000010,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Seibert", "phone": null, "title": null, "gender": "FEMALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-4ee23eac898d4db68ebaab30650ae867.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "4ee23eac898d4db68ebaab30650ae867"}, "firstname": "Sofia", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 1600.0, "subType": "Kellnerin", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000006,
	'alink+13@immomio.de',
	null,
	2000004,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Kooper", "phone": null, "title": null, "gender": "FEMALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-5bfc637042114287a4e406fe0b140c98.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "5bfc637042114287a4e406fe0b140c98"}, "firstname": "Henriette", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 7300.0, "subType": "Chirurgin", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000003,
	'alink+10@immomio.de',
	null,
	2000001,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Goldmann", "phone": null, "title": null, "gender": "MALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-9293ea58cd3d4d5c88ecfd69367a0d9a.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "9293ea58cd3d4d5c88ecfd69367a0d9a"}, "firstname": "Christian", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 3500.0, "subType": "Innenarchitekt", "employmentDate": null}, "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000005,
	'alink+12@immomio.de',
	null,
	2000003,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Beck", "phone": null, "title": null, "gender": "FEMALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-733e94c7226b46ada8c36630d09aa706.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "733e94c7226b46ada8c36630d09aa706"}, "firstname": "Nadine", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 2200.0, "subType": "Winzerin", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000021,
	'alunt+132@immomio.de',
	null,
	2000008,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Behrnke", "phone": null, "title": null, "gender": "MALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-baf3443a87bc424fbef21872e3ff32e2.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "baf3443a87bc424fbef21872e3ff32e2"}, "firstname": "Simon", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 7000.0, "subType": "Pilot", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": "Hamburg", "region": "Hamburg", "street": "Rosenstr.", "country": "DE", "zipCode": "20095", "additional": null, "coordinates": null, "houseNumber": 27}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000013,
	'alink+17@immomio.de',
	null,
	2000013,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Dreese", "phone": null, "title": null, "gender": "MALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-04b20536d37244f5be989e9f2b32bacf.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "04b20536d37244f5be989e9f2b32bacf"}, "firstname": "Mohammed", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 2000.0, "subType": "Koch", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000019,
	'alink+22@immomio.de',
	null,
	2000007,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Gustav", "phone": null, "title": null, "gender": "MALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-d9e2a4e3614e44f5bb76410e5bad0c6d.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "d9e2a4e3614e44f5bb76410e5bad0c6d"}, "firstname": "Noah", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 2200.0, "subType": "Erzieher", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000016,
	'alink+20@immomio.de',
	null,
	2000005,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Schappel", "phone": null, "title": null, "gender": "MALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-7fa32194ff8445b8b3ae2ba2e3357212.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "7fa32194ff8445b8b3ae2ba2e3357212"}, "firstname": "Leander", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 2700.0, "subType": "IT-Systemelektroniker", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000014,
	'alink+18@immomio.de',
	null,
	2000015,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Von Kormann", "phone": null, "title": null, "gender": "MALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-66f361b9bc99468293a1f57c3be3f241.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "66f361b9bc99468293a1f57c3be3f241"}, "firstname": "Harry", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 5100.0, "subType": "Professor", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000011,
	'alunt+135@immomio.de',
	null,
	2000018,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Janßen", "phone": null, "title": null, "gender": "FEMALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-8fe37c6946884cf78d94f79b43610ed2.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "8fe37c6946884cf78d94f79b43610ed2"}, "firstname": "Nele", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 1900.0, "subType": "Einzelhandelskauffrau", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": "Hamburg", "region": "Hamburg", "street": "Osterstraße", "country": "DE", "zipCode": "20097", "additional": null, "coordinates": null, "houseNumber": 16}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000009,
	'alink+15@immomio.de',
	null,
	2000019,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Kordes", "phone": null, "title": null, "gender": "MALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-c606b1ae77a94cc298d8c5174bb96e48.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "c606b1ae77a94cc298d8c5174bb96e48"}, "firstname": "Samuel", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 4600.0, "subType": "Unternehmensberater", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000022,
	'alink+24@immomio.de',
	null,
	2000020,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Feist", "phone": null, "title": null, "gender": "FEMALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-1f7e39fe3e724c5db9c7439e1648c58f.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "1f7e39fe3e724c5db9c7439e1648c58f"}, "firstname": "Isabel", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 2500.0, "subType": "Bankangestellte", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000004,
	'alunt+133@immomio.de',
	null,
	2000002,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Gerdes", "phone": null, "title": null, "gender": "MALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-39e928ea25fd441ea6c6669faa88414d.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "39e928ea25fd441ea6c6669faa88414d"}, "firstname": "Maurice", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 2400.0, "subType": "Steuerfachangestellter", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": "Hamburg", "region": "Hamburg", "street": "Ferdinandstraße", "country": "DE", "zipCode": "20359", "additional": null, "coordinates": null, "houseNumber": 156}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000017,
	'alunt+136@immomio.de',
	null,
	2000009,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Haue", "phone": null, "title": null, "gender": "FEMALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-54db6f275c0b46f089f500558dda9dae.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "54db6f275c0b46f089f500558dda9dae"}, "firstname": "Maria", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 3400.0, "subType": "Marketingmanagerin", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": "Hamburg", "region": "Hamburg", "street": "Raboisen", "country": "DE", "zipCode": "20095", "additional": null, "coordinates": null, "houseNumber": 119}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000020,
	'alink+23@immomio.de',
	null,
	2000006,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Weber", "phone": null, "title": null, "gender": "MALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-dae09b908f84431299c76e1a4ba9fc74.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "dae09b908f84431299c76e1a4ba9fc74"}, "firstname": "Hugo", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 2800.0, "subType": "Uhrmacher", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000012,
	'alink+16@immomio.de',
	null,
	2000011,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Meyer", "phone": null, "title": null, "gender": "FEMALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-6bee10b53179413f8c33c61cede88415.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "6bee10b53179413f8c33c61cede88415"}, "firstname": "Maja", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 3000.0, "subType": "Sonderpädagogin", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000010,
	'alunt+137@immomio.de',
	null,
	2000017,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "List", "phone": null, "title": null, "gender": "MALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-64571273e79f4a478a97f67741c266c3.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "64571273e79f4a478a97f67741c266c3"}, "firstname": "Anton", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 3100.0, "subType": "Werbetexter", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": "Berlin", "region": "Berlin", "street": "Chausseestraße", "country": "DE", "zipCode": "10115", "additional": null, "coordinates": null, "houseNumber": 76}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000007,
	'alunt+134@immomio.de',
	null,
	2000012,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Middendorff", "phone": null, "title": null, "gender": "MALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-4bd7352bb4aa41aca4686d1f358923b1.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "4bd7352bb4aa41aca4686d1f358923b1"}, "firstname": "Raphael", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 2900.0, "subType": "Künstler", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": "Hamburg", "region": "Hamburg", "street": "Brandsende", "country": "DE", "zipCode": "20567", "additional": null, "coordinates": null, "houseNumber": 4}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000008,
	'alink+14@immomio.de',
	null,
	2000014,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Husmann", "phone": null, "title": null, "gender": "FEMALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-0f61acfd81954f6a8f5768faa06f5be8.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "0f61acfd81954f6a8f5768faa06f5be8"}, "firstname": "Sandra", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 1500.0, "subType": "Tiermedizinische Fachangestellte", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}',
	'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
	2000015,
	'alink+19@immomio.de',
	null,
	2000016,
	true,
	false,
	false,
	'REGISTERED',
	NOW(),
	NOW(),
	NOW(),
	'{"law": {"allowSchufa": true, "noRentArrears": true, "noPoliceRecord": true, "noTenancyLawConflicts": true, "informationTrueAndComplete": true}, "name": "Juncke", "phone": null, "title": null, "gender": "FEMALE", "smoker": {"smoker": false, "inhouse": false}, "portrait": {"url": "https://s3.eu-central-1.amazonaws.com/immomio-ps-stg-img-store/IMG-4d0a4e0051374b54b11119241da7be61.jpg", "type": "IMG", "encrypted": false, "extension": "jpg", "identifier": "4d0a4e0051374b54b11119241da7be61"}, "firstname": "Annegret", "residents": 1, "moveInDate": null, "profession": {"type": "EMPLOYED_UNLIMITED", "income": 2600.0, "subType": "Kunsthistorikerin", "employmentDate": null}, "attachments": [], "dateOfBirth": null, "householdType": "COUPLE", "personalStatus": "MARRIED", "creditScreening": {"value": null, "available": false}, "additionalInformation": {"wbs": false, "music": false, "animals": false, "bailment": false}}',
	'{"city": null, "region": null, "street": null, "country": null, "zipCode": null, "additional": null, "coordinates": null, "houseNumber": null}',
	'APPLICANT'
);


-- PROPERTY-PROPOSALS
INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	21,
	2000004,
	1084277,
	9.2,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	22,
	2000007,
	1084277,
	9.2,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	23,
	2000011,
	1084277,
	9.3,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	24,
	2000017,
	1084277,
	9.1,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	25,
	2000010,
	1084277,
	8.9,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	15,
	2000017,
	1081310,
	9.1,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	16,
	2000018,
	1081310,
	6.4,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	17,
	2000019,
	1081310,
	8.1,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	5,
	2000007,
	1084269,
	9.2,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	6,
	2000008,
	1084269,
	7.6,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	4,
	2000006,
	1084269,
	8.7,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	3,
	2000005,
	1084269,
	6.3,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	7,
	2000009,
	1084251,
	6.8,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	8,
	2000010,
	1084251,
	8.9,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	10,
	2000012,
	1084251,
	8.3,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	2,
	2000004,
	1084251,
	9.2,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	12,
	2000014,
	1084224,
	7.5,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	9,
	2000011,
	1084224,
	9.3,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	1,
	2000003,
	1084224,
	7.1,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	11,
	2000013,
	1084224,
	5.4,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	14,
	2000016,
	1084224,
	7,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	13,
	2000015,
	1084277,
	4.2,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	18,
	2000020,
	1084277,
	8.2,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	19,
	2000021,
	1084277,
	9.9,
	'PROSPECT',
	NOW(),
	NOW()
);

INSERT INTO shared.propertyproposal (id, user_id, property_id, score, state, created, updated)
VALUES (
	20,
	2000022,
	1084277,
	1.2,
	'PROSPECT',
	NOW(),
	NOW()
);
