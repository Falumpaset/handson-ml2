ALTER table propertysearcher.custom_question_response SET SCHEMA shared;
alter table shared.application alter column custom_question_score set default null;
update shared.application set custom_question_score = null where custom_question_score = 0;