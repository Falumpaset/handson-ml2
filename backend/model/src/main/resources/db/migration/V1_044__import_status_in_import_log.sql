CREATE TYPE landlord.importstatus AS ENUM ('QUEUED', 'STARTED', 'PENDING', 'COMPLETED', 'ERROR');

ALTER TABLE landlord.import_log
    ALTER COLUMN "status" DROP DEFAULT,
    ALTER COLUMN "status" TYPE landlord.importstatus USING "status"::text::landlord.importstatus,
    ALTER COLUMN "status" SET DEFAULT 'QUEUED';