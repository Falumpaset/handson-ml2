update landlord.property
set data = jsonb_set(data, '{energyCertificate,demandCertificate}', '{"energyEfficiencyClass": "A_P"}')
where data -> 'energyCertificate' -> 'demandCertificate' ->> 'energyEfficiencyClass' in ('A+', 'A_PLUS');

update landlord.property
set data = jsonb_set(data, '{energyCertificate,demandCertificate}', '{"energyEfficiencyClass": null}')
where data -> 'energyCertificate' -> 'demandCertificate' ->> 'energyEfficiencyClass' = 'NOT_APPLICABLE';