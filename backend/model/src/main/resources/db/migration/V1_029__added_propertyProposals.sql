DROP   TYPE IF EXISTS shared.propertyproposalstate CASCADE;
CREATE TYPE           shared.propertyproposalstate AS ENUM ('PROSPECT', 'ACCEPTED', 'DENIED', 'INVITED');

CREATE TABLE shared.propertyproposal(
  id          bigint                        NOT NULL,
  user_id     bigint                        NOT NULL,
  property_id bigint                        NOT NULL,
  score       double precision              NOT NULL DEFAULT 0,
  state       shared.propertyProposalState  NOT NULL DEFAULT 'PROSPECT',

  created     timestamp without time zone,
  updated     timestamp without time zone,

  CONSTRAINT propertyproposal_pkey PRIMARY KEY (id),
  CONSTRAINT uq_propertyproposal_01  UNIQUE (property_id, user_id),
  CONSTRAINT fk_propertyproposal_01 FOREIGN KEY (user_id)
  REFERENCES propertysearcher."user" (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_propertyproposal_02 FOREIGN KEY (property_id)
  REFERENCES landlord.property (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);