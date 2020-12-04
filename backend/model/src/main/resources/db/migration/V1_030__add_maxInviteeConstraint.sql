
CREATE OR REPLACE FUNCTION appointmentHasSpace (query_id bigint)
RETURNS boolean AS $result$
BEGIN
  IF(query_id IN (SELECT id
    FROM shared.appointment a
    INNER JOIN (
       SELECT appointment_id, COUNT(*)
       FROM shared.appointment_acceptance
       GROUP BY appointment_id
    ) AS table_count
    ON a.id=table_count.appointment_id
    WHERE table_count.count < maxinviteecount)) THEN RETURN TRUE;
  END IF;
  RETURN FALSE;
END;
$result$ LANGUAGE plpgsql;

ALTER TABLE shared.appointment_acceptance
ADD CONSTRAINT appointmentHasSpace_C01 CHECK (public.appointmenthasspace(appointment_id));