DROP   TYPE IF EXISTS propertysearcher.usertype CASCADE;
CREATE TYPE           propertysearcher.usertype AS ENUM (
  'PROSPECT', 'APPLICANT', 'TENANT');

ALTER TABLE propertysearcher.user
  ADD COLUMN type propertysearcher.usertype NOT NULL;

ALTER TABLE propertysearcher.permissionscheme_rights
  ADD COLUMN type propertysearcher.usertype NOT NULL;

CREATE UNIQUE INDEX right_id_pkey ON propertysearcher."right" (right_id);

CREATE TABLE propertysearcher.usertype_rights
(
  id          bigint                          NOT NULL,
  usertype    propertysearcher.usertype       NOT NULL,
  right_id    bigint                          NOT NULL,
  created     timestamp without time zone,
  CONSTRAINT usertype_rights_pkey PRIMARY KEY (id),
  CONSTRAINT fk_usertype_rights_01 FOREIGN KEY (right_id)
  REFERENCES propertysearcher."right" (right_id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

ALTER TABLE propertysearcher.usertype_rights
  ADD CONSTRAINT uq_usertype_rights_01 UNIQUE (usertype, right_id);
