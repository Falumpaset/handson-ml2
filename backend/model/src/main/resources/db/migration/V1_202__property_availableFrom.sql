update landlord.property
SET data = jsonb_set(data, '{availableFrom}',
                     jsonb_build_object('dateAvailable', null, 'stringAvailable', data ->> 'availableFrom'));