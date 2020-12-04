alter table shared.self_disclosure_sub_question add column constant_name varchar(50);
update shared.self_disclosure_sub_question set constant_name = 'genderTypes' where id = 1