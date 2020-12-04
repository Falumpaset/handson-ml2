ALTER TABLE propertysearcher.user
ADD CONSTRAINT UC_usercustomerrelation UNIQUE (customer_id);