DROP TYPE IF EXISTS landlord.invoice_status CASCADE;
CREATE TYPE landlord.invoice_status AS ENUM ('INVOICED', 'CHARGED', 'PENDING', 'PROCESSING', 'ERROR', 'UNKNOWN', 'ACTIVE', 'CANCELED', 'REFUNDED');

CREATE TABLE landlord.invoice
(
  id bigint NOT NULL,
  status landlord.invoice_status NOT NULL,
  invoice_id bigint NOT NULL,
  customer_id bigint NOT NULL,
  invoice_date timestamp without time zone,
  charge_id varchar(256),
  line_items jsonb,
  discount jsonb,
  address jsonb,
  price double precision NOT NULL,
  post_discount_price double precision,
  currency constants.currencytype  NOT NULL,
  tax_rate double precision,
  cancellation boolean NOT NULL DEFAULT false,
  paymentmethod shared.paymentmethod NOT NULL,
  created timestamp without time zone,
  updated timestamp without time zone,

  CONSTRAINT invoice_pkey PRIMARY KEY (id),
  CONSTRAINT fk_invoice_customer FOREIGN KEY (customer_id)
  REFERENCES landlord.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

DROP TYPE IF EXISTS propertysearcher.invoice_status CASCADE;
CREATE TYPE propertysearcher.invoice_status AS ENUM ('INVOICED', 'CHARGED', 'PENDING', 'PROCESSING', 'ERROR', 'UNKNOWN', 'ACTIVE', 'CANCELED', 'REFUNDED');

CREATE TABLE propertysearcher.invoice
(
  id bigint NOT NULL,
  status propertysearcher.invoice_status NOT NULL,
  invoice_id bigint NOT NULL,
  customer_id bigint NOT NULL,
  invoice_date timestamp without time zone,
  charge_id varchar(256),
  line_items jsonb,
  discount jsonb,
  address jsonb,
  price double precision NOT NULL,
  post_discount_price double precision,
  currency constants.currencytype  NOT NULL,
  tax_rate double precision,
  cancellation boolean NOT NULL DEFAULT false,
  paymentmethod shared.paymentmethod NOT NULL,
  created timestamp without time zone,
  updated timestamp without time zone,

  CONSTRAINT invoice_pkey PRIMARY KEY (id),
  CONSTRAINT fk_invoice_customer FOREIGN KEY (customer_id)
  REFERENCES propertysearcher.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);