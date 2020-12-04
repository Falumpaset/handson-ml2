
ALTER TABLE landlord.customer
  ADD COLUMN priceMultiplier float NOT NULL DEFAULT 1;

ALTER TABLE propertysearcher.customer
  ADD COLUMN priceMultiplier float NOT NULL DEFAULT 1;