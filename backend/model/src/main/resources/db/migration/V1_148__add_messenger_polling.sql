alter table shared.conversation add column customer_id BIGINT NOT NULL ;
    alter table shared.conversation add  constraint fk_conversation_2 foreign key (customer_id) REFERENCES landlord.customer MATCH SIMPLE
        ON UPDATE CASCADE on delete cascade;
create index conversation_message_read on shared.conversation_message (conversation_id, sender, read);
create index conversation_message_created on shared.conversation_message (conversation_id, created);
create index fki_conversation_2 on shared.conversation (customer_id);