INSERT INTO landlord."right" (id, right_id, created)
VALUES (1001, 1001, NOW()) ON CONFLICT DO NOTHING;
INSERT INTO landlord."right" (id, right_id, created)
VALUES (1101, 1101, NOW()) ON CONFLICT DO NOTHING;
INSERT INTO landlord."right" (id, right_id, created)
VALUES (1201, 1201, NOW()) ON CONFLICT DO NOTHING;
INSERT INTO landlord."right" (id, right_id, created)
VALUES (2000, 2000, NOW()) ON CONFLICT DO NOTHING;
INSERT INTO landlord."right" (id, right_id, created)
VALUES (2001, 2001, NOW()) ON CONFLICT DO NOTHING;
INSERT INTO landlord."right" (id, right_id, created)
VALUES (2002, 2002, NOW()) ON CONFLICT DO NOTHING;
INSERT INTO landlord."right" (id, right_id, created)
VALUES (2003, 2003, NOW()) ON CONFLICT DO NOTHING;
INSERT INTO landlord."right" (id, right_id, created)
VALUES (2004, 2004, NOW()) ON CONFLICT DO NOTHING;
INSERT INTO landlord."right" (id, right_id, created)
VALUES (2005, 2005, NOW()) ON CONFLICT DO NOTHING;
INSERT INTO landlord."right" (id, right_id, created)
VALUES (2006, 2006, NOW()) ON CONFLICT DO NOTHING;

INSERT INTO landlord.usertype_rights (id, usertype, right_id, created)
VALUES (1000, 'COMPANYADMIN', 2000, NOW()) ON CONFLICT DO NOTHING;
INSERT INTO landlord.usertype_rights (id, usertype, right_id, created)
VALUES (1001, 'COMPANYADMIN', 2001, NOW()) ON CONFLICT DO NOTHING;