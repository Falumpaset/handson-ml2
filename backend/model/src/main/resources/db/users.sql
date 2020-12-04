INSERT INTO administration.user (id, email, password, enabled, expired, locked, lastlogin, created, updated, type) VALUES
(1, 'admin@immomio.de', '$2a$10$BFdOu1/.asZ8CV0Gug6RueN.KDy.2X3iDU.bay2FImUKRLvYtw0n6', true, false, false, null, NOW(), NOW(), 'ROOT');

INSERT INTO administration.user (id, email, password, enabled, expired, locked, lastlogin, created, updated, type) VALUES
(101, 'su_importer@immomio.de', '$2a$10$BFdOu1/.asZ8CV0Gug6RueN.KDy.2X3iDU.bay2FImUKRLvYtw0n6', true, false, false, null, NOW(), NOW(), 'SERVICE');