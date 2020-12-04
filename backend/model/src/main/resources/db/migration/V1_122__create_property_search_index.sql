drop index if exists landlord.gin_idx_property;

CREATE INDEX  gin_idx_property ON landlord.property USING gin
    (( customer_id || ' ' ||
       COALESCE(data -> 'address' ->> 'street', '')||  ' ' ||
       COALESCE(data -> 'address' ->> 'houseNumber', '') ||  ' ' ||
       COALESCE(data -> 'address' ->> 'zipCode', '') ||  ' ' ||
       COALESCE(data -> 'address' ->> 'region', '') ||  ' ' ||
       COALESCE(data -> 'address' ->> 'city', '') ||  ' ' ||
       COALESCE(data -> 'address' ->> 'country', '') ||  ' ' ||
       COALESCE(data ->> 'externalId', '')   ||  ' ' ||
       COALESCE(data ->> 'name','')
         )
     gin_trgm_ops);

