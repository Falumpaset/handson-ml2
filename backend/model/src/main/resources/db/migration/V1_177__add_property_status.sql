create type landlord.propertystatus as enum ('IMPORTED', 'RESERVED');

alter table landlord.property
    add column status landlord.propertystatus;