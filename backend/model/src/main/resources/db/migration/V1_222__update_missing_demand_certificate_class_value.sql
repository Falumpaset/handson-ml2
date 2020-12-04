update landlord.property
set data = jsonb_set(data, '{energyCertificate,demandCertificate}', '{"energyEfficiencyClass": null}')
where data -> 'energyCertificate' -> 'demandCertificate' ->> 'energyEfficiencyClass' = '';