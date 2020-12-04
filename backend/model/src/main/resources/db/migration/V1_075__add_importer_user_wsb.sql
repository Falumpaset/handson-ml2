INSERT INTO administration."user" (id, email, password, enabled, expired, locked, lastlogin, created, updated, type)
VALUES (3, 'wsb-exporter@immomio.de', '' , true, false, false,
NOW(), NOW(), NOW(), 'SERVICE') ON CONFLICT DO NOTHING;