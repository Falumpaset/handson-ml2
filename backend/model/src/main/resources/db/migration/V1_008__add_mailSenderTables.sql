

CREATE TABLE landlord.emails
(
  id bigint NOT NULL,
  customer_id bigint NOT NULL,
  user_id bigint,
  message jsonb NOT NULL,
  template varchar(255),
  created timestamp without time zone,
  CONSTRAINT emails_pkey PRIMARY KEY (id),
  CONSTRAINT fk_emails_01 FOREIGN KEY (customer_id)
  REFERENCES landlord.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_emails_02 FOREIGN KEY (customer_id, user_id)
  REFERENCES landlord."user" (customer_id, id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

ALTER TABLE propertysearcher."user"
  ADD CONSTRAINT uq_user_02 UNIQUE (id, customer_id);

CREATE TABLE propertysearcher.emails
(
  id bigint NOT NULL,
  customer_id bigint NOT NULL,
  user_id bigint,
  message jsonb NOT NULL,
  template varchar(255),
  created timestamp without time zone,
  CONSTRAINT emails_pkey PRIMARY KEY (id),
  CONSTRAINT fk_emails_01 FOREIGN KEY (customer_id)
  REFERENCES propertysearcher.customer (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE,
  CONSTRAINT fk_emails_02 FOREIGN KEY (customer_id, user_id)
  REFERENCES propertysearcher."user" (customer_id, id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);
