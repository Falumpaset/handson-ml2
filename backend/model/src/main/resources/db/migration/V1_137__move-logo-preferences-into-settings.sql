update landlord.customersettings set logo = sub_query.logo_obj
FROM(
        select id,
               CONCAT(
                       '{"name": null, "type": "IMG", "index": 1, "title": "", "encrypted": false, ',
                       '"extension": "',
                       split_part(substring(COALESCE(preferences->>'logo',''), strpos(COALESCE(preferences->>'logo',''), 'IMG-') + 4)::text, '.', 2),
                       '", "url": "',
                       COALESCE(preferences->>'logo', ''), '", "identifier":"',
                       split_part(substring(COALESCE(preferences->>'logo',''), strpos(COALESCE(preferences->>'logo',''), 'IMG-') + 4)::text, '.', 1),
                       '"}'
                   )::jsonb as logo_obj
        FROM landlord.customer
        where COALESCE(preferences ->> 'logo',null) IS NOT NULL
    ) as sub_query
WHERE customersettings.customer_id = sub_query.id;

update landlord.customer as customer SET preferences = sub_preferences::jsonb
FROM (
         select
             id,
             preferences::jsonb - 'logo' as sub_preferences
         from landlord.customer where COALESCE(preferences ->> 'logo', null) IS NOT NULL
     ) as sub_query
where customer.id = sub_query.id;

