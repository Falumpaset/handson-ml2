alter table shared.digital_contract
    add column document_files jsonb not null default '[]'::jsonb;
