alter table landlord.property add column note jsonb;

update landlord.property set note = jsonb_build_object('content', data ->> 'objectNote');