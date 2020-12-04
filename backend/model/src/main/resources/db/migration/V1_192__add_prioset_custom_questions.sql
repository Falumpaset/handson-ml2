alter table landlord.property_custom_question
    drop constraint fk_property_custom_question_1;

update landlord.property_custom_question set property_id = (
    select p.prioset_id from landlord.property p where p.id = property_id
);

alter table landlord.property_custom_question
    rename property_id to prioset_id;

alter table landlord.property_custom_question
    add constraint fk_prioset_custom_question_prioset foreign key (prioset_id) references landlord.prioset (id) on update cascade on delete cascade;

alter table landlord.property_custom_question
    rename constraint fk_property_custom_question_2 to fk_prioset_custom_question_custom_question;

alter table landlord.property_custom_question
rename to prioset_custom_question;
