/*********** landlord.customer */
INSERT INTO landlord.customer
  (id, description, name, taxid, paymentmethods, customertype, invoiceemail, preferences, files, address, location, managementunits, created, updated, paymentdetails, pricemultiplier, customersize)
VALUES
  (
    1000000,
    null,
    'Viersener Aktien-Baugesellschaft, AG',
    null,
    ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],
    'OTHER' :: landlord.customertype,
    'marcus.rautzenberg@vab-viersen.de',
    '{}',
    '{}',
    null,
    'DE' :: shared.customerlocation,
    3600,
    now(),
    null,
    '{}',
    1,
    'MEDIUM' :: landlord.customersize
  );

/*********** landlord admin user */
INSERT INTO landlord."user"
  (id, email, password, customer_id, enabled, expired, locked, lastlogin, created, updated, preferences, profile, type)
VALUES
  (
    1000000,
    'marcus.rautzenberg@vab-viersen.de',
    null,
    1000000,
    true,
    false,
    false,
    null,
    now(),
    null,
    '{
      "domain": "vab-viersen.immomio.de"
    }',
    '{
      "name": "Rautzenberg", "firstname": "Marcus", "phone": null, "title": null, "gender": "MALE", "portrait": null
    }',
    'COMPANYADMIN' :: landlord.usertype
  );

/*********** PRODUCT */
INSERT INTO landlord.product
  (id, name, description, subscriptionperiod, created, updated)
VALUES
  (100000, 'Flatrate-Produkt', 'Flatrate-Produkt mit einer Vertragslaufzeit von einem Monat.', 'MONTHLY', NOW(), NOW()),
  (100001, 'Flatrate-Produkt', 'Flatrate-Produkt mit einer Vertragslaufzeit von einem Jahr.', 'YEARLY', NOW(), NOW());

/*********** PRODUCT_LIMITATION */
INSERT INTO landlord.product_limitations
(id, product_id, limitation, limitation_value)
VALUES
  (100000, 100000, 'SUBDOMAIN' :: landlord.productlimitation, TRUE),
  (100001, 100001, 'SUBDOMAIN' :: landlord.productlimitation, TRUE);

/*********** PRICE */
INSERT INTO landlord.price
  (id, fixedpart, variablepart, currency)
VALUES
  (100000, 0, 0, 'EUR' :: constants.currencytype),
  (100001, 0, 0, 'EUR' :: constants.currencytype),
  (200000, 0, 30, 'EUR' :: constants.currencytype),
  (200001, 0, 300, 'EUR' :: constants.currencytype),
  (200002, 0, 30, 'EUR' :: constants.currencytype),
  (200003, 0, 300, 'EUR' :: constants.currencytype),
  (200004, 0, 30, 'EUR' :: constants.currencytype),
  (200005, 0, 300, 'EUR' :: constants.currencytype),
  (200006, 0, 30, 'EUR' :: constants.currencytype),
  (200007, 0, 300, 'EUR' :: constants.currencytype),
  (200008, 0, 30, 'EUR' :: constants.currencytype),
  (200009, 0, 300, 'EUR' :: constants.currencytype),
  (200010, 0, 30, 'EUR' :: constants.currencytype),
  (200011, 0, 300, 'EUR' :: constants.currencytype),
  (200012, 30, 0, 'EUR' :: constants.currencytype),
  (200013, 300, 0, 'EUR' :: constants.currencytype);

/*********** PRODUCT_PRICE */
INSERT INTO landlord.productprice
(id, product_id, price_id, location, paymentmethods)
VALUES
  (100000, 100000, 100000, 'DE' :: shared.customerlocation, ARRAY ['STRIPE', 'INVOICE'] :: shared.PAYMENTMETHOD []),
  (100001, 100001, 100001, 'DE' :: shared.customerlocation, ARRAY ['STRIPE', 'INVOICE'] :: shared.PAYMENTMETHOD []);

/*********** ADDONS */
INSERT INTO landlord.addonproduct
  (id, name, description, producttype, created, updated, addontype)
VALUES
  (200000, 'ADDON_TITLE_EMAIL_EDITOR_L', 'ADDON_DESCRIPTION_EMAIL_EDITOR_L', 'SUBSCRIPTION' :: shared.producttype, NOW(), NOW(), 'EMAILEDITOR' :: landlord.addontype),
  (200001, 'ADDON_TITLE_EMAIL_EDITOR_L', 'ADDON_DESCRIPTION_EMAIL_EDITOR_L', 'SUBSCRIPTION' :: shared.producttype, NOW(), NOW(), 'EMAILEDITOR' :: landlord.addontype),
  (200002, 'ADDON_TITLE_DATA_INSIGHTS_L', 'ADDON_DESCRIPTION_DATA_INSIGHTS_L', 'SUBSCRIPTION' :: shared.producttype, NOW(), NOW(), 'DATAINSIGHTS' :: landlord.addontype),
  (200003, 'ADDON_TITLE_DATA_INSIGHTS_L', 'ADDON_DESCRIPTION_DATA_INSIGHTS_L', 'SUBSCRIPTION' :: shared.producttype, NOW(), NOW(), 'DATAINSIGHTS' :: landlord.addontype),
  (200004, 'ADDON_TITLE_PUBLISH_TO_PORTALS_L', 'ADDON_DESCRIPTION_PUBLISH_TO_PORTALS_L', 'SUBSCRIPTION' :: shared.producttype, NOW(), NOW(), 'PORTALPUBLISH' :: landlord.addontype),
  (200005, 'ADDON_TITLE_PUBLISH_TO_PORTALS_L', 'ADDON_DESCRIPTION_PUBLISH_TO_PORTALS_L', 'SUBSCRIPTION' :: shared.producttype, NOW(), NOW(), 'PORTALPUBLISH' :: landlord.addontype),
  (200006, 'ADDON_TITLE_SHORTLIST_L', 'ADDON_DESCRIPTION_SHORTLIST_L', 'SUBSCRIPTION' :: shared.producttype, NOW(), NOW(), 'SHORTLIST' :: landlord.addontype),
  (200007, 'ADDON_TITLE_SHORTLIST_L', 'ADDON_DESCRIPTION_SHORTLIST_L', 'SUBSCRIPTION' :: shared.producttype, NOW(), NOW(), 'SHORTLIST' :: landlord.addontype),
  (200008, 'ADDON_TITLE_CUSTOMISED_BRANDING_L', 'ADDON_DESCRIPTION_CUSTOMISED_BRANDING_L', 'SUBSCRIPTION' :: shared.producttype, NOW(), NOW(), 'BRANDING' :: landlord.addontype),
  (200009, 'ADDON_TITLE_CUSTOMISED_BRANDING_L', 'ADDON_DESCRIPTION_CUSTOMISED_BRANDING_L', 'SUBSCRIPTION' :: shared.producttype, NOW(), NOW(), 'BRANDING' :: landlord.addontype),
  (200010, 'ADDON_TITLE_INVENTORY_IMPORT_L', 'ADDON_DESCRIPTION_INVENTORY_IMPORT_L', 'SUBSCRIPTION' :: shared.producttype, NOW(), NOW(), 'IMPORT' :: landlord.addontype),
  (200011, 'ADDON_TITLE_INVENTORY_IMPORT_L', 'ADDON_DESCRIPTION_INVENTORY_IMPORT_L', 'SUBSCRIPTION' :: shared.producttype, NOW(), NOW(), 'IMPORT' :: landlord.addontype),
  (200012, 'ADDON_TITLE_HOMEPAGE_MODULE_L', 'ADDON_DESCRIPTION_HOMEPAGE_MODULE_L', 'SUBSCRIPTION' :: shared.producttype, NOW(), NOW(), 'HPMODULE' :: landlord.addontype),
  (200013, 'ADDON_TITLE_HOMEPAGE_MODULE_L', 'ADDON_DESCRIPTION_HOMEPAGE_MODULE_L', 'SUBSCRIPTION' :: shared.producttype, NOW(), NOW(), 'HPMODULE' :: landlord.addontype),
  (200014, 'ADDON_TITLE_AGENT_L', 'ADDON_DESCRIPTION_AGENT_L', 'SUBSCRIPTION' :: shared.producttype, NOW(), NOW(), 'AGENT' :: landlord.addontype),
  (200015, 'ADDON_TITLE_AGENT_L', 'ADDON_DESCRIPTION_AGENT_L', 'SUBSCRIPTION' :: shared.producttype, NOW(), NOW(), 'AGENT' :: landlord.addontype);

INSERT INTO landlord.addonproduct_limitations
  (id, addonproduct_id, limitation, limitation_value)
VALUES
  (200000, 200000, 'EMAIL_EDITOR' :: landlord.productlimitation, TRUE),
  (200001, 200001, 'EMAIL_EDITOR' :: landlord.productlimitation, TRUE);

/*********** PERMISSION_SCHEMES */
INSERT INTO landlord.permission_scheme
(id, description, name, created)
VALUES
  (100, 'Email Editor', 'Email Editor', NOW()),
  (200, 'Data insights', 'Analytics', NOW()),
  (300, 'Publish to Portals', 'Publish to Portals', NOW()),
  (400, 'Shortlist', 'Shortlist', NOW()),
  (500, 'Customised branding', 'Customised branding', NOW()),
  (600, 'Inventory Import', 'Inventory Import', NOW());

INSERT INTO landlord.addonproductpermissionscheme
  (id, addonproduct_id, permissionscheme_id)
VALUES
  (200000, 200000, 100),
  (200001, 200001, 100),
  (200002, 200002, 200),
  (200003, 200003, 200),
  (200004, 200004, 300),
  (200005, 200005, 300),
  (200006, 200006, 400),
  (200007, 200007, 400),
  (200008, 200008, 500),
  (200009, 200009, 500),
  (200010, 200010, 600),
  (200011, 200011, 600);

/*********** RIGHTS */
INSERT INTO shared.right
  (id, description, name, shortcode, "group", created, updated)
VALUES
  (1, '', 'Is-Tenant', 'is_tenant', 'base', null, null),
  (2, '', 'Is-Owner', 'is_owner', 'base', null, null),
  (3, '', 'Is-Admin', 'is_admin', 'base', null, null),
  (4, '', 'Is-EMPLOYEE', 'is_EMPLOYEE', 'base', null, null),
  (5, '', 'Is-Commercial', 'is_commercial', 'base', null, null),
  (6, '', 'Is-HOTLINE', 'is_HOTLINE', 'base', null, null),
  (7, '', 'Is-Starter', 'is_starter', 'base', null, null),
  (8, '', 'Blacklist-Show-all', 'blacklist_show_all', 'blacklist', null, null),
  (9, '', 'Blacklist-Show', 'blacklist_show', 'blacklist', null, null),
  (10, '', 'Blacklist-Create', 'blacklist_create', 'blacklist', null, null),
  (11, '', 'Blacklist-Edit', 'blacklist_edit', 'blacklist', null, null),
  (12, '', 'Blacklist-Delete', 'blacklist_delete', 'blacklist', null, null),
  (13, '', 'Country-Show-all', 'country_show_all', 'country', null, null),
  (14, '', 'Country-Show', 'country_show', 'country', null, null),
  (15, '', 'Country-Create', 'country_create', 'country', null, null),
  (16, '', 'Country-Edit', 'country_edit', 'country', null, null),
  (17, '', 'Country-Delete', 'country_delete', 'country', null, null),
  (18, '', 'Currency-Show-all', 'currency_show_all', 'currency', null, null),
  (19, '', 'Currency-Show', 'currency_show', 'currency', null, null),
  (20, '', 'Currency-Create', 'currency_create', 'currency', null, null),
  (21, '', 'Currency-Edit', 'currency_edit', 'currency', null, null),
  (22, '', 'Currency-Delete', 'currency_delete', 'currency', null, null),
  (23, '', 'User-Show-all', 'user_show_all', 'user', null, null),
  (24, '', 'User-Show', 'user_show', 'user', null, null),
  (25, '', 'User-Create', 'user_create', 'user', null, null),
  (26, '', 'User-Edit', 'user_edit', 'user', null, null),
  (27, '', 'User-Delete', 'user_delete', 'user', null, null),
  (28, '', 'User-Change-Email', 'user_change_email', 'user', null, null),
  (29, '', 'User-Confirm-Change-Email', 'user_confirm_change_email', 'user', null, null),
  (30, '', 'User-Reset-Password', 'user_reset_password', 'user', null, null),
  (31, '', 'User-Register', 'user_register', 'user', null, null),
  (32, '', 'User-Confirm-Account', 'user_confirm_account', 'user', null, null),
  (33, '', 'User-find-By-Email', 'user_find_by_email', 'user', null, null),
  (34, '', 'User-find-Multiple-By-Email', 'user_find_multiple_by_email', 'user', null, null),
  (35, '', 'User-find-By-User-Profile-Id', 'user_find_by_user_profile_id', 'user', null, null),
  (36, '', 'User-find-User-Count-By-Customer', 'user_find_user_count_by_customer', 'user', null, null),
  (37, '', 'User-find-By-Group', 'user_find_by_group', 'user', null, null),
  (38, '', 'User-find-User-By-Customer-Id-And-Group-Id', 'user_find_by_customer_and_group_id', 'user', null, null),
  (39, '', 'User-Profile-Show-all', 'user_profile_show_all', 'userprofile', null, null),
  (40, '', 'User-Profile-Show', 'user_profile_show', 'userprofile', null, null),
  (41, '', 'User-Profile-Create', 'user_profile_create', 'userprofile', null, null),
  (42, '', 'User-Profile-Edit', 'user_profile_edit', 'userprofile', null, null),
  (43, '', 'User-Profile-Delete', 'user_profile_delete', 'userprofile', null, null),
  (44, '', 'User-Profile-find_by_Email', 'user_profile_find_by_email', 'userprofile', null, null),
  (45, '', 'Group-Show-all', 'group_show_all', 'group', null, null),
  (46, '', 'Group-Show', 'group_show', 'group', null, null),
  (47, '', 'Group-Create', 'group_create', 'group', null, null),
  (48, '', 'Group-Edit', 'group_edit', 'group', null, null),
  (49, '', 'Group-Delete', 'group_delete', 'group', null, null),
  (50, '', 'Rights-Show-all', 'rights_show_all', 'rights', null, null),
  (51, '', 'Rights-Show', 'rights_show', 'rights', null, null),
  (52, '', 'Rights-Create', 'rights_create', 'rights', null, null),
  (53, '', 'Rights-Edit', 'rights_edit', 'rights', null, null),
  (54, '', 'Rights-Delete', 'rights_show', 'rights', null, null),
  (55, '', 'Token-Show-all', 'token_show_all', 'token', null, null),
  (56, '', 'Token-Show', 'token_show', 'token', null, null),
  (57, '', 'Token-Create', 'token_create', 'token', null, null),
  (58, '', 'Token-Edit', 'token_edit', 'token', null, null),
  (59, '', 'Token-Delete', 'token_delete', 'token', null, null),
  (60, '', 'Token-find-by-customer-id-and-key-and-token', 'token_find_by_customer_id_and_key_and_token', 'token', null, null),
  (61, '', 'Token-find-by-key-and-token', 'token_find_by_key_and_token', 'token', null, null),
  (62, '', 'Token-find-by-key', 'token_find_by_token', 'token', null, null),
  (63, '', 'Token-get-by-key', 'token_get_by_key', 'token', null, null),
  (64, '', 'Customer-Show-all', 'customer_show_all', 'country', null, null),
  (65, '', 'Customer-Show', 'customer_show', 'country', null, null),
  (66, '', 'Customer-Create', 'customer_create', 'country', null, null),
  (67, '', 'Customer-Edit', 'customer_edit', 'country', null, null),
  (68, '', 'Customer-Delete', 'customer_delete', 'country', null, null),
  (69, '', 'Customer-Preferences-Show-all', 'customer_preferences_show_all', 'customerpreferences', null, null),
  (70, '', 'Customer-Show', 'customer_preferences_show', 'customerpreferences', null, null),
  (71, '', 'Customer-Create', 'customer_preferences_create', 'customerpreferences', null, null),
  (72, '', 'Customer-Edit', 'customer_preferences_edit', 'customerpreferences', null, null),
  (73, '', 'Customer-Delete', 'customer_preferences_delete', 'customerpreferences', null, null),
  (74, '', 'Payment-Show-all', 'payment_show_all', 'payment', null, null),
  (75, '', 'Payment-Show', 'payment_show', 'payment', null, null),
  (76, '', 'Payment-Create', 'payment_create', 'payment', null, null),
  (77, '', 'Payment-Edit', 'payment_edit', 'payment', null, null),
  (78, '', 'Payment-Delete', 'payment_delete', 'payment', null, null),
  (79, '', 'Credential-Show-all', 'credential_show_all', 'credential', null, null),
  (80, '', 'Credential-Show', 'credential_show', 'credential', null, null),
  (81, '', 'Credential-Create', 'credential_create', 'credential', null, null),
  (82, '', 'Credential-Edit', 'credential_edit', 'credential', null, null),
  (83, '', 'Credential-Delete', 'credential_delete', 'credential', null, null),
  (84, '', 'Invoice-Show-all', 'invoice_show_all', 'invoice', null, null),
  (85, '', 'Invoice-Show', 'invoice_show', 'invoice', null, null),
  (86, '', 'Invoice-Create', 'invoice_create', 'invoice', null, null),
  (87, '', 'Invoice-Edit', 'invoice_edit', 'invoice', null, null),
  (88, '', 'Invoice-Delete', 'invoice_delete', 'invoice', null, null),
  (89, '', 'Product-Basket-Show-all', 'product_basket_show_all', 'productbasket', null, null),
  (90, '', 'Product-Basket-Show', 'product_basket_show', 'productbasket', null, null),
  (91, '', 'Product-Basket-Create', 'product_basket_create', 'productbasket', null, null),
  (92, '', 'Product-Basket-Edit', 'product_basket_edit', 'productbasket', null, null),
  (93, '', 'Product-Basket-Delete', 'product_basket_delete', 'productbasket', null, null),
  (94, '', 'Product-Category-Show-all', 'product_category_show_all', 'productcategory', null, null),
  (95, '', 'Product-Category-Show', 'product_category_show', 'productcategory', null, null),
  (96, '', 'Product-Category-Create', 'product_category_create', 'productcategory', null, null),
  (97, '', 'Product-Category-Edit', 'product_category_edit', 'productcategory', null, null),
  (98, '', 'Product-Category-Delete', 'product_category_delete', 'productcategory', null, null),
  (99, '', 'Product-Category-Find-by-name', 'product_category_find_by_name', 'productcategory', null, null),
  (100, '', 'Product-Show-all', 'product_show_all', 'product', null, null),
  (101, '', 'Product-Show', 'product_show', 'product', null, null),
  (102, '', 'Product-Create', 'product_create', 'product', null, null),
  (103, '', 'Product-Edit', 'product_edit', 'product', null, null),
  (104, '', 'Product-Delete', 'product_delete', 'product', null, null),
  (105, '', 'Product-Find-by-Product-Category-id', 'product_find_by_product_category_id', 'product', null, null),
  (106, '', 'Product-Find-by-Product-Category-name', 'product_find_by_product_category_name', 'product', null, null),
  (107, '', 'Product-Find-by-Prices-Period', 'product_find_by_prices_period', 'product', null, null),
  (108, '', 'Price-Show-all', 'price_show_all', 'price', null, null),
  (109, '', 'Price-Show', 'price_show', 'price', null, null),
  (110, '', 'Price-Create', 'price_create', 'price', null, null),
  (111, '', 'Price-Edit', 'price_edit', 'price', null, null),
  (112, '', 'Price-Delete', 'price_delete', 'price', null, null),
  (113, '', 'Specification-Show-all', 'specification_show_all', 'specification', null, null),
  (114, '', 'Specification-Show', 'specification_show', 'specification', null, null),
  (115, '', 'Specification-Create', 'specification_create', 'specification', null, null),
  (116, '', 'Specification-Edit', 'specification_edit', 'specification', null, null),
  (117, '', 'Specification-Delete', 'specification_delete', 'specification', null, null),
  (118, '', 'Limitation-Show-all', 'limitation_show_all', 'limitation', null, null),
  (119, '', 'Limitation-Show', 'limitation_show', 'limitation', null, null),
  (120, '', 'Limitation-Create', 'limitation_create', 'limitation', null, null),
  (121, '', 'Limitation-Edit', 'limitation_edit', 'limitation', null, null),
  (122, '', 'Limitation-Delete', 'limitation_delete', 'limitation', null, null),
  (123, '', 'Discount-Show-all', 'discount_show_all', 'discount', null, null),
  (124, '', 'Discount-Show', 'discount_show', 'discount', null, null),
  (125, '', 'Discount-Create', 'discount_create', 'discount', null, null),
  (126, '', 'Discount-Edit', 'discount_edit', 'discount', null, null),
  (127, '', 'Discount-Delete', 'discount_delete', 'discount', null, null),
  (128, '', 'Flat-Show-all', 'flat_show_all', 'flat', null, null),
  (129, '', 'Flat-Show', 'flat_show', 'flat', null, null),
  (130, '', 'Flat-Create', 'flat_create', 'flat', null, null),
  (131, '', 'Flat-Edit', 'flat_edit', 'flat', null, null),
  (132, '', 'Flat-Delete', 'flat_delete', 'flat', null, null),
  (133, '', 'preference-prioset-show-all', 'preference_prioset_show_all', 'preferenceprioset', null, null),
  (134, '', 'preference-prioset-show', 'preference_prioset_show', 'preferenceprioset', null, null),
  (135, '', 'preference-prioset-create', 'preference_prioset_create', 'preferenceprioset', null, null),
  (136, '', 'preference-prioset-edit', 'preference_prioset_edit', 'preferenceprioset', null, null),
  (137, '', 'preference-prioset-delete', 'preference_prioset_delete', 'preferenceprioset', null, null),
  (138, '', 'Preference-Prioset-find-by-Customer-Id', 'preference_prioset_find_by_customer_id', 'preferenceprioset', null, null),
  (139, '', 'Preference-Prioset-find-by-Name', 'preference_prioset_find_by_name', 'preferenceprioset', null, null),
  (140, '', 'Flat-find-by-Customer-id', 'flat_find_by_customer_id', 'flat', null, null),
  (141, '', 'Flat-find-by-State-and-Preference-Prioset', 'flat_find_by_state_and_preference_prioset', 'flat', null, null),
  (142, '', 'Flat-find-by-Keys-and-Values', 'flat_find_by_keys_and_values', 'flat', null, null),
  (143, '', 'Flat-Application-Show-all', 'flat_application_show_all', 'flatapplication', null, null),
  (144, '', 'Flat-Application-Show', 'flat_application_show', 'flatapplication', null, null),
  (145, '', 'Flat-Application-Create', 'flat_application_create', 'flatapplication', null, null),
  (146, '', 'Flat-Application-Edit', 'flat_application_edit', 'flatapplication', null, null),
  (147, '', 'Flat-Application-Delete', 'flat_application_delete', 'flatapplication', null, null),
  (148, '', 'Flat-Application-find-all-by-Flat-Invitation-id', 'flat_application_find_all_by_flat_invitation_id', 'flatapplication', null, null),
  (149, '', 'Flat-Application-find-all-by-Flat-active-and-applied', 'flat_application_find_all_by_flat_active_and_applied', 'flatapplication', null, null),
  (150, '', 'Flat-Application-find-all-by-Flat-and-sort-custom', 'flat_application_find_all_by_flat_and_sort_custom', 'flatapplication', null, null),
  (151, '', 'Flat-Application-find-by-Keys-and-Values', 'flat_application_find_by_keys_and_values', 'flatapplication', null, null),
  (152, '', 'Flat-Invitation-Show-all', 'flat_invitation_show_all', 'flatinvitation', null, null),
  (153, '', 'Flat-Invitation-Show', 'flat_invitation_show', 'flatinvitation', null, null),
  (154, '', 'Flat-Invitation-Create', 'flat_invitation_create', 'flatinvitation', null, null),
  (155, '', 'Flat-Invitation-Edit', 'flat_invitation_edit', 'flatinvitation', null, null),
  (156, '', 'Flat-Invitation-Delete', 'flat_invitation_delete', 'flatinvitation', null, null),
  (157, '', 'Flat-Invitation-find-by-Userprofile-id', 'flat_invitation_find_by_userprofile_id', 'flatinvitation', null, null),
  (158, '', 'Flat-Invitation-find-by-Userprofile-id-and-canceled', 'flat_invitation_find_by_userprofile_id_and_canceled', 'flatinvitation', null, null),
  (159, '', 'Flat-Invitation-find-by-Userprofile-id-and-Flat-id-and-canceled', 'flat_invitation_find_by_userprofile_id_and_flat_id_and_canceled', 'flatinvitation', null, null),
  (160, '', 'Flat-Invitation-find-by-canceled-and-date-after-and-date-before-and-flat', 'flat_invitation_find_by_canceled_and_date_after_and_date_before_and_flat', 'flatinvitation', null, null),
  (161, '', 'Menu-Item-Abo', 'menu_item_abo', 'ui-menu', null, null),
  (162, '', 'Menu-Item-Admin-Dashboard', 'menu_item_admin_dashboard', 'ui-menu', null, null),
  (163, '', 'Menu-Item-Agents', 'menu_item_agents', 'ui-menu', null, null),
  (164, '', 'Menu-Item-Applications', 'menu_item_applications', 'ui-menu', null, null),
  (165, '', 'Menu-Item-Branding', 'menu_item_branding', 'ui-menu', null, null),
  (166, '', 'Menu-Item-Email-Editor', 'menu_item_email_editor', 'ui-menu', null, null),
  (167, '', 'Menu-Item-create-Tenants', 'menu_item_create_tenants', 'ui-menu', null, null),
  (168, '', 'Menu-Item-Dashboard', 'menu_item_dashboard', 'ui-menu', null, null),
  (169, '', 'Menu-Item-Email-change', 'menu_item_email_change', 'ui-menu', null, null),
  (170, '', 'Menu-Item-Flats', 'menu_item_flats', 'ui-menu', null, null),
  (171, '', 'Menu-Item-Invitations', 'menu_item_invitations', 'ui-menu', null, null),
  (172, '', 'Menu-Item-Invitations-Owner', 'menu_item_invitations_owner', 'ui-menu', null, null),
  (173, '', 'Menu-Item-Orders', 'menu_item_orders', 'ui-menu', null, null),
  (174, '', 'Menu-Item-Password-change', 'menu_item_password_change', 'ui-menu', null, null),
  (175, '', 'Menu-Item-Personal', 'menu_item_personal', 'ui-menu', null, null),
  (176, '', 'Menu-Item-Personal-Commercial', 'menu_item_personal_commercial', 'ui-menu', null, null),
  (177, '', 'Menu-Item-Personal-Group', 'menu_item_personal_group', 'ui-menu', null, null),
  (178, '', 'Menu-Item-Portals', 'menu_item_portals', 'ui-menu', null, null),
  (179, '', 'Menu-Item-Priosets', 'menu_item_priosets', 'ui-menu', null, null),
  (180, '', 'Menu-Item-Search-Tenants', 'menu_item_search_tenants', 'ui-menu', null, null),
  (181, '', 'Menu-Item-Abo', 'menu_item_abo', 'ui-menu', null, null),
  (182, '', 'Tenant-Summary-Overview', 'tenant_summary_overview', 'ui-view-tenant-summary', null, null),
  (183, '', 'Tenant-Summary-News', 'tenant_summary_news', 'ui-view-tenant-summary', null, null),
  (184, '', 'Tenant-Summary-Solvency', 'tenant_summary_solvency', 'ui-view-tenant-summary', null, null),
  (185, '', 'Tenant-Summary-Documents', 'tenant_summary_documents', 'ui-view-tenant-summary', null, null),
  (186, '', 'Tenant-Summary-Comments', 'tenant_summary_comments', 'ui-view-tenant-summary', null, null),
  (187, '', 'Tenant-Summary-Short-Icons', 'tenant_summary_short_icons', 'ui-view-tenant-summary', null, null),
  (190, '', 'Ftp-Access-Show-All', 'ftp_access_show_all', 'ftpaccess', null, null),
  (191, '', 'Ftp-Access-Show', 'ftp_access_show_all', 'ftpaccess', null, null),
  (192, '', 'Ftp-Access-Create', 'ftp_access_show_create', 'ftpaccess', null, null),
  (193, '', 'Ftp-Access-Edit', 'ftp_access_show_edit', 'ftpaccess', null, null),
  (194, '', 'Ftp-Access-Delete', 'ftp_access_show_delete', 'ftpaccess', null, null),
  (195, '', 'Ftp-Import-Log-Show-all', 'ftp_import_log_show_all', 'ftpimportlog', null, null),
  (196, '', 'Ftp-Import-Log-Show', 'ftp_import_log_show', 'ftpimportlog', null, null),
  (197, '', 'Email-Editor-Show', 'email_editor_show', 'emaileditor', null, null),
  (198, '', 'Email-Editor-Create', 'email_editor_create', 'emaileditor', null, null),
  (199, '', 'Email-Editor-Edit', 'email_editor_edit', 'emaileditor', null, null),
  (200, '', 'Email-Editor-Delete', 'email_editor_delete', 'emaileditor', null, null),
  (201, '', 'Branding-Show', 'branding_show', 'branding', null, null),
  (202, '', 'Branding-Create', 'branding_create', 'branding', null, null),
  (203, '', 'Branding-Edit', 'branding_edit', 'branding', null, null),
  (204, '', 'Branding-Delete', 'branding_delete', 'branding', null, null),
  (205, '', 'Menu-Item-Ftp-Import-Log', 'menu_item_ftp_import_log', 'ui-menu', null, null),
  (206, '', 'Menu-Item-Ftp-Import-Access', 'menu_item_ftp_import_access', 'ui-menu', null, null),
  (207, '', 'Flat-Summary-Expose', 'flat_summary_expose', 'ui-view-flat-summary', null, null),
  (208, '', 'Menu-Item-Extra-Services', 'menu_item_extra_services', 'ui-menu', null, null),
  (210, '', 'Product-Change-Abo', 'product_change_abo', 'product', null, null),
  (220, '', 'Portal-Flat-Activate', 'portal_flat_activate', 'portal2', null, null),
  (221, '', 'Portal-Flat-Deactivate', 'portal_flat_deactivate', 'portal2', null, null),
  (222, '', 'Portal-Flat-Update', 'portal_flat_update', 'portal2', null, null),
  (223, '', 'Portal-Flat-Delete', 'portal_flat_delete', 'portal2', null, null),
  (230, '', 'Menu-Item-Customerfiles', 'menu_item_customerfiles', 'ui-menu', null, null),
  (231, '', 'Show-Prioset-Loader', 'show_prioset_loader', 'ui-view-flat', null, null),
  (232, '', 'Flat-Edit-WriteProtection', 'flat_edit_writeProtection', 'flat', null, null),
  (233, '', 'Flat-Set-As-Tenant', 'flat_set_as_tenant', 'flat', null, null),
  (1001, '', 'Shortlist', 'shortlist', 'shortlist', '2018-03-21 11:43:28.029816', '2018-03-21 11:43:28.029816'),
  (1101, '', 'Export-to-Portals', 'export_to_portals', 'exporttoportals', '2018-03-21 11:43:28.029816', '2018-03-21 11:43:28.029816'),
  (1201, '', 'Data-insights', 'data-insights', 'analytics', '2018-03-21 11:43:28.029816', '2018-03-21 11:43:28.029816'),
  (2000, 'Lets only company admins see the managing tabs', 'View-Manager-Tab', 'menu_item_manage', 'ui-menu', '2018-05-07 08:10:01.910538', null),
  (2001, 'Lets you view analytics tab', 'View-AnalyticsTab', 'menu_item_analytics', 'ui-menu', '2018-05-07 08:10:01.910538', null),
  (2002, 'show email editor', 'Show-Email-Editor', 'email_editor_show', 'addon', '2018-05-07 08:46:26.018062', null),
  (2003, 'show branding', 'Show-Branding', 'branding_show', 'addon', '2018-05-07 08:46:26.018062', null),
  (2004, 'show portals', 'Show-Portals', 'portals_show', 'addon', '2018-05-07 08:46:26.018062', null),
  (2005, 'show shortlist', 'Show-ShortList', 'shortlist_show', 'addon', '2018-05-07 08:46:26.018062', null),
  (2006, 'show inventotry import', 'Show-Inventory-Import', 'inventory_import_show', 'addon', '2018-05-07 08:46:26.018062', null)
ON CONFLICT
  DO NOTHING;

INSERT INTO landlord.right
  (id, right_id, created)
VALUES
  (2, 2, now()),
  (4, 4, now()),
  (5, 5, now()),
  (6, 6, now()),
  (13, 13, now()),
  (14, 14, now()),
  (18, 18, now()),
  (19, 19, now()),
  (24, 24, now()),
  (25, 25, now()),
  (26, 26, now()),
  (27, 27, now()),
  (28, 28, now()),
  (29, 29, now()),
  (30, 30, now()),
  (31, 31, now()),
  (32, 32, now()),
  (40, 40, now()),
  (42, 42, now()),
  (44, 44, now()),
  (65, 65, now()),
  (67, 67, now()),
  (70, 70, now()),
  (72, 72, now()),
  (75, 75, now()),
  (76, 76, now()),
  (77, 77, now()),
  (78, 78, now()),
  (80, 80, now()),
  (81, 81, now()),
  (82, 82, now()),
  (83, 83, now()),
  (85, 85, now()),
  (90, 90, now()),
  (91, 91, now()),
  (92, 92, now()),
  (94, 94, now()),
  (95, 95, now()),
  (96, 96, now()),
  (97, 97, now()),
  (99, 99, now()),
  (101, 101, now()),
  (105, 105, now()),
  (106, 106, now()),
  (108, 108, now()),
  (109, 109, now()),
  (114, 114, now()),
  (118, 118, now()),
  (119, 119, now()),
  (124, 124, now()),
  (129, 129, now()),
  (130, 130, now()),
  (131, 131, now()),
  (132, 132, now()),
  (134, 134, now()),
  (135, 135, now()),
  (136, 136, now()),
  (137, 137, now()),
  (138, 138, now()),
  (139, 139, now()),
  (140, 140, now()),
  (141, 141, now()),
  (142, 142, now()),
  (144, 144, now()),
  (145, 145, now()),
  (146, 146, now()),
  (148, 148, now()),
  (149, 149, now()),
  (150, 150, now()),
  (151, 151, now()),
  (153, 153, now()),
  (154, 154, now()),
  (155, 155, now()),
  (156, 156, now()),
  (157, 157, now()),
  (158, 158, now()),
  (159, 159, now()),
  (160, 160, now()),
  (163, 163, now()),
  (165, 165, now()),
  (166, 166, now()),
  (167, 167, now()),
  (168, 168, now()),
  (169, 169, now()),
  (170, 170, now()),
  (172, 172, now()),
  (173, 173, now()),
  (174, 174, now()),
  (175, 175, now()),
  (176, 176, now()),
  (177, 177, now()),
  (178, 178, now()),
  (179, 179, now()),
  (180, 180, now()),
  (182, 182, now()),
  (183, 183, now()),
  (184, 184, now()),
  (185, 185, now()),
  (186, 186, now()),
  (187, 187, now()),
  (195, 195, now()),
  (196, 196, now()),
  (197, 197, now()),
  (198, 198, now()),
  (199, 199, now()),
  (200, 200, now()),
  (201, 201, now()),
  (202, 202, now()),
  (203, 203, now()),
  (204, 204, now()),
  (205, 205, now()),
  (206, 206, now()),
  (207, 207, now()),
  (208, 208, now()),
  (210, 210, now()),
  (220, 220, now()),
  (221, 221, now()),
  (222, 222, now()),
  (223, 223, now()),
  (230, 230, now()),
  (231, 231, now()),
  (233, 233, now()),
  (1001, 1001, now()),
  (1101, 1101, now()),
  (1201, 1201, now()),
  (2000, 2000, now()),
  (2001, 2001, now()),
  (2002, 2002, now()),
  (2003, 2003, now()),
  (2004, 2004, now()),
  (2005, 2005, now()),
  (2006, 2006, now())
ON CONFLICT
  DO NOTHING;

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

/*********** PRODUCT_ADDON */
INSERT INTO landlord.productaddon
  (id, product_id, addonproduct_id)
VALUES
  (200000, 100000, 200000),
  (200001, 100001, 200001),
  (200002, 100000, 200002),
  (200003, 100001, 200003),
  (200004, 100000, 200004),
  (200005, 100001, 200005),
  (200006, 100000, 200006),
  (200007, 100001, 200007),
  (200008, 100000, 200008),
  (200009, 100001, 200009),
  (200010, 100000, 200010),
  (200011, 100001, 200011),
  (200014, 100000, 200014),
  (200015, 100001, 200015);

/*********** PRICE */
INSERT INTO landlord.price
  (id, fixedpart, variablepart, currency)
VALUES
  (200000, 0, 30, 'EUR' :: constants.currencytype),
  (200001, 0, 300, 'EUR' :: constants.currencytype),
  (200002, 0, 30, 'EUR' :: constants.currencytype),
  (200003, 0, 300, 'EUR' :: constants.currencytype),
  (200004, 0, 30, 'EUR' :: constants.currencytype),
  (200005, 0, 300, 'EUR' :: constants.currencytype),
  (200006, 0, 30, 'EUR' :: constants.currencytype),
  (200007, 0, 300, 'EUR' :: constants.currencytype),
  (200008, 0, 30, 'EUR' :: constants.currencytype),
  (200009, 0, 300, 'EUR' :: constants.currencytype),
  (200010, 0, 30, 'EUR' :: constants.currencytype),
  (200011, 0, 300, 'EUR' :: constants.currencytype),
  (200014, 30, 0, 'EUR' :: constants.currencytype),
  (200015, 300, 0, 'EUR' :: constants.currencytype);

INSERT INTO landlord.productaddonprice
  (id, productaddon_id, price_id, location, paymentmethods, customer_id)
VALUES
  (200000, 200000, 200000, 'DE' :: shared.customerlocation, ARRAY['STRIPE', 'INVOICE'] :: shared.paymentmethod[], null),
  (200001, 200001, 200001, 'DE' :: shared.customerlocation, ARRAY['STRIPE', 'INVOICE'] :: shared.paymentmethod[], null),
  (200002, 200002, 200002, 'DE' :: shared.customerlocation, ARRAY['STRIPE', 'INVOICE'] :: shared.paymentmethod[], null),
  (200003, 200003, 200003, 'DE' :: shared.customerlocation, ARRAY['STRIPE', 'INVOICE'] :: shared.paymentmethod[], null),
  (200004, 200004, 200004, 'DE' :: shared.customerlocation, ARRAY['STRIPE', 'INVOICE'] :: shared.paymentmethod[], null),
  (200005, 200005, 200005, 'DE' :: shared.customerlocation, ARRAY['STRIPE', 'INVOICE'] :: shared.paymentmethod[], null),
  (200006, 200006, 200006, 'DE' :: shared.customerlocation, ARRAY['STRIPE', 'INVOICE'] :: shared.paymentmethod[], null),
  (200007, 200007, 200007, 'DE' :: shared.customerlocation, ARRAY['STRIPE', 'INVOICE'] :: shared.paymentmethod[], null),
  (200008, 200008, 200008, 'DE' :: shared.customerlocation, ARRAY['STRIPE', 'INVOICE'] :: shared.paymentmethod[], null),
  (200009, 200009, 200009, 'DE' :: shared.customerlocation, ARRAY['STRIPE', 'INVOICE'] :: shared.paymentmethod[], null),
  (200010, 200010, 200010, 'DE' :: shared.customerlocation, ARRAY['STRIPE', 'INVOICE'] :: shared.paymentmethod[], null),
  (200011, 200011, 200011, 'DE' :: shared.customerlocation, ARRAY['STRIPE', 'INVOICE'] :: shared.paymentmethod[], null),
  (200014, 200014, 200014, 'DE' :: shared.customerlocation, ARRAY['STRIPE', 'INVOICE'] :: shared.paymentmethod[], null),
  (200015, 200015, 200015, 'DE' :: shared.customerlocation, ARRAY['STRIPE', 'INVOICE'] :: shared.paymentmethod[], null);

/*********** ADD ALL PRODUCT ADDONS TO CUSTOMER */
INSERT INTO landlord.customerproduct
(id, product_id, customer_id, duedate, renew, trial, created, updated)
VALUES
  (1000000, 100001, 1000000, '2018-06-29', false, true, now(), now());

INSERT INTO landlord.customeraddonproduct
(id, customerproduct_id, addonproduct_id, renew, created, updated, expirydate)
VALUES
  (1000000, 1000000, 200001, false, now(), now(), '2018-06-30'),
  (1000001, 1000000, 200003, false, now(), now(), '2018-06-30'),
  (1000002, 1000000, 200005, false, now(), now(), '2018-06-30'),
  (1000003, 1000000, 200007, false, now(), now(), '2018-06-30'),
  (1000004, 1000000, 200009, false, now(), now(), '2018-06-30'),
  (1000005, 1000000, 200011, false, now(), now(), '2018-06-30'),
  (1000006, 1000000, 200015, false, now(), now(), '2018-06-30');

/*********** limit to 5 agents */
INSERT INTO landlord.addonproduct_limitations
  (id, addonproduct_id, limitation, limitation_value)
VALUES
  (1000000, 200015, 'USER' :: landlord.productlimitation, 5);

/*********** propertyearcher.customer */
INSERT INTO propertysearcher.customer(id, paymentmethods, files, location, created, updated, paymentdetails, pricemultiplier)
VALUES
  (2000000,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000001,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000002,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000003,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000004,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000005,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000006,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000007,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000008,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000009,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000010,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000011,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000012,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000013,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000014,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000015,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000016,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000017,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000018,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000019,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5),
  (2000020,ARRAY ['DEFAULT'] :: shared.PAYMENTMETHOD [],'{}','DE',NOW(),NOW(),'{}',1.5);

/*********** propertyearcher.user */
INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000000,
  'alink+10@immomio.de',
  null,
  2000000,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Innenarchitekt",
          "income": 3500.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Christian",
      "name": "Goldmann",
      "phone": null,
      "gender": "MALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000001,
  'alink+11@immomio.de',
  null,
  2000001,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Innenarchitekt",
          "income": 3500.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Christian",
      "name": "Goldmann",
      "phone": null,
      "gender": "MALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000002,
  'alunt+12@immomio.de',
  null,
  2000002,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Steuerfachangestellter",
          "income": 2400.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Maurice",
      "name": "Gerdes",
      "phone": null,
      "gender": "MALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000003,
  'alink+13@immomio.de',
  null,
  2000003,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Winzerin",
          "income": 2200.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Nadine",
      "name": "Beck",
      "phone": null,
      "gender": "FEMALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000004,
  'alink+14@immomio.de',
  null,
  2000004,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Chirurgin",
          "income": 7300.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Henriette",
      "name": "Kooper",
      "phone": null,
      "gender": "FEMALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000005,
  'alunt+15@immomio.de',
  null,
  2000005,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Künstler",
          "income": 2900.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Raphael",
      "name": "Middendorff",
      "phone": null,
      "gender": "MALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000006,
  'alink+16@immomio.de',
  null,
  2000006,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Tiermedizinisch Fachangestellte",
          "income": 1500.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Sandra",
      "name": "Husmann",
      "phone": null,
      "gender": "FEMALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000007,
  'alink+17@immomio.de',
  null,
  2000007,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Unternehmensberater",
          "income": 4600.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Samuel",
      "name": "Kordes",
      "phone": null,
      "gender": "MALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000008,
  'alunt+18@immomio.de',
  null,
  2000008,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Werbetexter",
          "income": 3100.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Anton",
      "name": "List",
      "phone": null,
      "gender": "MALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000009,
  'alunt+19@immomio.de',
  null,
  2000009,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Einzelhandelskauffrau",
          "income": 1900.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Nele",
      "name": "Janßen",
      "phone": null,
      "gender": "FEMALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000010,
  'alink+20@immomio.de',
  null,
  2000010,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Sonderpädagogin",
          "income": 3000.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Maja",
      "name": "Meyer",
      "phone": null,
      "gender": "FEMALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000011,
  'alink+21@immomio.de',
  null,
  2000011,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Koch",
          "income": 2000.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Mohammed",
      "name": "Dreese",
      "phone": null,
      "gender": "MALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000012,
  'alink+22@immomio.de',
  null,
  2000012,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Professor",
          "income": 5100.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Harry",
      "name": "Von Kormann",
      "phone": null,
      "gender": "MALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000013,
  'alink+23@immomio.de',
  null,
  2000013,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Kunsthistorikerin",
          "income": 2600.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Annegret",
      "name": "Juncke",
      "phone": null,
      "gender": "FEMALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000014,
  'alink+24@immomio.de',
  null,
  2000014,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "IT-Systemelektroniker",
          "income": 2700.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Leander",
      "name": "Schappel",
      "phone": null,
      "gender": "MALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000015,
  'alunt+25@immomio.de',
  null,
  2000015,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Marketingmanagerin",
          "income": 3400.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Maria",
      "name": "Haue",
      "phone": null,
      "gender": "FEMALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000016,
  'alink+26@immomio.de',
  null,
  2000016,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Kellnerin",
          "income": 1600.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Sofia",
      "name": "Seibert",
      "phone": null,
      "gender": "FEMALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000017,
  'alink+27@immomio.de',
  null,
  2000017,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Erzieher",
          "income": 2200.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Noah",
      "name": "Gustav",
      "phone": null,
      "gender": "MALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000018,
  'alink+28@immomio.de',
  null,
  2000018,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Uhrmacher",
          "income": 2800.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Hugo",
      "name": "Weber",
      "phone": null,
      "gender": "MALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000019,
  'alunt+29@immomio.de',
  null,
  2000019,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Pilot",
          "income": 7000.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Simon",
      "name": "Behrnke",
      "phone": null,
      "gender": "MALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);

INSERT INTO propertysearcher."user" (id, email, password, customer_id, enabled, expired, locked, status, lastlogin, created, updated, profile, address, type)
VALUES (
  2000020,
  'alink+30@immomio.de',
  null,
  2000020,
  true,
  false,
  false,
  'REGISTERED',
  NOW(),
  NOW(),
  NOW(),
  '{
      "householdType": "COUPLE",
      "personalStatus": "MARRIED",
      "residents": 1,
      "dateOfBirth": null,
      "moveInDate": null,
      "profession": {
          "type": "EMPLOYED_UNLIMITED",
          "subType": "Bankangestellte",
          "income": 2500.0,
          "employmentDate": null
      },
      "creditScreening": {
          "available": false,
          "value": null
      },
      "law": {
          "noRentArrears": true,
          "noPoliceRecord": true,
          "noTenancyLawConflicts": true,
          "informationTrueAndComplete": true,
          "allowSchufa": true
      },
      "smoker": {
          "smoker": false,
          "inhouse": false
      },
      "additionalInformation": {
          "animals": false,
          "bailment": false,
          "music": false,
          "wbs": false
      },
      "firstname": "Isabel",
      "name": "Feist",
      "phone": null,
      "gender": "FEMALE",
      "title": null,
      "portrait": null
  }',
  '{}',
  'APPLICANT'
);
