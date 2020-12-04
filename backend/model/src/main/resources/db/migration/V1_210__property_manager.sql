alter table landlord.property
    add column property_manager_id bigint;
alter table landlord.property
    add constraint fk_property_manager foreign key (property_manager_id) references landlord."user" (id) match simple on update cascade on delete set null;

delete from shared."right" where id < 1000;

UPDATE landlord.permissionscheme_rights SET id = 611 WHERE permission_scheme_id = 600 AND right_id = 206 AND usertype = 'COMPANYADMIN';
UPDATE landlord.permissionscheme_rights SET id = 612 WHERE permission_scheme_id = 200 AND right_id = 2001 AND usertype = 'COMPANYADMIN';
create unique index uq_permission_scheme_rights on  landlord.permissionscheme_rights (id);

insert into shared."right" values (2500 ,'','Menu-Item-Dashboard', 'menu_item_dashboard', 'ui-menu', now(), now());
insert into shared."right" values (2501 ,'','Menu-Item-Objects', 'menu_item_objects', 'ui-menu', now(), now());
insert into shared."right" values (2502 ,'','Menu-Item-Viewings', 'menu_item_viewings', 'ui-menu', now(), now());
insert into shared."right" values (2503 ,'','Menu-Item-Contracts', 'menu_item_contracts', 'ui-menu', now(), now());
insert into shared."right" values (2504 ,'','Menu-Item-Reporting', 'menu_item_reporting', 'ui-menu', now(), now());
insert into shared."right" values (2505 ,'','Menu-Item-Schufa', 'menu_item_schufa', 'ui-menu', now(), now());
insert into shared."right" values (2506 ,'','Menu-Item-Search', 'menu_item_search', 'ui-menu', now(), now());
insert into shared."right" values (2507 ,'','Menu-Item-Messenger', 'menu_item_messenger', 'ui-menu', now(), now());
insert into shared."right" values (3500 ,'','Show-Personal-Data', 'show_personal_data', 'personal-data', now(), now());


insert into landlord."right" values (2500, 2500, now());
insert into landlord."right" values (2501, 2501, now());
insert into landlord."right" values (2502, 2502, now());
insert into landlord."right" values (2503, 2503, now());
insert into landlord."right" values (2504, 2504, now());
insert into landlord."right" values (2505, 2505, now());
insert into landlord."right" values (2506, 2506, now());
insert into landlord."right" values (2507, 2507, now());
insert into landlord."right" values (3500, 3500, now());

insert into landlord.usertype_rights values (1003, 'COMPANYADMIN', 2500, now());
insert into landlord.usertype_rights values (1004, 'EMPLOYEE', 2500, now());

insert into landlord.usertype_rights values (1005, 'COMPANYADMIN', 2501, now());
insert into landlord.usertype_rights values (1006, 'EMPLOYEE', 2501, now());

insert into landlord.usertype_rights values (1007, 'COMPANYADMIN', 2502, now());
insert into landlord.usertype_rights values (1008, 'EMPLOYEE', 2502, now());
insert into landlord.usertype_rights values (1009, 'PROPERTYMANAGER', 2502, now());

insert into landlord.usertype_rights values (1010, 'COMPANYADMIN', 2503, now());
insert into landlord.usertype_rights values (1011, 'EMPLOYEE', 2503, now());

insert into landlord.usertype_rights values (1012, 'COMPANYADMIN', 2504, now());
insert into landlord.usertype_rights values (1013, 'EMPLOYEE', 2504, now());

insert into landlord.usertype_rights values (1014, 'COMPANYADMIN', 2505, now());
insert into landlord.usertype_rights values (1015, 'EMPLOYEE', 2505, now());

insert into landlord.usertype_rights values (1016, 'COMPANYADMIN', 2506, now());
insert into landlord.usertype_rights values (1017, 'EMPLOYEE', 2506, now());

insert into landlord.usertype_rights values (1018, 'COMPANYADMIN', 2507, now());
insert into landlord.usertype_rights values (1019, 'EMPLOYEE', 2507, now());

insert into landlord.usertype_rights values (1020, 'COMPANYADMIN', 3500, now());
insert into landlord.usertype_rights values (1021, 'EMPLOYEE', 3500, now());