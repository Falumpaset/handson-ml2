alter table shared.digital_contract rename column signed_document_file to signed_document_combined_file;

alter table shared.digital_contract add column signed_document_single_files jsonb;

alter table shared.digital_contract add column signed_document_archive_file jsonb;
