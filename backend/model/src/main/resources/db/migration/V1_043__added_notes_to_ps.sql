CREATE TABLE shared.note (
  id          bigint NOT NULL,
  customer_id bigint NOT NULL,
  user_id     bigint NOT NULL,
  comments    jsonb  NOT NULL,

  CONSTRAINT note_pkey PRIMARY KEY (id),
  CONSTRAINT fk_note_cust FOREIGN KEY (customer_id)
    REFERENCES landlord.customer (id) MATCH SIMPLE
      ON DELETE SET NULL,
  CONSTRAINT fk_note_user FOREIGN KEY (user_id)
    REFERENCES propertysearcher."user" (id) MATCH SIMPLE
      ON UPDATE CASCADE
      ON DELETE CASCADE
);
