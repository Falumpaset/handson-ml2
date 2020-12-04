alter type propertysearcher.userstatus rename to user_type;
alter type propertysearcher.user_type rename value 'ANONYMOUS' to 'UNREGISTERED';

alter table propertysearcher."user"
    drop column type;

alter table propertysearcher."user"
    rename status to type;

create type propertysearcher.user_profile_type as enum (
    'ANONYMOUS', 'GUEST', 'MAIN'
    );

create table propertysearcher.user_profile
(
    id                      bigint                             not null,
    user_id                 bigint                             not null,

    email                   varchar(255)                       not null,
    type                    propertysearcher.user_profile_type not null,

    created                 timestamp with time zone,
    updated                 timestamp with time zone,

    address                 jsonb,
    data                    jsonb,

    search_until            timestamp,

    customer_tenant_pool_id bigint,

    constraint pk_user_profile primary key (id),
    constraint fk_user_profile_user foreign key (user_id) references propertysearcher.user (id) match simple on update cascade on delete cascade,
    constraint fk_user_profile_custom_tenant_pool foreign key (customer_tenant_pool_id) references landlord.customer (id) match simple on update cascade on delete set null
);

create index if not exists idx_user_profile_user on propertysearcher.user_profile (user_id);
create index if not exists idx_user_profile_custom_tenant_pool on propertysearcher.user_profile (customer_tenant_pool_id);
create index if not exists idx_user_profile_id_search_until on propertysearcher.user_profile (id, search_until);
create index if not exists idx_user_profile_search_until on propertysearcher.user_profile (search_until);
create index if not exists gin_idx_user_profile_name_email on propertysearcher.user_profile using gin ((email || ' ' || (data ->> 'firstname') || ' ' || (data ->> 'name'))
                                                                                                       gin_trgm_ops);

create unique index uidx_main_profile on propertysearcher.user_profile (user_id) where type = 'MAIN';

create function propertysearcher.mapEnums(user_type propertysearcher.user_type
                                         )
    returns propertysearcher.user_profile_type
    language plpgsql
as
$$
begin
    if (user_type = 'REGISTERED') then
        return 'MAIN';
    end if;
    return 'ANONYMOUS';
end;
$$;


insert into propertysearcher.user_profile
select u.id,
       u.id,
       u.email,
       propertysearcher.mapEnums(u.type),
       u.created,
       u.updated,
       u.address,
       u.profile,
       u.search_until,
       u.customer_tenant_pool_id
from propertysearcher."user" u;

drop function propertysearcher.mapEnums;

alter table propertysearcher."user"
    drop column password,
    drop column expired,
    drop column locked,
    drop column profile,
    drop column address,
    drop column search_until,
    drop column customer_tenant_pool_id;

alter table landlord.schufa_job
    drop constraint fk_schufa_job_02;

alter table landlord.schufa_job
    rename user_id to user_profile_id;

alter table landlord.schufa_job
    add constraint fk_schufa_job_user_profile foreign key (user_profile_id) references propertysearcher.user_profile (id) match simple on update cascade on delete set null;


alter table shared.custom_question_response
    drop constraint uq_user_custom_question,
    drop constraint fk_custom_question_response_1;

alter table shared.custom_question_response
    rename user_id to user_profile_id;

alter table shared.custom_question_response
    add constraint fk_custom_question_response_user_profile foreign key (user_profile_id) references propertysearcher.user_profile (id) match simple on update cascade on delete cascade,
    add constraint uq_custom_question_response_user_profile_custom_question unique (user_profile_id, custom_question_id);


alter table shared.note
    drop constraint if exists uc01_customer_user,
    drop constraint if exists fk_note_user;

alter table shared.note
    rename user_id to user_profile_id;

alter table shared.note
    add constraint fk_note_user_profile_ foreign key (user_profile_id) references propertysearcher.user_profile (id) match simple on update cascade on delete cascade,
    add constraint uq_note_customer_user_profile unique (customer_id, user_profile_id);


alter table shared.application
    drop constraint fk_application_02;

alter table shared.application
    rename user_id to user_profile_id;

alter table shared.application
    add constraint fk_application_user_profile foreign key (user_profile_id) references propertysearcher.user_profile (id) match simple on update cascade on delete cascade;


alter table shared.propertyproposal
    drop constraint fk_propertyproposal_01,
    drop constraint uq_propertyproposal_01;

alter table shared.propertyproposal
    rename user_id to user_profile_id;

alter table shared.propertyproposal
    add constraint fk_propertyproposal_user_profile foreign key (user_profile_id) references propertysearcher.user_profile (id) match simple on update cascade on delete cascade,
    add constraint uq_propertyproposal_property_user_profile unique (property_id, user_profile_id);


alter table shared.propertytenant
    drop constraint uq_propertytenant_01,
    drop constraint fk_propertytenant_02;

alter table shared.propertytenant
    rename user_id to user_profile_id;

alter table shared.propertytenant
    add constraint fk_propertytenant_user_profile foreign key (user_profile_id) references propertysearcher.user_profile (id) match simple on update cascade on delete set null,
    add constraint uq_propertytenant_property_user_profile unique (property_id, user_profile_id);


alter table shared.self_disclosure_response
    drop constraint uq_user_self_disclosure_question_response,
    drop constraint fk_self_disclosure_response_1;

alter table shared.self_disclosure_response
    rename user_id to user_profile_id;

alter table shared.self_disclosure_response
    add constraint fk_self_disclosure_response_user_profile foreign key (user_profile_id) references propertysearcher.user_profile (id) match simple on update cascade on delete cascade,
    add constraint uq_self_disclosure_response_user_profile_property_question unique (user_profile_id, property_id, self_disclosure_id);


alter table propertysearcher.searchprofile
    drop constraint fk_searchprofile_01;

alter table propertysearcher.searchprofile
    rename user_id to user_profile_id;

alter table propertysearcher.searchprofile
    add constraint fk_searchprofile_user_profile foreign key (user_profile_id) references propertysearcher.user_profile (id) match simple on update cascade on delete cascade;

create type user_right_type as enum ('USER');

alter table propertysearcher.usertype_rights
    alter column usertype type user_right_type using usertype::text::user_right_type;

alter table propertysearcher.permissionscheme_rights
    alter column usertype type user_right_type using usertype::text::user_right_type;

drop type propertysearcher.usertype;

