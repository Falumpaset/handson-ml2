
    
ALTER TYPE landlord.customersize RENAME TO customersize_old;

CREATE TYPE           landlord.customersize AS ENUM ('PRIVATE', 'SMALL',
    'MEDIUM', 'LARGE', 'ENTERPRISE');

ALTER TABLE landlord.customer
  ALTER COLUMN customersize TYPE landlord.customersize USING customersize::text::landlord.customersize;
  
DROP TYPE landlord.customersize_old;
    
UPDATE landlord.customer SET customersize = 'ENTERPRISE' WHERE customersize IS NULL;
    
ALTER TABLE landlord.customer
    ALTER customersize SET NOT NULL;