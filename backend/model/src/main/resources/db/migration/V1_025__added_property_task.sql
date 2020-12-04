CREATE TYPE landlord.property_task AS ENUM ('IDLE','ACTIVATE','DEACTIVATE','UPDATE','DELETE');

ALTER TABLE landlord.property
  ADD COLUMN property_task landlord.property_task NOT NULL DEFAULT 'IDLE';