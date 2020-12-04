ALTER TABLE shared.appointment_acceptance
DROP CONSTRAINT appointmentHasSpace_C01;

DROP function if exists public.appointmenthasspace(query_id bigint);

ALTER TABLE shared.appointment_acceptance
DROP CONSTRAINT uq_appointment_acceptance_01;

create function appointmenthasspace(query_id bigint)
  returns boolean
language plpgsql
as $$
BEGIN
  IF(query_id IN (SELECT id
    FROM shared.appointment a
    INNER JOIN (
       SELECT appointment_id, COUNT(*)
       FROM shared.appointment_acceptance t
       WHERE t.state='ACTIVE'
       GROUP BY appointment_id
    ) AS table_count
    ON a.id=table_count.appointment_id
    WHERE table_count.count < maxinviteecount)) THEN RETURN TRUE;
  END IF;
  RETURN FALSE;
END;
$$;

ALTER TABLE shared.appointment_acceptance
ADD CONSTRAINT appointmentHasSpace_C01 CHECK (public.appointmenthasspace(appointment_id));
