
ALTER TYPE shared.propertyproposalstate RENAME TO propertyproposalstate_old;

CREATE TYPE shared.propertyproposalstate AS ENUM ('PROSPECT', 'ACCEPTED', 'DENIEDBYLL', 'DENIEDBYPS', 'INVITED');

UPDATE shared.propertyproposal SET state = 'PROSPECT' WHERE state = 'DENIED';

ALTER TABLE shared.propertyproposal
  ALTER COLUMN state DROP DEFAULT,
  ALTER COLUMN state TYPE shared.propertyproposalstate USING state::text::shared.propertyproposalstate,
  ALTER COLUMN state SET DEFAULT 'PROSPECT';

DROP TYPE shared.propertyproposalstate_old;