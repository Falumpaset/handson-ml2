alter table shared.application
    rename column custom_question_score to cqscore_tmp;

alter table shared.application
    add column custom_question_score jsonb;

update shared.application
set custom_question_score = jsonb_build_object('scoreIncludingRange', application.cqscore_tmp, 'scoreExcludingRange',
                                               application.cqscore_tmp);

alter table shared.application
    drop column cqscore_tmp;