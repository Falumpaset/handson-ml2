update landlord.prioset
set data = jsonb_set(data, '{residents}',
                     jsonb_build_object('value', (data -> 'residents' ->> 'value')::numeric, 'lowerBound', 0,
                                        'upperBound', (data -> 'residents' ->> 'number')::numeric))
where data ->> 'residents' is not null;