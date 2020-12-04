alter table shared.application add column archived timestamp;

alter table shared.application drop constraint if exists uq_application_01;
drop index if exists shared.uq_application_01;

create unique index uq_application_01 on shared.application (property_id, user_id) where archived IS NULL;

create type shared.application_archive_unit as enum (
    'MONTH',
    'DAY',
    'WEEK'
    );

alter table landlord.customersettings add column application_archive_unit shared.application_archive_unit default 'MONTH'::shared.application_archive_unit;
alter table landlord.customersettings add column application_archive_amount INTEGER default 6;
alter table landlord.customersettings add column application_archive_active boolean default false;