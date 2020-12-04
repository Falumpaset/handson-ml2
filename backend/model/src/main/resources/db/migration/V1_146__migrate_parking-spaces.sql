UPDATE landlord.property property
SET data=jsonb_set(
        data,
        '{parkingSpaces}',
        sub_query.parking_spaces_json
    )
FROM (
         SELECT
             id,
             jsonb_agg(
                     jsonb_build_object('price', 0.0, 'count', 1, 'type', parkingSpace)
                 ) parking_spaces_json
         FROM (
                  SELECT
                      id,
                      json_array_elements((data->>'parkingSpaceTypes')::json) AS parkingSpace
                  FROM landlord.property
              ) parking_spaces_query
         GROUP BY parking_spaces_query.id
     ) sub_query
where property.id = sub_query.id;;

update landlord.property as property SET data = sub_data::jsonb
FROM (
         select
             id,
             data::jsonb - 'parkingSpaceTypes' - 'parkingSpace' as sub_data
         from landlord.property
     ) as sub_query
where property.id = sub_query.id;