alter table shared.digital_contract add column agent_info jsonb;
create type shared.digital_contract_create_method as enum ('APP', 'API');

update shared.digital_contract set agent_info = jsonb_build_object('id', digital_contract.user_id);
alter table shared.digital_contract drop column user_id;

alter table shared.digital_contract alter column document_files drop not null ;
alter table shared.digital_contract add column create_method shared.digital_contract_create_method;

create unique index uq_customer_external_id on shared.digital_contract (customer_id, external_id) where external_id is not null;


update landlord.price set fixedpart = 119 where id = 300001;
update landlord.price set fixedpart = 550 where id = 300002;
update landlord.price set fixedpart = 1010 where id = 300003;
update landlord.price set fixedpart = 2350 where id = 300004;
update landlord.price set fixedpart = 4350 where id = 300005;
update landlord.price set fixedpart = 8100 where id = 300006;

insert into landlord.price (id, fixedpart, variablepart, currency)
values (300008, 20700, 0, 'EUR');
insert into landlord.quota_package (id, type, price_id, quantity, created, updated)
values (300008, 'DIGITAL_CONTRACT', 300008, 3000, now(), now());
