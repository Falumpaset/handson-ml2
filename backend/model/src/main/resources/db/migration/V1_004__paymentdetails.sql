
ALTER TABLE landlord.customer
  ADD COLUMN paymentdetails jsonb NOT NULL DEFAULT '{}';

ALTER TABLE propertysearcher.customer
  ADD COLUMN paymentdetails jsonb NOT NULL DEFAULT '{}';
    