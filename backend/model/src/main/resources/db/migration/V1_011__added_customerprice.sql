
ALTER TABLE landlord.customer
    ADD CONSTRAINT uq_customer_01 UNIQUE (id, location);

ALTER TABLE landlord.productprice
    ADD COLUMN customer_id bigint;
    
ALTER TABLE landlord.productprice
    ADD CONSTRAINT fk_productprice_04 FOREIGN KEY (customer_id, location)
    REFERENCES landlord.customer (id, location) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE;
    
ALTER TABLE landlord.productaddonprice
    ADD COLUMN customer_id bigint;

ALTER TABLE landlord.productaddonprice
    ADD CONSTRAINT fk_productaddonprice_04 FOREIGN KEY (customer_id, location)
    REFERENCES landlord.customer (id, location) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE;

    
    
ALTER TABLE propertysearcher.customer
    ADD CONSTRAINT uq_customer_01 UNIQUE (id, location);

ALTER TABLE propertysearcher.productprice
    ADD COLUMN customer_id bigint;
    
ALTER TABLE propertysearcher.productprice
    ADD CONSTRAINT fk_productprice_04 FOREIGN KEY (customer_id, location)
    REFERENCES propertysearcher.customer (id, location) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE;
    
ALTER TABLE propertysearcher.productaddonprice
    ADD COLUMN customer_id bigint;

ALTER TABLE propertysearcher.productaddonprice
    ADD CONSTRAINT fk_productaddonprice_04 FOREIGN KEY (customer_id, location)
    REFERENCES propertysearcher.customer (id, location) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE;