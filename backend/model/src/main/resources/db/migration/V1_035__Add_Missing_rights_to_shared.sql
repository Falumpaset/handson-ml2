INSERT INTO shared."right" (id, description, name, shortcode, "group", created, updated)
VALUES (1001, '', 'Shortlist', 'shortlist', 'shortlist', NOW(), NOW())
ON CONFLICT DO NOTHING;

INSERT INTO shared."right" (id, description, name, shortcode, "group", created, updated)
VALUES (1101, '', 'Export-to-Portals', 'export_to_portals', 'exporttoportals', NOW(), NOW())
ON CONFLICT DO NOTHING;

INSERT INTO shared."right" (id, description, name, shortcode, "group", created, updated)
VALUES (1201, '', 'Data-insights', 'data-insights', 'analytics', NOW(), NOW())
ON CONFLICT DO NOTHING;

INSERT INTO shared."right" (id, description, name, shortcode, "group", created, updated)
VALUES (2000, 'Lets only company admins see the managing tabs', 'View-Manager-Tab', 'menu_item_manage', 'ui-menu', NOW(), null)
ON CONFLICT DO NOTHING;

INSERT INTO shared."right" (id, description, name, shortcode, "group", created, updated)
VALUES (2001, 'Lets you view analytics tab', 'View-AnalyticsTab', 'menu_item_analytics', 'ui-menu', NOW(), null)
ON CONFLICT DO NOTHING;

INSERT INTO shared."right" (id, description, name, shortcode, "group", created, updated)
VALUES (2002, 'show email editor', 'Show-Email-Editor', 'email_editor_show', 'addon', NOW(), null)
ON CONFLICT DO NOTHING;

INSERT INTO shared."right" (id, description, name, shortcode, "group", created, updated)
VALUES (2003, 'show branding', 'Show-Branding', 'branding_show', 'addon', NOW(), null)
ON CONFLICT DO NOTHING;

INSERT INTO shared."right" (id, description, name, shortcode, "group", created, updated)
VALUES (2004, 'show portals', 'Show-Portals', 'portals_show', 'addon', NOW(), null)
ON CONFLICT DO NOTHING;

INSERT INTO shared."right" (id, description, name, shortcode, "group", created, updated)
VALUES (2005, 'show shortlist', 'Show-ShortList', 'shortlist_show', 'addon', NOW(), null)
ON CONFLICT DO NOTHING;

INSERT INTO shared."right" (id, description, name, shortcode, "group", created, updated)
VALUES (2006, 'show inventotry import', 'Show-Inventory-Import', 'inventory_import_show', 'addon', NOW(), null)
ON CONFLICT DO NOTHING;