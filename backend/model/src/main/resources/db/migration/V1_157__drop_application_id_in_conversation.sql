update shared.conversation
set id = application_id;


alter table shared.conversation
    drop column application_id cascade;


alter table shared.conversation drop constraint if exists fk_conversation_1 ;
alter table shared.conversation
    add constraint  fk_conversation_1 foreign key (id) references shared.application (id) on update cascade on delete cascade;