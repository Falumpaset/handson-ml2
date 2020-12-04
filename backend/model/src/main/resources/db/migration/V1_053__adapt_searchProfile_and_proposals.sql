ALTER TABLE shared.propertyproposal
ADD searchprofile_id bigint NOT NULL;

ALTER TABLE shared.propertyproposal
ADD CONSTRAINT fk_searchprofile_01 FOREIGN KEY(searchprofile_id) REFERENCES propertysearcher.searchprofile(id)
ON UPDATE CASCADE
ON DELETE CASCADE;