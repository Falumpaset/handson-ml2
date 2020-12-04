alter table shared.propertytenant
    alter column user_id drop not null,
    drop constraint fk_propertytenant_02,
    add constraint fk_propertytenant_02 foreign key (user_id) references propertysearcher."user"
            on update cascade on delete set null ;
