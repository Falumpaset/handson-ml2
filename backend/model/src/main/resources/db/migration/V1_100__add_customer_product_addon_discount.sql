CREATE TABLE landlord.discount_customer_product_addon
(
    id                           bigint                   NOT NULL,
    customer_id                  bigint                   NOT NULL,
    product_addon_id             bigint                   NOT NULL,
    discount_id                  bigint                   NOT NULL,

    CONSTRAINT discount_customer_product_addon_pkey PRIMARY KEY (id),
    CONSTRAINT fk_discount_customer_product_addon_01 FOREIGN KEY (customer_id)
        REFERENCES landlord.customer (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_discount_customer_product_addon_02 FOREIGN KEY (product_addon_id)
        REFERENCES landlord.productaddon (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_discount_customer_product_addon_03 FOREIGN KEY (discount_id)
        REFERENCES shared.discount (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

create index fki_fk_discount_customer_product_addon_1 on landlord.discount_customer_product_addon (customer_id);
create index fki_fk_discount_customer_product_addon_2 on landlord.discount_customer_product_addon (product_addon_id);
create index fki_fk_discount_customer_product_addon_3 on landlord.discount_customer_product_addon (discount_id);
create unique index idx_customer_product_addon on landlord.discount_customer_product_addon (customer_id, product_addon_id);