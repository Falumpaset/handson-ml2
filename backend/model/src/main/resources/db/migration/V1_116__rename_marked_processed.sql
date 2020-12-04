alter table shared.application rename column marked to processed;
create index fki_fk_application_7 on shared.application (property_id, status, processed)