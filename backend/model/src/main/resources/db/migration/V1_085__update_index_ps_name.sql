DROP INDEX IF EXISTS propertysearcher.idx_profile_name_mail;

CREATE INDEX IF NOT EXISTS gin_idx_name_email ON propertysearcher."user" USING gin ((email || ' ' || (profile ->> 'firstname') || ' ' || (profile ->> 'name'))
                                                                                    gin_trgm_ops);