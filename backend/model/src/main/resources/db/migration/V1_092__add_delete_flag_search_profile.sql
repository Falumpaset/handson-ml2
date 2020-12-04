UPDATE propertysearcher.searchprofile
SET data = jsonb_set(data, '{radius}', to_jsonb(5000), TRUE);

ALTER TABLE propertysearcher.searchprofile
    ADD COLUMN deleted boolean default false;

drop index if exists propertysearcher.searchprofile_gist;

create index searchprofile_gist
    on propertysearcher.searchprofile (location, deleted);

create index idx_user_id_deleted
    on propertysearcher.searchprofile (user_id, deleted);
