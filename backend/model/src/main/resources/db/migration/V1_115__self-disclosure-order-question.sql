delete from "shared"."self_disclosure_question" where title='SELF_DISCLOSURE_FIND_OUT_ABOUT_US_L';
delete from "shared"."self_disclosure_question" where title='SELF_DISCLOSURE_HAS_WBS_L';
delete from "shared"."self_disclosure_question" where title='SELF_DISCLOSURE_FLAT_VISITED_L';
delete from "shared"."self_disclosure_question" where title='SELF_DISCLOSURE_HAS_PETS_L';

alter table shared.self_disclosure_question
    add "order_number" int default 1;

UPDATE "shared"."self_disclosure_question" SET "order_number" = 1 WHERE "id" = 1005;
UPDATE "shared"."self_disclosure_question" SET "order_number" = 2 WHERE "id" = 1004;
UPDATE "shared"."self_disclosure_question" SET "order_number" = 3 WHERE "id" = 1006;
UPDATE "shared"."self_disclosure_question" SET "order_number" = 4 WHERE "id" = 1007;
