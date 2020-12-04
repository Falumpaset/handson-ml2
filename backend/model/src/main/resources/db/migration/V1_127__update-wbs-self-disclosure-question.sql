delete from "shared"."self_disclosure_question" where title='SELF_DISCLOSURE_DOCUMENT_WBS';

INSERT INTO "shared"."self_disclosure_question" ("id", "title", "mandatory", "hidden", "self_disclosure_id", "type",
                                                 "desired_responses", "comment_allowed", "comment_hint", "created",
                                                 "updated", "sub_questions", "order_number", "upload_allowed", "upload_hint") VALUES (
       1008,
       'SELF_DISCLOSURE_DOCUMENT_WBS_L',
       false,
       false,
       1000,
       'BOOLEAN',
       '{}',
       false,
       null,
       NOW(),
       NOW(),
       null,
       5,
       true,
       'SELF_DISCLOSURE_DOCUMENT_WBS_HINT_L'
    );


INSERT INTO "shared"."self_disclosure_question" ("id", "title", "mandatory", "hidden", "self_disclosure_id", "type",
                                                 "desired_responses", "comment_allowed", "comment_hint", "created",
                                                 "updated", "sub_questions", "order_number", "upload_allowed", "upload_hint")
SELECT nextval('dictionary_seq'),
       'SELF_DISCLOSURE_DOCUMENT_WBS_L',
       false,
       false,
       id,
       'BOOLEAN',
       '{}',
       false,
       null,
       NOW(),
       NOW(),
       null,
       5,
       true,
       'SELF_DISCLOSURE_DOCUMENT_WBS_HINT_L'
FROM shared.self_disclosure
WHERE customer_id IS NOT NULL;

update "shared"."self_disclosure_question" set sub_questions='[
  {
    "title": "SELF_DISCLOSURE_CURRENT_ADDRESS_L",
    "type": "ADDRESS",
    "mandatory": false,
    "hidden": false,
    "commentAllowed": false
  },
  {
    "title": "SELF_DISCLOSURE_EMPLOYMENT_L",
    "type": "EMPLOYMENT",
    "mandatory": false,
    "hidden": false,
    "commentAllowed": false
  },
  {
    "title": "SELF_DISCLOSURE_PAYMENT_OBLIGATIONS_L",
    "type": "BOOLEAN",
    "mandatory": true,
    "hidden": false,
    "commentAllowed": true,
    "commentHint": "SELF_DISCLOSURE_PAYMENT_OBLIGATIONS_COMMENT_HINT_L"
  },
  {
    "title": "SELF_DISCLOSURE_LIST_OF_DEBTORS_L",
    "type": "BOOLEAN",
    "mandatory": true,
    "hidden": false,
    "commentAllowed": false
  },
  {
    "title": "SELF_DISCLOSURE_PAST_EVICTIONS_L",
    "type": "BOOLEAN",
    "mandatory": true,
    "hidden": false,
    "commentAllowed": false
  },
  {
    "title": "SELF_DISCLOSURE_PRIVATE_BANKRUPTCY_L",
    "type": "BOOLEAN",
    "mandatory": true,
    "hidden": false,
    "commentAllowed": false
  },
  {
    "type": "DOCUMENT",
    "title": "SELF_DISCLOSURE_DOCUMENT_INCOME",
    "hidden": false,
    "mandatory": false
  },
  {
    "type": "DOCUMENT",
    "title": "SELF_DISCLOSURE_DOCUMENT_CREDIT_RATING",
    "hidden": false,
    "mandatory": false
  }
]'
where title='SELF_DISCLOSURE_PERSONAL_INFO_L';

update "shared"."self_disclosure_question" set sub_questions='[
  {
    "title": "SELF_DISCLOSURE_CURRENT_ADDRESS_L",
    "type": "ADDRESS",
    "mandatory": false,
    "hidden": false,
    "commentAllowed": false
  },
  {
    "title": "SELF_DISCLOSURE_EMPLOYMENT_L",
    "type": "EMPLOYMENT",
    "mandatory": false,
    "hidden": false,
    "commentAllowed": false
  },
  {
    "title": "SELF_DISCLOSURE_PAYMENT_OBLIGATIONS_L",
    "type": "BOOLEAN",
    "mandatory": true,
    "hidden": false,
    "commentAllowed": true,
    "commentHint": "SELF_DISCLOSURE_PAYMENT_OBLIGATIONS_COMMENT_HINT_L"
  },
  {
    "title": "SELF_DISCLOSURE_LIST_OF_DEBTORS_L",
    "type": "BOOLEAN",
    "mandatory": true,
    "hidden": false,
    "commentAllowed": false
  },
  {
    "title": "SELF_DISCLOSURE_PAST_EVICTIONS_L",
    "type": "BOOLEAN",
    "mandatory": true,
    "hidden": false,
    "commentAllowed": false
  },
  {
    "title": "SELF_DISCLOSURE_PRIVATE_BANKRUPTCY_L",
    "type": "BOOLEAN",
    "mandatory": true,
    "hidden": false,
    "commentAllowed": false
  },
  {
    "type": "DOCUMENT",
    "title": "SELF_DISCLOSURE_DOCUMENT_INCOME",
    "hidden": false,
    "mandatory": false
  },
  {
    "type": "DOCUMENT",
    "title": "SELF_DISCLOSURE_DOCUMENT_CREDIT_RATING",
    "hidden": false,
    "mandatory": false
  }
]'
    where title='SELF_DISCLOSURE_PERSONAL_INFO_TENANT_L';