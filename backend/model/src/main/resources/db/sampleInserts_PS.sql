INSERT INTO propertysearcher.customer(id, paymentmethods, files, location, created, updated, paymentdetails, pricemultiplier)
VALUES (2000001,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'[]','DE',NOW(),NOW(),'{}',1.5);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (2000002, 'mbaumbach+321@immomio.de', null, 2000001, true,false,false,'REGISTERED', NOW(), NOW(), NOW(), '{}', '{}', 'APPLICANT');
