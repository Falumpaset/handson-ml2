ALTER TABLE landlord.property
ADD COLUMN applications_viewed timestamp NOT NULL DEFAULT NOW();

CREATE INDEX idx_applications_viewed ON shared.application(property_id, created);