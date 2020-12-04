ALTER TABLE shared.appointment
    drop COLUMN email_sent;


ALTER TABLE shared.appointment_acceptance
    ADD COLUMN email_sent boolean default false;