alter table landlord.property
    alter status SET DEFAULT 'DEFAULT'::landlord.propertystatus;

update landlord.property
set status = 'DEFAULT'
WHERE status is null;

alter table landlord.property
    alter column status set not null;