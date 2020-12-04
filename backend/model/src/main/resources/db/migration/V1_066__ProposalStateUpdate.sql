
--Step 1: Adding the type OFFERED to the type enum
ALTER TYPE shared.propertyproposalstate RENAME TO propertyproposalstate_old;

CREATE TYPE shared.propertyproposalstate AS ENUM ('PROSPECT', 'ACCEPTED', 'DENIEDBYLL', 'DENIEDBYPS', 'INVITED',
'OFFERED');

ALTER TABLE shared.propertyproposal
  ALTER COLUMN state DROP DEFAULT,
  ALTER COLUMN state TYPE shared.propertyproposalstate USING state::text::shared.propertyproposalstate,
  ALTER COLUMN state SET DEFAULT 'PROSPECT';

--Replace OFFERED WITH OFFERED
UPDATE shared.propertyproposal
SET state = 'OFFERED' WHERE state = 'INVITED';

DROP TYPE shared.propertyproposalstate_old;

--Step 2: Removing the old type OFFERED from the type enum
ALTER TYPE shared.propertyproposalstate RENAME TO propertyproposalstate_old;

CREATE TYPE shared.propertyproposalstate AS ENUM ('PROSPECT', 'ACCEPTED', 'DENIEDBYLL', 'DENIEDBYPS', 'OFFERED');

ALTER TABLE shared.propertyproposal
  ALTER COLUMN state DROP DEFAULT,
  ALTER COLUMN state TYPE shared.propertyproposalstate USING state::text::shared.propertyproposalstate,
  ALTER COLUMN state SET DEFAULT 'PROSPECT';

DROP TYPE shared.propertyproposalstate_old;