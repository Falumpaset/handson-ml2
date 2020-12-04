SELECT AddGeometryColumn('landlord', 'property', 'location', 4326, 'POINT', 2);

--build a 2D-index
CREATE INDEX properties_gist ON landlord.property USING GIST ( location );
UPDATE landlord.property
SET location = ST_SetSRID(ST_PointFromText('POINT(' ||
    CAST(
      COALESCE(
        NULLIF(regexp_replace(data #>> '{address,coordinates,longitude}', '[^-0-9.]+', '', 'g'),'')
      ,'0.0') AS DOUBLE PRECISION
    ) || ' ' ||
    CAST(
      COALESCE(
        NULLIF(regexp_replace(data #>> '{address,coordinates,latitude}', '[^-0-9.]+', '', 'g'),''),
      '0.0') AS DOUBLE PRECISION
    ) || ')', 4326),4326);
