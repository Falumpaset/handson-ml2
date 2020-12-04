ALTER TABLE shared."application"
    ADD COLUMN custom_question_score double precision default 0;

CREATE INDEX IF NOT EXISTS idx_property_cq_score ON shared."application" (property_id, custom_question_score);

ALTER TABLE shared.property_custom_question  SET SCHEMA landlord;
ALTER TABLE shared.custom_question_response  SET SCHEMA propertysearcher;

ALTER TABLE landlord.property_custom_question add CONSTRAINT property_custom_question_pkey PRIMARY KEY (id);
