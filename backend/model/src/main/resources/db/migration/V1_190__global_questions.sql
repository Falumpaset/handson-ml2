create type shared.custom_question_type as enum ('GLOBAL','PROPERTY');

alter table shared.custom_question
    add column type shared.custom_question_type default 'PROPERTY';

alter table shared.custom_question add column importance bigint default 0;

alter table shared.propertyproposal
    add column custom_question_score jsonb default jsonb_build_object('scoreIncludingRange', null,
                                                                      'scoreExcludingRange',
                                                                      null);


update shared.propertyproposal set custom_question_score =  jsonb_build_object('scoreIncludingRange', null,
                                                                               'scoreExcludingRange',
                                                                               null);

update shared.propertyproposal
set custom_question_score = (SELECT a.custom_question_score
                             from shared.application a
                             where a.user_id = shared.propertyproposal.user_id
                               and a.property_id = shared.propertyproposal.property_id
                             LIMIT 1)
where ((SELECT count(a1.id)
       from shared.application a1
       where a1.property_id = propertyproposal.property_id
         and a1.user_id = propertyproposal.user_id) > 0)
