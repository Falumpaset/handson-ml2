CREATE TYPE landlord.publishstate AS ENUM ('SUCCESS', 'ERROR');

CREATE TABLE landlord.publish_log
(
  id      bigint NOT NULL,
  property_id    bigint,
  agent_info  jsonb,
  customer_id bigint not null ,
  error  text,
  portals jsonb,
  publish_state  landlord.publishstate,
  property_task landlord.property_task,
  created timestamp without time zone,
  updated timestamp without time zone,
  CONSTRAINT pk_publish_log PRIMARY KEY (id),
  CONSTRAINT fk_publish_log FOREIGN KEY (customer_id)
    REFERENCES landlord.customer (id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_property_id ON landlord.publish_log(property_id);