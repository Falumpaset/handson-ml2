CREATE TABLE landlord.customersettings
(
  id              bigint                 NOT NULL,
  customer_id     bigint                 NOT NULL,
  subdomain       character varying(255),

  created         timestamp without time zone,
  updated         timestamp without time zone,

  CONSTRAINT settings_pkey PRIMARY KEY (id),
  CONSTRAINT uq_settings_customer UNIQUE (customer_id),
  CONSTRAINT fk_settings_01 FOREIGN KEY (customer_id)
  REFERENCES landlord.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);
