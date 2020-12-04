alter table shared.digital_contract_signer add column docusign_additional_document_ids jsonb;

update shared.digital_contract_signer set docusign_additional_document_ids =
    jsonb_build_object(
        'viewingDateCombinedDocId', digital_contract_signer.docusign_additional_document_id,
        'identityConfirmationDocId', null
    );

alter table shared.digital_contract_signer drop column docusign_additional_document_id;
