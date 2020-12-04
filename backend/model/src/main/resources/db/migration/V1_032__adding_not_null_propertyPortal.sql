ALTER TABLE shared.application
DROP COLUMN portal;

ALTER TABLE shared.application
ADD portal shared.portalenum NOT NULL;