alter table propertysearcher."user" add column "rented_applications_fetched" timestamp;

create index if not exists conversation_message_read_agent
    on shared.conversation_message (conversation_id, sender, read, (agent_info ->> 'id'));
