alter table landlord.property add column external_id varchar(100);

CREATE INDEX customer_id_external_id on landlord.property (customer_id, external_id);

update landlord.property set external_id = data ->> 'externalId';

update landlord.property set data =  data - 'externalId';

