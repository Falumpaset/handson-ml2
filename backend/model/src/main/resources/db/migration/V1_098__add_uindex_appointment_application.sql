
CREATE UNIQUE INDEX IF NOT EXISTS uq_appointment_acceptance_1 ON shared.appointment_acceptance (appointment_id, application_id) WHERE (state = 'ACTIVE');

