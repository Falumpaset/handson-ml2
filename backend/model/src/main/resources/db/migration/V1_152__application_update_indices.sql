
create index if not exists idx_property_cq_score
    on shared.application (property_id, custom_question_score);
