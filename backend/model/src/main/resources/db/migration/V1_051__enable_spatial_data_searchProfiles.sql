SELECT AddGeometryColumn('propertysearcher', 'searchprofile', 'location', 4326, 'POINT', 2);

--build a 2D-index
CREATE INDEX searchprofile_gist ON propertysearcher.searchprofile USING GIST ( location );