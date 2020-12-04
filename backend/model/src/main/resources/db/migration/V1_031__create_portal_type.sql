CREATE TYPE shared.portalenum AS ENUM (
    'IMMOBILIENSCOUT24_DE',
    'IMMOBILIENSCOUT24_HM_DE',
    'IMMOBILIENSCOUT24_GC_DE',
    'KALAYDO_DE',
    'IMMOBILIEN_DE',
    'IMMONET_DE',
    'IMMOWELT_DE',
    'IVD',
    'EBAY',
    'IMMOEXPERTEN',
    'WORDPRESS_PLUGIN'
);

ALTER TABLE shared.application
DROP COLUMN portal;

ALTER TABLE shared.application
ADD portal shared.portalenum;

ALTER TABLE landlord.propertyportal
DROP COLUMN portal;

ALTER TABLE landlord.propertyportal
ADD portal shared.portalenum NOT NULL;