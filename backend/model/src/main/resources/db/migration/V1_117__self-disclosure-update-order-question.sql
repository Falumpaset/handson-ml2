UPDATE "shared"."self_disclosure_question" SET "order_number" = 1 WHERE title = 'SELF_DISCLOSURE_PERSONAL_INFO_L';
UPDATE "shared"."self_disclosure_question" SET "order_number" = 2 WHERE title = 'SELF_DISCLOSURE_PERSONAL_INFO_TENANT_L';
UPDATE "shared"."self_disclosure_question" SET "order_number" = 3 WHERE title = 'SELF_DISCLOSURE_CHILDREN_ADULT_L';
UPDATE "shared"."self_disclosure_question" SET "order_number" = 4 WHERE title = 'SELF_DISCLOSURE_CHILDREN_MINOR_L';


UPDATE "shared"."self_disclosure_question" SET "sub_questions" = '[
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
    "title": "SELF_DISCLOSURE_LIST_OF_DEBTORS_L",
    "type": "BOOLEAN",
    "mandatory": true,
    "hidden": false,
    "commentAllowed": true,
    "commentHint": "SELF_DISCLOSURE_LIST_OF_DEBTORS_COMMENT_HINT_L"
  }
]'
WHERE title = 'SELF_DISCLOSURE_PERSONAL_INFO_L';

UPDATE "shared"."self_disclosure_question" SET "sub_questions" = '[
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
    "title": "SELF_DISCLOSURE_LIST_OF_DEBTORS_L",
    "type": "BOOLEAN",
    "mandatory": true,
    "hidden": false,
    "commentAllowed": true,
    "commentHint": "SELF_DISCLOSURE_LIST_OF_DEBTORS_COMMENT_HINT_L"
  }
]'
WHERE title = 'SELF_DISCLOSURE_PERSONAL_INFO_TENANT_L';