ALTER TABLE propertysearcher.permissionscheme_rights DROP COLUMN type;

ALTER TABLE propertysearcher.permissionscheme_rights
  ADD COLUMN usertype propertysearcher.usertype NOT NULL;
