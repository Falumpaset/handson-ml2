CREATE TABLE shared.shorturl
(
  id bigint NOT NULL,
  externalId varchar NOT NULL,
  redirectUrl varchar NOT NULL,
  called integer,
  expires timestamp without time zone,
  created timestamp without time zone,
  updated timestamp without time zone,
  properties jsonb,
  redirectWithToken boolean,

  CONSTRAINT shorturl_pkey PRIMARY KEY (id)
);