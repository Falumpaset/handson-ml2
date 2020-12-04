alter table shared.self_disclosure_question
    add upload_allowed boolean default false;

alter table shared.self_disclosure_question
    add upload_hint VARCHAR(2048);

INSERT INTO "shared"."self_disclosure_question" ("id", "title", "mandatory", "hidden", "self_disclosure_id", "type",
                                                 "desired_responses", "comment_allowed", "comment_hint", "created",
                                                 "updated", "sub_questions", "order_number", "upload_allowed", "upload_hint")
SELECT nextval('dictionary_seq'),
       'SELF_DISCLOSURE_DOCUMENT_WBS',
       false,
       false,
       id,
       'BOOLEAN',
       '{}',
       false,
       null,
       NOW(),
       NOW(),
       '[
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
       ]',
       5,
       true,
       'SELF_DISCLOSURE_DOCUMENT_WBS_HINT'
FROM shared.self_disclosure
WHERE customer_id IS NOT NULL;



