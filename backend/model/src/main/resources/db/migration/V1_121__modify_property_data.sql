UPDATE landlord.property SET data = jsonb_set(data, '{ground}', '"PARQUET"') where data->> 'ground' = 'PARQUETT';