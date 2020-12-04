CREATE TABLE propertysearcher.prospectoptin
(
  id                  bigint                      NOT NULL,
  user_id             bigint                      NOT NULL,
  opt_in_for_prospect boolean ,
  created             timestamp without time zone NOT NULL,
  updated             timestamp without time zone,
  CONSTRAINT ps_pr_oi_pkey PRIMARY KEY (id),
  CONSTRAINT ps_pr_oi_u_fkey FOREIGN KEY (user_id)
    REFERENCES propertysearcher."user" (id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

DO
$do$
DECLARE
  userId bigint;
BEGIN

  FOR userId IN
    SELECT u.id
    FROM propertysearcher."user" u
    LEFT JOIN propertysearcher.prospectoptin oi on u.id = oi.user_id
    WHERE oi.id IS NULL
  LOOP
    INSERT INTO propertysearcher.prospectoptin(id, user_id, opt_in_for_prospect, created)
    VALUES (userId, userId, false, now());
  END LOOP;

END
$do$;