alter table shared.note_comment
   add property_id bigint;

alter table shared.note_comment
   add foreign key (property_id)
   references landlord.property(id)
       on update cascade
       on delete set null;