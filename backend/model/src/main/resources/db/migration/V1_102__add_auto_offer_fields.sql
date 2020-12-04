alter table landlord.property add column auto_offer_enabled boolean default false,
    add column auto_offer_threshold double precision default 0;
