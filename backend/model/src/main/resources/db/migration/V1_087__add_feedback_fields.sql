ALTER TYPE shared.applicationstate RENAME TO applicationstate_old;

create type shared.applicationstate as enum ('UNANSWERED', 'ACCEPTED', 'REJECTED', 'INTENT', 'NO_INTENT');

ALTER TABLE shared.application
    ALTER COLUMN "status" DROP DEFAULT,
    ALTER COLUMN "status" TYPE shared.applicationstate USING "status"::text::shared.applicationstate,
    ALTER COLUMN "status" SET DEFAULT 'UNANSWERED';

DROP TYPE shared.applicationstate_old;

ALTER TABLE shared.appointment
    ADD COLUMN email_sent boolean default false;