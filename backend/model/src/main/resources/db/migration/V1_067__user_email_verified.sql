ALTER TABLE landlord."user"
    ADD COLUMN email_verified timestamp without time zone;

ALTER TABLE propertysearcher."user"
    ADD COLUMN email_verified timestamp without time zone;

update landlord."user" set email_verified = now();
update propertysearcher."user" set email_verified = now();
