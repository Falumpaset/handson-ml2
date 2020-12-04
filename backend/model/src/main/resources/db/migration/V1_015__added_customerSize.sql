DROP   TYPE IF EXISTS landlord.customersize CASCADE;
CREATE TYPE           landlord.customersize AS ENUM ('PRIVATE', 'SMALL',
    'MEDIUM', 'LARGE');

ALTER TABLE landlord.customer
    ADD COLUMN customersize landlord.customersize;