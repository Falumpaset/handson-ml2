update landlord."user"
set profile = jsonb_set(profile, '{gender}', 'null')
where profile ->> 'gender' = 'NA';

update propertysearcher."user"
set profile = jsonb_set(profile, '{gender}', 'null')
where profile ->> 'gender' = 'NA';

update shared.digital_contract_signer set data = jsonb_set(data, '{gender}', 'null') where data ->> 'gender' = 'NA';