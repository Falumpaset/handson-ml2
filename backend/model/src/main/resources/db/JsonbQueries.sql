Update landlord.property
Set data = data - 'atAddition';

UPDATE landlord.prioset
SET data = jsonb_set(data, '{monthlyIncome}', jsonb '{"value": 10, "lowerBound": 1000, "upperBound": 10000}');


UPDATE landlord.prioset
SET data = jsonb_set(data, '{householdType}', jsonb '{"value": 10, "choice": null}');



UPDATE landlord.prioset
Set data = data - 'jobType';
SET data = jsonb_set(data, '{employmentType}', jsonb '{"value": 10, "choice": null}');

UPDATE landlord.property
SET data = jsonb_set(data #- '{energyCertificate,usageCertificate,inludesHeatConsumption}',
                                '{energyCertificate,usageCertificate,includesHeatConsumption}',
                                data#>'{energyCertificate,usageCertificate,inludesHeatConsumption}')
WHERE data#>'{energyCertificate,usageCertificate}' ? 'inludesHeatConsumption';



-- Get a list of all flats paired with their external ID for a customer
SELECT id, data -> 'name' AS name, data -> 'externalId' AS externalId
FROM landlord.property
WHERE customer_id = 1031856
ORDER BY name asc, externalId asc;


UPDATE landlord.property p
SET data = jsonb_set(p.data, '{externalId}', to_jsonb(regexp_replace(data->>'externalId', '\....', '')), false)
Where p.customer_id=2000003 and p.data->'externalId' is not null;


-- exchanging reference id and external id of a flat
Select data->'externalId' as externalId, data -> 'referenceId' as referenceId from landlord.property where id = 1084251;

Alter table landlord.property
  drop COLUMN if exists temp;

ALTER Table landlord.property
  ADD COLUMN temp varchar(255);

Update landlord.property
Set temp = data->>'externalId'
WHERE id = 1084251;

Update landlord.property
SET data = jsonb_set(data, '{externalId}', to_jsonb(data->>'referenceId'))
WHERE id = 1084251;

Select temp from landlord.property where id=1084251;

Update landlord.property
SET data = jsonb_set(data, '{referenceId}', to_jsonb(temp))
WHERE id = 1084251;

Alter table landlord.property
  drop COLUMN temp;

Select data->'externalId' as externalId, data -> 'referenceId' as referenceId from landlord.property where id = 1084251;