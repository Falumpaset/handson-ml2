ALTER TYPE administration.usertype RENAME TO usertype_old;

CREATE TYPE administration.usertype AS ENUM ('DEVELOPER', 'SALES', 'ROOT', 'SERVICE');

ALTER TABLE administration.user
  ALTER COLUMN "type" TYPE administration.usertype USING "type"::text::administration.usertype;

DROP TYPE administration.usertype_old;

UPDATE administration.user SET "type" = 'ROOT';

ALTER TABLE administration.user
    ALTER "type" SET NOT NULL;