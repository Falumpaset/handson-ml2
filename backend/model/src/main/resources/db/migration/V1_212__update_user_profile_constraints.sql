alter table propertysearcher."user" drop constraint if exists c_user_type_change_main_profile ;

create or replace function propertysearcher.user_type_change()
    returns trigger
    language plpgsql
as $$
begin

    if (old.type = 'REGISTERED' and new.type = 'UNREGISTERED') then
        raise exception 'type cannot be changed from registered to unregistered';
    end if;

    if (old.type = 'UNREGISTERED' and new.type = 'REGISTERED') then
        if ((select count(*) from propertysearcher.user_profile up where up.user_id = new.id) = 1) and
           (select up.type from propertysearcher.user_profile up where up.user_id = new.id limit 1) = 'MAIN' then
            return new;
        end if;
        raise exception 'registered only main profile allowed';

    end if;
    return new;
end; $$;

alter function propertysearcher.user_type_change() set search_path = shared;

create constraint trigger c_user_type_change_main_profile after update on propertysearcher."user" initially deferred for each row execute procedure propertysearcher.user_type_change();

alter table propertysearcher.user_profile drop constraint if exists uidx_user_registered_only_main_profile ;

create or replace function propertysearcher.user_registered_only_main_profile()
    returns trigger
    language plpgsql
as
$$
begin
    if ((select u.type from propertysearcher."user" u where u.id = new.user_id limit 1) = 'REGISTERED') then
        if (new.type = 'MAIN' and
            (select count(*) from propertysearcher.user_profile up where up.user_id = new.user_id) <> 1) then
            raise exception 'more than one profile';
        end if;

        if (new.type <> 'MAIN' and
            (select count(*) from propertysearcher.user_profile up where up.id = new.id) <> 0) then
            raise exception 'not allowed % % %', new.id, new.type, (select count(*) from propertysearcher.user_profile up where up.id = new.id) ;
        end if;
    end if;
    return new;
end;
$$;

alter function propertysearcher.user_registered_only_main_profile() set search_path = shared;

create constraint trigger c_user_registered_only_main_profile after insert or update on propertysearcher.user_profile initially deferred for each row execute procedure propertysearcher.user_registered_only_main_profile();
