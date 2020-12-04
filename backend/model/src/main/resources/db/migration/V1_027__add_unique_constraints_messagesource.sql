ALTER TABLE landlord.messagesource
ADD CONSTRAINT UC_MessageSource UNIQUE (customer_id,messagekey,locale);