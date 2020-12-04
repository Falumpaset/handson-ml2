INSERT INTO shared."right" (id, description, name, shortcode, "group", created, updated)
VALUES (2007, 'email editor booked - show templates', 'Show-Multi-Templates', 'email_templates_show', 'ui-menu', NOW
    (), NOW()) ON CONFLICT DO NOTHING ;

INSERT INTO landlord."right" (id, right_id, created)
VALUES (2007, 2007, NOW()) ON CONFLICT DO NOTHING;

INSERT INTO landlord.permissionscheme_rights (id, permission_scheme_id, right_id, usertype)
VALUES (106, 100, 2007, 'COMPANYADMIN') ON CONFLICT DO NOTHING ;

INSERT INTO landlord.permissionscheme_rights (id, permission_scheme_id, right_id, usertype)
VALUES (107, 100, 2007, 'EMPLOYEE') ON CONFLICT DO NOTHING ;