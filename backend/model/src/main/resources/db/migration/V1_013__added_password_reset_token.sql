CREATE TABLE landlord.user_change_password
(
  id          bigint                      NOT NULL,
  token       character varying(255)      NOT NULL,
  user_id     bigint                      NOT NULL,
  validuntil  timestamp without time zone,
  created     timestamp without time zone NOT NULL,
  updated     timestamp without time zone NOT NULL,
  CONSTRAINT user_change_password_pkey PRIMARY KEY (id),
  CONSTRAINT user_change_password_token_key UNIQUE (token),
  CONSTRAINT user_change_password_user_id_fkey FOREIGN KEY (user_id)
  REFERENCES landlord."user" (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);

CREATE TABLE propertysearcher.user_change_password
(
  id          bigint                      NOT NULL,
  token       character varying(255)      NOT NULL,
  user_id     bigint                      NOT NULL,
  validuntil  timestamp without time zone,
  created     timestamp without time zone NOT NULL,
  updated     timestamp without time zone NOT NULL,
  CONSTRAINT user_change_password_pkey PRIMARY KEY (id),
  CONSTRAINT user_change_password_token_key UNIQUE (token),
  CONSTRAINT user_change_password_user_id_fkey FOREIGN KEY (user_id)
  REFERENCES propertysearcher."user" (id) MATCH SIMPLE
  ON UPDATE CASCADE
  ON DELETE CASCADE
);