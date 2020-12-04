INSERT INTO landlord.permissionscheme_rights
  (id, permission_scheme_id, right_id, usertype)
VALUES
  (101, 100, 166, 'COMPANYADMIN' :: landlord.usertype),
  (102, 100, 197, 'COMPANYADMIN' :: landlord.usertype),
  (103, 100, 198, 'COMPANYADMIN' :: landlord.usertype),
  (104, 100, 199, 'COMPANYADMIN' :: landlord.usertype),
  (105, 100, 200, 'COMPANYADMIN' :: landlord.usertype),
  (201, 200, 1201, 'COMPANYADMIN' :: landlord.usertype),
  (301, 300, 1101, 'COMPANYADMIN' :: landlord.usertype),
  (401, 400, 1001, 'COMPANYADMIN' :: landlord.usertype),
  (501, 500, 165, 'COMPANYADMIN' :: landlord.usertype),
  (502, 500, 201, 'COMPANYADMIN' :: landlord.usertype),
  (503, 500, 202, 'COMPANYADMIN' :: landlord.usertype),
  (504, 500, 203, 'COMPANYADMIN' :: landlord.usertype),
  (505, 500, 204, 'COMPANYADMIN' :: landlord.usertype),
  (601, 600, 195, 'COMPANYADMIN' :: landlord.usertype),
  (602, 600, 196, 'COMPANYADMIN' :: landlord.usertype),
  (603, 600, 205, 'COMPANYADMIN' :: landlord.usertype),
  (604, 600, 206, 'COMPANYADMIN' :: landlord.usertype),
  (605, 200, 2001, 'COMPANYADMIN' :: landlord.usertype),
  (605, 100, 2002, 'COMPANYADMIN' :: landlord.usertype),
  (606, 500, 2003, 'COMPANYADMIN' :: landlord.usertype),
  (607, 400, 2005, 'COMPANYADMIN' :: landlord.usertype),
  (608, 600, 2006, 'COMPANYADMIN' :: landlord.usertype),
  (609, 300, 2004, 'COMPANYADMIN' :: landlord.usertype)
ON CONFLICT
  DO NOTHING;