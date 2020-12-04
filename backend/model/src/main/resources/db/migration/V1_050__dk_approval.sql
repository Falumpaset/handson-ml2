DROP   TYPE IF EXISTS landlord.dk_approval_level CASCADE;
CREATE TYPE landlord.dk_approval_level AS ENUM ('DK1', 'DK2', 'DK3');

CREATE TABLE landlord.dk_approval
(
  id bigint NOT NULL,
  level landlord.dk_approval_level NOT NULL DEFAULT 'DK1',
  application_id bigint NOT NULL,
  created timestamp without time zone,

  CONSTRAINT dk_approval_pkey PRIMARY KEY (id),
  CONSTRAINT fk_dk_approval_application FOREIGN KEY (application_id)
  REFERENCES shared.application (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

insert into landlord.dk_approval(id, level, created, application_id)
select nextval('dictionary_seq'), 'DK1', NOW(), id from shared.application;
