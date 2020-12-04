ALTER TABLE landlord.customer
DROP COLUMN paymentmethods;

ALTER TABLE landlord.customer
ADD COLUMN paymentmethods jsonb DEFAULT '[{"method":"DEFAULT","preferred": true}]'::jsonb;


ALTER TABLE propertysearcher.customer
DROP COLUMN paymentmethods;

ALTER TABLE propertysearcher.customer
ADD COLUMN paymentmethods jsonb DEFAULT '[{"method":"DEFAULT","preferred": true}]'::jsonb;