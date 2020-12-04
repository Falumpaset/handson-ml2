alter table shared.application add column seen timestamp;
update shared.application set seen = now() where processed;
alter table shared.application drop column processed;