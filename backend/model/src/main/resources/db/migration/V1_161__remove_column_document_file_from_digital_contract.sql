alter table shared.digital_contract drop column document_file;

alter table shared.digital_contract_signer add column docusign_additional_document_id varchar(10);
