INSERT INTO administration."user" (id, email, password, enabled, expired, locked, lastlogin, created, updated, type)
VALUES (2, 'importer@immomio.de', '$2a$10$BFdOu1/.asZ8CV0Gug6RueN.KDy.2X3iDU.bay2FImUKRLvYtw0n6', true, false, false,
 NOW(), NOW(), NOW(), 'SERVICE') ON CONFLICT DO NOTHING;
INSERT INTO administration."user" (id, email, password, enabled, expired, locked, lastlogin, created, updated, type)
VALUES (1, 'admin@immomio.de', '$2a$10$BFdOu1/.asZ8CV0Gug6RueN.KDy.2X3iDU.bay2FImUKRLvYtw0n6', true, false, false,
NOW(), NOW(), NOW(), 'ROOT') ON CONFLICT DO NOTHING;