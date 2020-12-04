Update landlord."user"
Set type = 'EMPLOYEE' WHERE type = 'HOTLINE';

ALTER TYPE landlord.usertype RENAME TO usertype_old;

CREATE TYPE landlord.usertype AS ENUM ('COMPANYADMIN', 'EMPLOYEE');

ALTER TABLE landlord."user"
  ALTER COLUMN type TYPE landlord.usertype USING type::text::landlord.usertype;

DELETE FROM landlord.usertype_rights WHERE usertype = 'HOTLINE';

ALTER TABLE landlord.usertype_rights
  ALTER COLUMN usertype TYPE landlord.usertype USING usertype::text::landlord.usertype;

DELETE FROM landlord.permissionscheme_rights WHERE usertype = 'HOTLINE';

ALTER TABLE landlord.permissionscheme_rights
  ALTER COLUMN usertype TYPE landlord.usertype USING usertype::text::landlord.usertype;

DROP TYPE landlord.usertype_old;