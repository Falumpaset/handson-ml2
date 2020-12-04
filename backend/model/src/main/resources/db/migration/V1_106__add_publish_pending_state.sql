ALTER TYPE landlord.publishstate RENAME TO publishstate_old;

create type landlord.publishstate as enum ('SUCCESS', 'PENDING', 'ERROR');

ALTER TABLE landlord.publish_log
    ALTER COLUMN "publish_state" DROP DEFAULT,
    ALTER COLUMN "publish_state" TYPE landlord.publishstate USING "publish_state"::text::landlord.publishstate,
    ALTER COLUMN "publish_state" SET DEFAULT 'PENDING';

DROP TYPE landlord.publishstate_old;