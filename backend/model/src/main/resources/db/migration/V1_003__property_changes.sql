DROP   TYPE IF EXISTS landlord.propertywriteprotection CASCADE;
CREATE TYPE           landlord.propertywriteprotection AS ENUM (
  'UNPROTECTED', 'IMPORT_PROTECTED', 'IMPORT_PROTECTED_ACTIVE');

ALTER TABLE landlord."user"
  ADD CONSTRAINT uq_user_02 UNIQUE (id, customer_id);

ALTER TABLE landlord.property
  ADD COLUMN user_id bigint;

ALTER TABLE landlord.property
  ADD CONSTRAINT fk_property_03 FOREIGN KEY (user_id, customer_id)
REFERENCES landlord."user" (id, customer_id) MATCH SIMPLE
ON UPDATE CASCADE
ON DELETE CASCADE;
CREATE INDEX fki_fk_property_03
  ON landlord.property(user_id, customer_id);

ALTER TABLE landlord.property
  ADD COLUMN writeprotection landlord.propertywriteprotection NOT NULL;

ALTER TABLE landlord.property
  ADD COLUMN validuntil timestamp without time zone;

ALTER TABLE landlord.property
  ADD COLUMN runtimeindays integer;

ALTER TABLE landlord.propertyportal
  ADD COLUMN credential_id bigint;

ALTER TABLE landlord.propertyportal
  ADD CONSTRAINT fk_propertyportal_02 FOREIGN KEY (credential_id)
REFERENCES landlord.credential (id) MATCH SIMPLE
ON UPDATE CASCADE
ON DELETE CASCADE;
CREATE INDEX fki_fk_propertyportal_02
  ON landlord.propertyportal(credential_id);