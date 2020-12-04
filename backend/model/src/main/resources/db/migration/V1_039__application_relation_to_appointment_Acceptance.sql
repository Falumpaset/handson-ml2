ALTER TABLE shared.appointment_acceptance
DROP CONSTRAINT uq_appointment_acceptance_01;

ALTER TABLE shared.appointment_acceptance
DROP CONSTRAINT fk_appointment_acceptance_02;

ALTER TABLE shared.appointment_acceptance
DROP COLUMN user_id;

ALTER TABLE shared.appointment_acceptance
ADD COLUMN application_id bigint NOT NULL DEFAULT 1;

ALTER TABLE shared.appointment_acceptance
ADD CONSTRAINT uq_appointment_acceptance_01 UNIQUE (appointment_id, application_id);

ALTER TABLE shared.appointment_acceptance
ADD CONSTRAINT fk_appointment_acceptance_02 FOREIGN KEY (application_id)
REFERENCES shared.application (id) MATCH SIMPLE
ON UPDATE CASCADE
ON DELETE CASCADE;