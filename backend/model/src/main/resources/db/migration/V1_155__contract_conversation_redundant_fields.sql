alter table shared.digital_contract
    add column current_state shared.digital_contract_history_state;

update shared.digital_contract dc
set current_state = (select h.state
                     from shared.digital_contract_history h
                     where h.digital_contract_id = dc.id
                       and h.created =
                           (SELECT max(h2.created)
                            from shared.digital_contract_history h2
                            where h2.digital_contract_id = dc.id)
                     LIMIT 1);


alter table shared.digital_contract_signer
    add column current_state shared.digital_contract_signer_history_state;

update shared.digital_contract_signer dcs
set current_state = (select h.state
                     from shared.digital_contract_signer_history h
                     where h.signer_id = dcs.id
                       and h.created =
                           (SELECT max(h2.created)
                            from shared.digital_contract_signer_history h2
                            where h2.signer_id = dcs.id)
                     LIMIT 1);

alter table shared.conversation
    add column last_message_date timestamp;
alter table shared.conversation
    add column last_message_text text;
alter table shared.conversation
    add column last_message_sender shared.message_sender;

update shared.conversation c
set last_message_date = (select cm.created
                         from shared.conversation_message cm
                         where cm.conversation_id = c.id
                           and cm.created =
                               (SELECT max(cm2.created)
                                from shared.conversation_message cm2
                                where cm2.conversation_id = c.id)
                         LIMIT 1);


update shared.conversation c
set last_message_sender = (select cm.sender
                           from shared.conversation_message cm
                           where cm.conversation_id = c.id
                             and cm.created =
                                 (SELECT max(cm2.created)
                                  from shared.conversation_message cm2
                                  where cm2.conversation_id = c.id)
                           LIMIT 1);

update shared.conversation c
set last_message_text = (select cm.message
                         from shared.conversation_message cm
                         where cm.conversation_id = c.id
                           and cm.created =
                               (SELECT max(cm2.created)
                                from shared.conversation_message cm2
                                where cm2.conversation_id = c.id)
                         LIMIT 1);

alter table shared.digital_contract add column docusign_account_id varchar(255);