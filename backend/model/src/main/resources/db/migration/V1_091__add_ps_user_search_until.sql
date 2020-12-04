
ALTER TABLE propertysearcher."user"
    ADD COLUMN search_until timestamp;
CREATE INDEX IF NOT EXISTS idx_id_search_until ON propertysearcher."user" (id, search_until);

CREATE INDEX IF NOT EXISTS idx_search_until ON propertysearcher."user" (search_until);

update propertysearcher."user" set search_until = now() + interval '8 weeks' where lastlogin is not null