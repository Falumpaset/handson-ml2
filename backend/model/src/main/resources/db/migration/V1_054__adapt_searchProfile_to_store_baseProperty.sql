ALTER TABLE propertysearcher.searchprofile
ADD property_id bigint;

ALTER TABLE propertysearcher.searchprofile
ADD CONSTRAINT fk_property_02 FOREIGN KEY(property_id) REFERENCES landlord.property(id)
ON UPDATE CASCADE
ON DELETE SET NULL;