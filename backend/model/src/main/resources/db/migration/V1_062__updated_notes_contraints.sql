ALTER TABLE shared.note
ADD CONSTRAINT uc01_customer_user UNIQUE (customer_id, user_id);