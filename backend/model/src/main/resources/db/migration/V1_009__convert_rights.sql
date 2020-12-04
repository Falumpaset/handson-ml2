ALTER TABLE landlord.usertype_rights DROP CONSTRAINT groups_rights_pkey;

ALTER TABLE landlord.usertype_rights
    ADD CONSTRAINT usertype_rights_pkey PRIMARY KEY (id);

ALTER TABLE landlord.usertype_rights
    ADD CONSTRAINT uq_usertype_rights_01 UNIQUE (usertype, right_id);
    
    
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES 
    (231, '', 'Show-Prioset-Loader', 'show_prioset_loader', 'ui-view-flat');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (232, '', 'Flat-Edit-WriteProtection', 'flat_edit_writeProtection', 'flat');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (8, '', 'Blacklist-Show-all', 'blacklist_show_all', 'blacklist');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (9, '', 'Blacklist-Show', 'blacklist_show', 'blacklist');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (10, '', 'Blacklist-Create', 'blacklist_create', 'blacklist');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (11, '', 'Blacklist-Edit', 'blacklist_edit', 'blacklist');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (12, '', 'Blacklist-Delete', 'blacklist_delete', 'blacklist');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (99, '', 'Product-Category-Find-by-name', 'product_category_find_by_name', 'productcategory');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (100, '', 'Product-Show-all', 'product_show_all', 'product');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (13, '', 'Country-Show-all', 'country_show_all', 'country');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (14, '', 'Country-Show', 'country_show', 'country');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (15, '', 'Country-Create', 'country_create', 'country');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (16, '', 'Country-Edit', 'country_edit', 'country');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (17, '', 'Country-Delete', 'country_delete', 'country');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (18, '', 'Currency-Show-all', 'currency_show_all', 'currency');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (19, '', 'Currency-Show', 'currency_show', 'currency');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (20, '', 'Currency-Create', 'currency_create', 'currency');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (21, '', 'Currency-Edit', 'currency_edit', 'currency');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (22, '', 'Currency-Delete', 'currency_delete', 'currency');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (23, '', 'User-Show-all', 'user_show_all', 'user');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (24, '', 'User-Show', 'user_show', 'user');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (25, '', 'User-Create', 'user_create', 'user');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (26, '', 'User-Edit', 'user_edit', 'user');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (27, '', 'User-Delete', 'user_delete', 'user');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (28, '', 'User-Change-Email', 'user_change_email', 'user');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (29, '', 'User-Confirm-Change-Email', 'user_confirm_change_email', 'user');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (30, '', 'User-Reset-Password', 'user_reset_password', 'user');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (31, '', 'User-Register', 'user_register', 'user');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (32, '', 'User-Confirm-Account', 'user_confirm_account', 'user');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (33, '', 'User-find-By-Email', 'user_find_by_email', 'user');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (34, '', 'User-find-Multiple-By-Email', 'user_find_multiple_by_email', 'user');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (35, '', 'User-find-By-User-Profile-Id', 'user_find_by_user_profile_id', 'user');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (36, '', 'User-find-User-Count-By-Customer', 'user_find_user_count_by_customer', 'user');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (37, '', 'User-find-By-Group', 'user_find_by_group', 'user');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (38, '', 'User-find-User-By-Customer-Id-And-Group-Id', 'user_find_by_customer_and_group_id', 'user');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (39, '', 'User-Profile-Show-all', 'user_profile_show_all', 'userprofile');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (40, '', 'User-Profile-Show', 'user_profile_show', 'userprofile');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (41, '', 'User-Profile-Create', 'user_profile_create', 'userprofile');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (42, '', 'User-Profile-Edit', 'user_profile_edit', 'userprofile');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (43, '', 'User-Profile-Delete', 'user_profile_delete', 'userprofile');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (44, '', 'User-Profile-find_by_Email', 'user_profile_find_by_email', 'userprofile');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (45, '', 'Group-Show-all', 'group_show_all', 'group');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (46, '', 'Group-Show', 'group_show', 'group');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (47, '', 'Group-Create', 'group_create', 'group');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (48, '', 'Group-Edit', 'group_edit', 'group');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (49, '', 'Group-Delete', 'group_delete', 'group');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (50, '', 'Rights-Show-all', 'rights_show_all', 'rights');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (51, '', 'Rights-Show', 'rights_show', 'rights');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (52, '', 'Rights-Create', 'rights_create', 'rights');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (53, '', 'Rights-Edit', 'rights_edit', 'rights');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (54, '', 'Rights-Delete', 'rights_show', 'rights');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (55, '', 'Token-Show-all', 'token_show_all', 'token');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (56, '', 'Token-Show', 'token_show', 'token');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (57, '', 'Token-Create', 'token_create', 'token');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (58, '', 'Token-Edit', 'token_edit', 'token');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (59, '', 'Token-Delete', 'token_delete', 'token');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (182, '', 'Tenant-Summary-Overview', 'tenant_summary_overview', 'ui-view-tenant-summary');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (183, '', 'Tenant-Summary-News', 'tenant_summary_news', 'ui-view-tenant-summary');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (184, '', 'Tenant-Summary-Solvency', 'tenant_summary_solvency', 'ui-view-tenant-summary');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (185, '', 'Tenant-Summary-Documents', 'tenant_summary_documents', 'ui-view-tenant-summary');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (186, '', 'Tenant-Summary-Comments', 'tenant_summary_comments', 'ui-view-tenant-summary');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (187, '', 'Tenant-Summary-Short-Icons', 'tenant_summary_short_icons', 'ui-view-tenant-summary');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (1, '', 'Is-Tenant', 'is_tenant', 'base');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (60, '', 'Token-find-by-customer-id-and-key-and-token', 'token_find_by_customer_id_and_key_and_token', 'token');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (61, '', 'Token-find-by-key-and-token', 'token_find_by_key_and_token', 'token');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (62, '', 'Token-find-by-key', 'token_find_by_token', 'token');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (63, '', 'Token-get-by-key', 'token_get_by_key', 'token');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (64, '', 'Customer-Show-all', 'customer_show_all', 'country');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (65, '', 'Customer-Show', 'customer_show', 'country');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (66, '', 'Customer-Create', 'customer_create', 'country');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (67, '', 'Customer-Edit', 'customer_edit', 'country');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (68, '', 'Customer-Delete', 'customer_delete', 'country');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (69, '', 'Customer-Preferences-Show-all', 'customer_preferences_show_all', 'customerpreferences');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (70, '', 'Customer-Show', 'customer_preferences_show', 'customerpreferences');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (71, '', 'Customer-Create', 'customer_preferences_create', 'customerpreferences');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (72, '', 'Customer-Edit', 'customer_preferences_edit', 'customerpreferences');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (73, '', 'Customer-Delete', 'customer_preferences_delete', 'customerpreferences');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (74, '', 'Payment-Show-all', 'payment_show_all', 'payment');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (75, '', 'Payment-Show', 'payment_show', 'payment');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (76, '', 'Payment-Create', 'payment_create', 'payment');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (77, '', 'Payment-Edit', 'payment_edit', 'payment');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (78, '', 'Payment-Delete', 'payment_delete', 'payment');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (79, '', 'Credential-Show-all', 'credential_show_all', 'credential');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (80, '', 'Credential-Show', 'credential_show', 'credential');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (81, '', 'Credential-Create', 'credential_create', 'credential');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (82, '', 'Credential-Edit', 'credential_edit', 'credential');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (83, '', 'Credential-Delete', 'credential_delete', 'credential');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (84, '', 'Invoice-Show-all', 'invoice_show_all', 'invoice');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (85, '', 'Invoice-Show', 'invoice_show', 'invoice');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (86, '', 'Invoice-Create', 'invoice_create', 'invoice');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (87, '', 'Invoice-Edit', 'invoice_edit', 'invoice');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (88, '', 'Invoice-Delete', 'invoice_delete', 'invoice');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (89, '', 'Product-Basket-Show-all', 'product_basket_show_all', 'productbasket');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (90, '', 'Product-Basket-Show', 'product_basket_show', 'productbasket');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (91, '', 'Product-Basket-Create', 'product_basket_create', 'productbasket');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (92, '', 'Product-Basket-Edit', 'product_basket_edit', 'productbasket');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (93, '', 'Product-Basket-Delete', 'product_basket_delete', 'productbasket');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (94, '', 'Product-Category-Show-all', 'product_category_show_all', 'productcategory');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (95, '', 'Product-Category-Show', 'product_category_show', 'productcategory');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (96, '', 'Product-Category-Create', 'product_category_create', 'productcategory');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (97, '', 'Product-Category-Edit', 'product_category_edit', 'productcategory');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (98, '', 'Product-Category-Delete', 'product_category_delete', 'productcategory');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (101, '', 'Product-Show', 'product_show', 'product');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (102, '', 'Product-Create', 'product_create', 'product');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (103, '', 'Product-Edit', 'product_edit', 'product');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (104, '', 'Product-Delete', 'product_delete', 'product');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (105, '', 'Product-Find-by-Product-Category-id', 'product_find_by_product_category_id', 'product');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (106, '', 'Product-Find-by-Product-Category-name', 'product_find_by_product_category_name', 'product');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (107, '', 'Product-Find-by-Prices-Period', 'product_find_by_prices_period', 'product');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (108, '', 'Price-Show-all', 'price_show_all', 'price');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (109, '', 'Price-Show', 'price_show', 'price');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (110, '', 'Price-Create', 'price_create', 'price');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (111, '', 'Price-Edit', 'price_edit', 'price');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (112, '', 'Price-Delete', 'price_delete', 'price');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (113, '', 'Specification-Show-all', 'specification_show_all', 'specification');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (114, '', 'Specification-Show', 'specification_show', 'specification');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (115, '', 'Specification-Create', 'specification_create', 'specification');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (116, '', 'Specification-Edit', 'specification_edit', 'specification');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (117, '', 'Specification-Delete', 'specification_delete', 'specification');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (118, '', 'Limitation-Show-all', 'limitation_show_all', 'limitation');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (119, '', 'Limitation-Show', 'limitation_show', 'limitation');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (120, '', 'Limitation-Create', 'limitation_create', 'limitation');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (121, '', 'Limitation-Edit', 'limitation_edit', 'limitation');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (122, '', 'Limitation-Delete', 'limitation_delete', 'limitation');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (123, '', 'Discount-Show-all', 'discount_show_all', 'discount');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (124, '', 'Discount-Show', 'discount_show', 'discount');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (125, '', 'Discount-Create', 'discount_create', 'discount');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (126, '', 'Discount-Edit', 'discount_edit', 'discount');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (127, '', 'Discount-Delete', 'discount_delete', 'discount');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (128, '', 'Flat-Show-all', 'flat_show_all', 'flat');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (129, '', 'Flat-Show', 'flat_show', 'flat');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (130, '', 'Flat-Create', 'flat_create', 'flat');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (131, '', 'Flat-Edit', 'flat_edit', 'flat');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (132, '', 'Flat-Delete', 'flat_delete', 'flat');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (140, '', 'Flat-find-by-Customer-id', 'flat_find_by_customer_id', 'flat');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (141, '', 'Flat-find-by-State-and-Preference-Prioset', 'flat_find_by_state_and_preference_prioset', 'flat');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (142, '', 'Flat-find-by-Keys-and-Values', 'flat_find_by_keys_and_values', 'flat');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (143, '', 'Flat-Application-Show-all', 'flat_application_show_all', 'flatapplication');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (144, '', 'Flat-Application-Show', 'flat_application_show', 'flatapplication');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (145, '', 'Flat-Application-Create', 'flat_application_create', 'flatapplication');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (146, '', 'Flat-Application-Edit', 'flat_application_edit', 'flatapplication');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (147, '', 'Flat-Application-Delete', 'flat_application_delete', 'flatapplication');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (148, '', 'Flat-Application-find-all-by-Flat-Invitation-id', 'flat_application_find_all_by_flat_invitation_id', 'flatapplication');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (149, '', 'Flat-Application-find-all-by-Flat-active-and-applied', 'flat_application_find_all_by_flat_active_and_applied', 'flatapplication');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (150, '', 'Flat-Application-find-all-by-Flat-and-sort-custom', 'flat_application_find_all_by_flat_and_sort_custom', 'flatapplication');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (151, '', 'Flat-Application-find-by-Keys-and-Values', 'flat_application_find_by_keys_and_values', 'flatapplication');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (152, '', 'Flat-Invitation-Show-all', 'flat_invitation_show_all', 'flatinvitation');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (153, '', 'Flat-Invitation-Show', 'flat_invitation_show', 'flatinvitation');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (154, '', 'Flat-Invitation-Create', 'flat_invitation_create', 'flatinvitation');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (155, '', 'Flat-Invitation-Edit', 'flat_invitation_edit', 'flatinvitation');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (156, '', 'Flat-Invitation-Delete', 'flat_invitation_delete', 'flatinvitation');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (157, '', 'Flat-Invitation-find-by-Userprofile-id', 'flat_invitation_find_by_userprofile_id', 'flatinvitation');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (158, '', 'Flat-Invitation-find-by-Userprofile-id-and-canceled', 'flat_invitation_find_by_userprofile_id_and_canceled', 'flatinvitation');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (159, '', 'Flat-Invitation-find-by-Userprofile-id-and-Flat-id-and-canceled', 'flat_invitation_find_by_userprofile_id_and_flat_id_and_canceled', 'flatinvitation');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (160, '', 'Flat-Invitation-find-by-canceled-and-date-after-and-date-before-and-flat', 'flat_invitation_find_by_canceled_and_date_after_and_date_before_and_flat', 'flatinvitation');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (161, '', 'Menu-Item-Abo', 'menu_item_abo', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (162, '', 'Menu-Item-Admin-Dashboard', 'menu_item_admin_dashboard', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (163, '', 'Menu-Item-Agents', 'menu_item_agents', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (164, '', 'Menu-Item-Applications', 'menu_item_applications', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (165, '', 'Menu-Item-Branding', 'menu_item_branding', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (166, '', 'Menu-Item-Email-Editor', 'menu_item_email_editor', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (167, '', 'Menu-Item-create-Tenants', 'menu_item_create_tenants', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (168, '', 'Menu-Item-Dashboard', 'menu_item_dashboard', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (169, '', 'Menu-Item-Email-change', 'menu_item_email_change', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (170, '', 'Menu-Item-Flats', 'menu_item_flats', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (171, '', 'Menu-Item-Invitations', 'menu_item_invitations', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (172, '', 'Menu-Item-Invitations-Owner', 'menu_item_invitations_owner', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (173, '', 'Menu-Item-Orders', 'menu_item_orders', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (174, '', 'Menu-Item-Password-change', 'menu_item_password_change', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (175, '', 'Menu-Item-Personal', 'menu_item_personal', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (176, '', 'Menu-Item-Personal-Commercial', 'menu_item_personal_commercial', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (177, '', 'Menu-Item-Personal-Group', 'menu_item_personal_group', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (178, '', 'Menu-Item-Portals', 'menu_item_portals', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (179, '', 'Menu-Item-Priosets', 'menu_item_priosets', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (180, '', 'Menu-Item-Search-Tenants', 'menu_item_search_tenants', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (181, '', 'Menu-Item-Abo', 'menu_item_abo', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (136, '', 'preference-prioset-edit', 'preference_prioset_edit', 'preferenceprioset');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (138, '', 'Preference-Prioset-find-by-Customer-Id', 'preference_prioset_find_by_customer_id', 'preferenceprioset');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (134, '', 'preference-prioset-show', 'preference_prioset_show', 'preferenceprioset');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (137, '', 'preference-prioset-delete', 'preference_prioset_delete', 'preferenceprioset');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (139, '', 'Preference-Prioset-find-by-Name', 'preference_prioset_find_by_name', 'preferenceprioset');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (2, '', 'Is-Owner', 'is_owner', 'base');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (3, '', 'Is-Admin', 'is_admin', 'base');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (4, '', 'Is-EMPLOYEE', 'is_EMPLOYEE', 'base');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (5, '', 'Is-Commercial', 'is_commercial', 'base');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (6, '', 'Is-HOTLINE', 'is_HOTLINE', 'base');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (7, '', 'Is-Starter', 'is_starter', 'base');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (190, '', 'Ftp-Access-Show-All', 'ftp_access_show_all', 'ftpaccess');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (191, '', 'Ftp-Access-Show', 'ftp_access_show_all', 'ftpaccess');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (192, '', 'Ftp-Access-Create', 'ftp_access_show_create', 'ftpaccess');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (193, '', 'Ftp-Access-Edit', 'ftp_access_show_edit', 'ftpaccess');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (194, '', 'Ftp-Access-Delete', 'ftp_access_show_delete', 'ftpaccess');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (195, '', 'Ftp-Import-Log-Show-all', 'ftp_import_log_show_all', 'ftpimportlog');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (196, '', 'Ftp-Import-Log-Show', 'ftp_import_log_show', 'ftpimportlog');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (197, '', 'Email-Editor-Show', 'email_editor_show', 'emaileditor');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (198, '', 'Email-Editor-Create', 'email_editor_create', 'emaileditor');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (199, '', 'Email-Editor-Edit', 'email_editor_edit', 'emaileditor');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (200, '', 'Email-Editor-Delete', 'email_editor_delete', 'emaileditor');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (201, '', 'Branding-Show', 'branding_show', 'branding');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (202, '', 'Branding-Create', 'branding_create', 'branding');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (203, '', 'Branding-Edit', 'branding_edit', 'branding');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (204, '', 'Branding-Delete', 'branding_delete', 'branding');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (205, '', 'Menu-Item-Ftp-Import-Log', 'menu_item_ftp_import_log', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (206, '', 'Menu-Item-Ftp-Import-Access', 'menu_item_ftp_import_access', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (133, '', 'preference-prioset-show-all', 'preference_prioset_show_all', 'preferenceprioset');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (135, '', 'preference-prioset-create', 'preference_prioset_create', 'preferenceprioset');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (210, '', 'Product-Change-Abo', 'product_change_abo', 'product');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (220, '', 'Portal-Flat-Activate', 'portal_flat_activate', 'portal2');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (221, '', 'Portal-Flat-Deactivate', 'portal_flat_deactivate', 'portal2');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (222, '', 'Portal-Flat-Update', 'portal_flat_update', 'portal2');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (223, '', 'Portal-Flat-Delete', 'portal_flat_delete', 'portal2');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (207, '', 'Flat-Summary-Expose', 'flat_summary_expose', 'ui-view-flat-summary');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (208, '', 'Menu-Item-Extra-Services', 'menu_item_extra_services', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (230, '', 'Menu-Item-Customerfiles', 'menu_item_customerfiles', 'ui-menu');
INSERT INTO shared."right" (id, description, name, shortcode, "group") VALUES
    (233, '', 'Flat-Set-As-Tenant', 'flat_set_as_tenant', 'flat');
    
INSERT INTO landlord.right (id, right_id) VALUES 
(129,129);
INSERT INTO landlord.right (id, right_id) VALUES
(106,106);
INSERT INTO landlord.right (id, right_id) VALUES
(220,220);
INSERT INTO landlord.right (id, right_id) VALUES
(156,156);
INSERT INTO landlord.right (id, right_id) VALUES
(138,138);
INSERT INTO landlord.right (id, right_id) VALUES
(80,80);
INSERT INTO landlord.right (id, right_id) VALUES
(135,135);
INSERT INTO landlord.right (id, right_id) VALUES
(179,179);
INSERT INTO landlord.right (id, right_id) VALUES
(132,132);
INSERT INTO landlord.right (id, right_id) VALUES
(131,131);
INSERT INTO landlord.right (id, right_id) VALUES
(148,148);
INSERT INTO landlord.right (id, right_id) VALUES
(176,176);
INSERT INTO landlord.right (id, right_id) VALUES
(208,208);
INSERT INTO landlord.right (id, right_id) VALUES
(96,96);
INSERT INTO landlord.right (id, right_id) VALUES
(175,175);
INSERT INTO landlord.right (id, right_id) VALUES
(67,67);
INSERT INTO landlord.right (id, right_id) VALUES
(178,178);
INSERT INTO landlord.right (id, right_id) VALUES
(169,169);
INSERT INTO landlord.right (id, right_id) VALUES
(14,14);
INSERT INTO landlord.right (id, right_id) VALUES
(99,99);
INSERT INTO landlord.right (id, right_id) VALUES
(28,28);
INSERT INTO landlord.right (id, right_id) VALUES
(83,83);
INSERT INTO landlord.right (id, right_id) VALUES
(94,94);
INSERT INTO landlord.right (id, right_id) VALUES
(174,174);
INSERT INTO landlord.right (id, right_id) VALUES
(142,142);
INSERT INTO landlord.right (id, right_id) VALUES
(151,151);
INSERT INTO landlord.right (id, right_id) VALUES
(77,77);
INSERT INTO landlord.right (id, right_id) VALUES
(140,140);
INSERT INTO landlord.right (id, right_id) VALUES
(221,221);
INSERT INTO landlord.right (id, right_id) VALUES
(4,4);
INSERT INTO landlord.right (id, right_id) VALUES
(30,30);
INSERT INTO landlord.right (id, right_id) VALUES
(160,160);
INSERT INTO landlord.right (id, right_id) VALUES
(119,119);
INSERT INTO landlord.right (id, right_id) VALUES
(153,153);
INSERT INTO landlord.right (id, right_id) VALUES
(95,95);
INSERT INTO landlord.right (id, right_id) VALUES
(210,210);
INSERT INTO landlord.right (id, right_id) VALUES
(40,40);
INSERT INTO landlord.right (id, right_id) VALUES
(182,182);
INSERT INTO landlord.right (id, right_id) VALUES
(222,222);
INSERT INTO landlord.right (id, right_id) VALUES
(19,19);
INSERT INTO landlord.right (id, right_id) VALUES
(157,157);
INSERT INTO landlord.right (id, right_id) VALUES
(108,108);
INSERT INTO landlord.right (id, right_id) VALUES
(154,154);
INSERT INTO landlord.right (id, right_id) VALUES
(109,109);
INSERT INTO landlord.right (id, right_id) VALUES
(180,180);
INSERT INTO landlord.right (id, right_id) VALUES
(92,92);
INSERT INTO landlord.right (id, right_id) VALUES
(31,31);
INSERT INTO landlord.right (id, right_id) VALUES
(65,65);
INSERT INTO landlord.right (id, right_id) VALUES
(101,101);
INSERT INTO landlord.right (id, right_id) VALUES
(76,76);
INSERT INTO landlord.right (id, right_id) VALUES
(44,44);
INSERT INTO landlord.right (id, right_id) VALUES
(173,173);
INSERT INTO landlord.right (id, right_id) VALUES
(85,85);
INSERT INTO landlord.right (id, right_id) VALUES
(82,82);
INSERT INTO landlord.right (id, right_id) VALUES
(81,81);
INSERT INTO landlord.right (id, right_id) VALUES
(32,32);
INSERT INTO landlord.right (id, right_id) VALUES
(25,25);
INSERT INTO landlord.right (id, right_id) VALUES
(26,26);
INSERT INTO landlord.right (id, right_id) VALUES
(42,42);
INSERT INTO landlord.right (id, right_id) VALUES
(137,137);
INSERT INTO landlord.right (id, right_id) VALUES
(90,90);
INSERT INTO landlord.right (id, right_id) VALUES
(134,134);
INSERT INTO landlord.right (id, right_id) VALUES
(18,18);
INSERT INTO landlord.right (id, right_id) VALUES
(139,139);
INSERT INTO landlord.right (id, right_id) VALUES
(146,146);
INSERT INTO landlord.right (id, right_id) VALUES
(78,78);
INSERT INTO landlord.right (id, right_id) VALUES
(185,185);
INSERT INTO landlord.right (id, right_id) VALUES
(233,233);
INSERT INTO landlord.right (id, right_id) VALUES
(150,150);
INSERT INTO landlord.right (id, right_id) VALUES
(168,168);
INSERT INTO landlord.right (id, right_id) VALUES
(136,136);
INSERT INTO landlord.right (id, right_id) VALUES
(167,167);
INSERT INTO landlord.right (id, right_id) VALUES
(158,158);
INSERT INTO landlord.right (id, right_id) VALUES
(13,13);
INSERT INTO landlord.right (id, right_id) VALUES
(172,172);
INSERT INTO landlord.right (id, right_id) VALUES
(183,183);
INSERT INTO landlord.right (id, right_id) VALUES
(24,24);
INSERT INTO landlord.right (id, right_id) VALUES
(184,184);
INSERT INTO landlord.right (id, right_id) VALUES
(91,91);
INSERT INTO landlord.right (id, right_id) VALUES
(170,170);
INSERT INTO landlord.right (id, right_id) VALUES
(70,70);
INSERT INTO landlord.right (id, right_id) VALUES
(27,27);
INSERT INTO landlord.right (id, right_id) VALUES
(145,145);
INSERT INTO landlord.right (id, right_id) VALUES
(230,230);
INSERT INTO landlord.right (id, right_id) VALUES
(118,118);
INSERT INTO landlord.right (id, right_id) VALUES
(130,130);
INSERT INTO landlord.right (id, right_id) VALUES
(223,223);
INSERT INTO landlord.right (id, right_id) VALUES
(207,207);
INSERT INTO landlord.right (id, right_id) VALUES
(155,155);
INSERT INTO landlord.right (id, right_id) VALUES
(231,231);
INSERT INTO landlord.right (id, right_id) VALUES
(163,163);
INSERT INTO landlord.right (id, right_id) VALUES
(6,6);
INSERT INTO landlord.right (id, right_id) VALUES
(165,165);
INSERT INTO landlord.right (id, right_id) VALUES
(149,149);
INSERT INTO landlord.right (id, right_id) VALUES
(29,29);
INSERT INTO landlord.right (id, right_id) VALUES
(105,105);
INSERT INTO landlord.right (id, right_id) VALUES
(159,159);
INSERT INTO landlord.right (id, right_id) VALUES
(2,2);
INSERT INTO landlord.right (id, right_id) VALUES
(72,72);
INSERT INTO landlord.right (id, right_id) VALUES
(97,97);
INSERT INTO landlord.right (id, right_id) VALUES
(186,186);
INSERT INTO landlord.right (id, right_id) VALUES
(75,75);
INSERT INTO landlord.right (id, right_id) VALUES
(177,177);
INSERT INTO landlord.right (id, right_id) VALUES
(124,124);
INSERT INTO landlord.right (id, right_id) VALUES
(141,141);
INSERT INTO landlord.right (id, right_id) VALUES
(5,5);
INSERT INTO landlord.right (id, right_id) VALUES
(144,144);
INSERT INTO landlord.right (id, right_id) VALUES
(187,187);
INSERT INTO landlord.right (id, right_id) VALUES
(114,114);

ALTER TABLE propertysearcher."right"
    ADD COLUMN right_id bigint NOT NULL;
    
INSERT INTO propertysearcher.right (id, right_id) VALUES    
(129,129);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(173,173);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(85,85);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(82,82);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(106,106);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(171,171);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(81,81);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(25,25);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(32,32);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(1,1);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(26,26);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(164,164);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(42,42);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(80,80);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(90,90);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(18,18);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(146,146);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(78,78);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(185,185);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(168,168);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(13,13);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(175,175);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(183,183);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(67,67);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(24,24);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(169,169);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(184,184);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(91,91);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(70,70);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(14,14);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(99,99);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(28,28);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(83,83);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(94,94);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(174,174);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(145,145);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(142,142);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(151,151);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(77,77);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(207,207);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(147,147);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(155,155);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(30,30);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(119,119);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(153,153);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(95,95);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(40,40);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(182,182);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(19,19);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(157,157);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(29,29);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(105,105);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(109,109);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(72,72);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(92,92);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(31,31);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(65,65);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(75,75);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(177,177);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(76,76);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(101,101);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(124,124);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(144,144);
INSERT INTO propertysearcher.right (id, right_id) VALUES   
(114,114);

INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (1,'COMPANYADMIN',2);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (2,'COMPANYADMIN',4);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (3,'COMPANYADMIN',5);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (4,'COMPANYADMIN',6);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (5,'COMPANYADMIN',13);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (6,'COMPANYADMIN',14);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (7,'COMPANYADMIN',18);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (8,'COMPANYADMIN',19);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (9,'COMPANYADMIN',24);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (10,'COMPANYADMIN',25);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (11,'COMPANYADMIN',26);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (12,'COMPANYADMIN',27);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (13,'COMPANYADMIN',28);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (14,'COMPANYADMIN',29);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (15,'COMPANYADMIN',30);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (16,'COMPANYADMIN',31);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (17,'COMPANYADMIN',32);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (18,'COMPANYADMIN',40);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (19,'COMPANYADMIN',42);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (20,'COMPANYADMIN',44);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (21,'COMPANYADMIN',65);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (22,'COMPANYADMIN',67);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (23,'COMPANYADMIN',70);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (24,'COMPANYADMIN',72);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (25,'COMPANYADMIN',75);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (26,'COMPANYADMIN',76);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (27,'COMPANYADMIN',77);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (28,'COMPANYADMIN',78);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (29,'COMPANYADMIN',80);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (30,'COMPANYADMIN',81);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (31,'COMPANYADMIN',82);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (32,'COMPANYADMIN',83);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (33,'COMPANYADMIN',85);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (34,'COMPANYADMIN',90);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (35,'COMPANYADMIN',91);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (36,'COMPANYADMIN',92);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (37,'COMPANYADMIN',94);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (38,'COMPANYADMIN',95);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (39,'COMPANYADMIN',96);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (40 ,'COMPANYADMIN',97);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (41 ,'COMPANYADMIN',99);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (42 ,'COMPANYADMIN',101);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (43 ,'COMPANYADMIN',105);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (44 ,'COMPANYADMIN',106);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (45 ,'COMPANYADMIN',108);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (46 ,'COMPANYADMIN',109);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (47 ,'COMPANYADMIN',114);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (48 ,'COMPANYADMIN',118);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (49 ,'COMPANYADMIN',119);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (50 ,'COMPANYADMIN',124);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (51 ,'COMPANYADMIN',129);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (52 ,'COMPANYADMIN',130);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (53 ,'COMPANYADMIN',131);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (54 ,'COMPANYADMIN',132);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (55 ,'COMPANYADMIN',134);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (56 ,'COMPANYADMIN',135);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (57 ,'COMPANYADMIN',136);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (58 ,'COMPANYADMIN',137);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (59 ,'COMPANYADMIN',138);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (60 ,'COMPANYADMIN',139);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (61 ,'COMPANYADMIN',140);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (62 ,'COMPANYADMIN',141);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (63 ,'COMPANYADMIN',142);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (64 ,'COMPANYADMIN',144);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (65 ,'COMPANYADMIN',145);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (66 ,'COMPANYADMIN',146);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (67 ,'COMPANYADMIN',148);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (68 ,'COMPANYADMIN',149);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (69 ,'COMPANYADMIN',150);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (70 ,'COMPANYADMIN',151);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (71 ,'COMPANYADMIN',153);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (72 ,'COMPANYADMIN',154);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (73 ,'COMPANYADMIN',155);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (74 ,'COMPANYADMIN',156);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (75 ,'COMPANYADMIN',157);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (76 ,'COMPANYADMIN',158);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (77 ,'COMPANYADMIN',159);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (78 ,'COMPANYADMIN',160);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (79 ,'COMPANYADMIN',163);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (80 ,'COMPANYADMIN',165);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (81 ,'COMPANYADMIN',167);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (82 ,'COMPANYADMIN',168);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (83 ,'COMPANYADMIN',169);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (84 ,'COMPANYADMIN',170);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (85 ,'COMPANYADMIN',172);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (86 ,'COMPANYADMIN',173);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (87 ,'COMPANYADMIN',174);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (88 ,'COMPANYADMIN',175);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (89 ,'COMPANYADMIN',176);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (90 ,'COMPANYADMIN',177);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (91 ,'COMPANYADMIN',178);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (92 ,'COMPANYADMIN',179);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (93 ,'COMPANYADMIN',180);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (94 ,'COMPANYADMIN',182);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (95 ,'COMPANYADMIN',183);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (96 ,'COMPANYADMIN',184);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (97 ,'COMPANYADMIN',185);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (98 ,'COMPANYADMIN',186);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (99 ,'COMPANYADMIN',187);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (100,'COMPANYADMIN',207);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (101,'COMPANYADMIN',208);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (102,'COMPANYADMIN',210);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (103,'COMPANYADMIN',220);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (104,'COMPANYADMIN',221);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (105,'COMPANYADMIN',222);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (106,'COMPANYADMIN',223);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (107,'COMPANYADMIN',230);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (108,'COMPANYADMIN',231);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES
    (109,'COMPANYADMIN',233);
    
    
    
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (194,'EMPLOYEE',4);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (195,'EMPLOYEE',13);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (196,'EMPLOYEE',14);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (197,'EMPLOYEE',18);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (198,'EMPLOYEE',19);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (199,'EMPLOYEE',24);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (200,'EMPLOYEE',26);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (201,'EMPLOYEE',28);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (202,'EMPLOYEE',29);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (203,'EMPLOYEE',30);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (204,'EMPLOYEE',32);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (205,'EMPLOYEE',40);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (206,'EMPLOYEE',42);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (207,'EMPLOYEE',44);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (208,'EMPLOYEE',90);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (209,'EMPLOYEE',91);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (210,'EMPLOYEE',94);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (211,'EMPLOYEE',95);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (212,'EMPLOYEE',99);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (213,'EMPLOYEE',101);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (214,'EMPLOYEE',105);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (215,'EMPLOYEE',106);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (216,'EMPLOYEE',109);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (217,'EMPLOYEE',114);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (218,'EMPLOYEE',119);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (219,'EMPLOYEE',124);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (220,'EMPLOYEE',129);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (221,'EMPLOYEE',130);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (222,'EMPLOYEE',131);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (223,'EMPLOYEE',132);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (224,'EMPLOYEE',134);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (225,'EMPLOYEE',135);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (226,'EMPLOYEE',136);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (227,'EMPLOYEE',137);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (228,'EMPLOYEE',138);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (229,'EMPLOYEE',139);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (230,'EMPLOYEE',140);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (231,'EMPLOYEE',141);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (232,'EMPLOYEE',142);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (233,'EMPLOYEE',144);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (234,'EMPLOYEE',145);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (235,'EMPLOYEE',146);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (236,'EMPLOYEE',148);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (237,'EMPLOYEE',149);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (238,'EMPLOYEE',150);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (239,'EMPLOYEE',151);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (240,'EMPLOYEE',153);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (241,'EMPLOYEE',154);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (242,'EMPLOYEE',155);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (243,'EMPLOYEE',156);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (244,'EMPLOYEE',157);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (245,'EMPLOYEE',158);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (246,'EMPLOYEE',159);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (247,'EMPLOYEE',160);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (248,'EMPLOYEE',168);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (249,'EMPLOYEE',169);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (250,'EMPLOYEE',170);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (251,'EMPLOYEE',172);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (252,'EMPLOYEE',174);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (253,'EMPLOYEE',175);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (254,'EMPLOYEE',177);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (255,'EMPLOYEE',180);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (256,'EMPLOYEE',182);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (257,'EMPLOYEE',183);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (258,'EMPLOYEE',184);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (259,'EMPLOYEE',185);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (260,'EMPLOYEE',186);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (261,'EMPLOYEE',187);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (262,'EMPLOYEE',207);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (263,'EMPLOYEE',220);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (264,'EMPLOYEE',221);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (265,'EMPLOYEE',222);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (266,'EMPLOYEE',223);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (267,'EMPLOYEE',231);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (268,'EMPLOYEE',233);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (269,'HOTLINE',6);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (270,'HOTLINE',13);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (271,'HOTLINE',14);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (272,'HOTLINE',18);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (273,'HOTLINE',19);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (274,'HOTLINE',24);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (275,'HOTLINE',26);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (276,'HOTLINE',28);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (277,'HOTLINE',29);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (278,'HOTLINE',30);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (279,'HOTLINE',32);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (280,'HOTLINE',40);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (281,'HOTLINE',42);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (282,'HOTLINE',44);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (283,'HOTLINE',99);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (284,'HOTLINE',105);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (285,'HOTLINE',106);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (286,'HOTLINE',129);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (287,'HOTLINE',134);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (288,'HOTLINE',138);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (289,'HOTLINE',139);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (290,'HOTLINE',140);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (291,'HOTLINE',141);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (292,'HOTLINE',142);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (293,'HOTLINE',144);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (294,'HOTLINE',145);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (295,'HOTLINE',146);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (296,'HOTLINE',148);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (297,'HOTLINE',149);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (298,'HOTLINE',150);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (299,'HOTLINE',151);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (300,'HOTLINE',153);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (301,'HOTLINE',154);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (302,'HOTLINE',155);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (303,'HOTLINE',156);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (304,'HOTLINE',157);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (305,'HOTLINE',158);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (306,'HOTLINE',159);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (307,'HOTLINE',160);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (308,'HOTLINE',167);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (309,'HOTLINE',168);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (310,'HOTLINE',169);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (311,'HOTLINE',170);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (312,'HOTLINE',172);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (313,'HOTLINE',174);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (314,'HOTLINE',175);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (315,'HOTLINE',177);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (316,'HOTLINE',180);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (317,'HOTLINE',182);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (318,'HOTLINE',183);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (319,'HOTLINE',184);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (320,'HOTLINE',185);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (321,'HOTLINE',186);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (322,'HOTLINE',187);
INSERT INTO landlord.usertype_rights (id, usertype, right_id) VALUES 
    (323,'HOTLINE',207);
    