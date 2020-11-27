CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis_topology;
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE SCHEMA IF NOT EXISTS landlord;
CREATE TABLE IF NOT EXISTS landlord.messagesource (
  id          bigint                  NOT NULL,
  customer_id bigint                  NOT NULL,
  messagekey  character varying(255)  NOT NULL,
  locale      character varying(10)   NOT NULL,
  value       text                    NOT NULL
);