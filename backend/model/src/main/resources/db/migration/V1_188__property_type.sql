create type landlord.property_type as enum ('FLAT','GARAGE', 'COMMERCIAL');

alter table landlord.property add column type landlord.property_type default 'FLAT';
