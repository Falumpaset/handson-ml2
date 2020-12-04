ALTER TABLE shared.appointment_acceptance DROP CONSTRAINT if exists appointmentHasSpace_C01;

drop function if exists shared.appointmenthasspace(query_id bigint, state shared.appointmentacceptancestate
                                                  );

drop function if exists public.appointmenthasspace(query_id bigint, state shared.appointmentacceptancestate
);

create function shared.appointmenthasspace(query_id bigint,
                                    state shared.appointmentacceptancestate
                                   )
    returns boolean
    language plpgsql
as
$$
begin
    if (state = 'CANCELED') then
        return true;
    end if;
    if (query_id in (select id
                     from shared.appointment a
                              inner join (
                         select appointment_id, COUNT(*) as number_of_acceptances
                         from shared.appointment_acceptance t
                         where t.state = 'ACTIVE'
                         group by appointment_id
                     ) as acceptance_count
                                         on a.id = acceptance_count.appointment_id
                     where acceptance_count.number_of_acceptances > a.maxinviteecount)) then
        return false;
    end if;
    return true;
end;
$$;

alter table shared.appointment_acceptance
    add constraint appointmentHasSpace_C01 check (shared.appointmenthasspace(appointment_id, state));

ALTER FUNCTION shared.appointmenthasspace(bigint, shared.appointmentacceptancestate) SET search_path=shared;
