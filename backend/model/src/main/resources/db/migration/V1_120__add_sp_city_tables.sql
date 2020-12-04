drop table if exists constants.zipcode;
drop table if exists constants.region;
drop table if exists constants.country;
CREATE TABLE constants.state
(
    id   BIGINT       NOT NULL,
    name varchar(200) NOT NULL,
    constraint pk_state_id primary key (id)
);

CREATE TABLE constants.city
(
    id       BIGINT       NOT NULL,
    state_id BIGINT       NOT NULL,
    name     varchar(200) NOT NULL,
    constraint pk_city_id primary key (id),
    constraint fk_city_01 foreign key (state_id) references constants.state (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

create index fki_city_01 on constants.city (state_id);
create index idx_city_name on constants.city (name);

CREATE TABLE constants.district
(
    id      BIGINT       NOT NULL,
    city_id BIGINT       NOT NULL,
    name    varchar(200) NOT NULL,
    latitude     float,
    longitude     float,
    constraint pk_district_id primary key (id),
    constraint fk_district_01 foreign key (city_id) references constants.city (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

create index fki_district_01 on constants.district (city_id);

CREATE TABLE constants.zipcode
(
    id      bigint      NOT NULL,
    city_id bigint      NOT NULL,
    zipcode varchar(10) NOT NULL,
    constraint pk_zipcode_id primary key (id),
    constraint fk_zipcode_01 foreign key (city_id) references constants.city (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

create table constants.district_zipcode
(
    district_id bigint not null,
    zipcode_id  bigint not null,

    constraint fk_district_zipcode_01 foreign key (district_id) references constants.district (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    constraint fk_district_zipcode_02 foreign key (zipcode_id) references constants.zipcode (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

create index fki_district_zipcode_1 on constants.district_zipcode (zipcode_id);
create index fki_district_zipcode_2 on constants.district_zipcode (district_id);

create table propertysearcher.searchprofile_district
(
    district_id      bigint not null,
    searchprofile_id bigint not null,
    constraint fk_searchprofile_district_1 foreign key (district_id) references constants.district (id) match simple
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    constraint fk_searchprofile_district_2 foreign key (searchprofile_id) references propertysearcher.searchprofile (id) match simple
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

create type propertysearcher.searchprofiletype as enum ('RADIUS', 'DISTRICT');

alter table propertysearcher.searchprofile
    add column type propertysearcher.searchprofiletype default 'RADIUS'::propertysearcher.searchprofiletype not null ;

create index fki_zipcode_01 on constants.zipcode (city_id);
create index index_zipcode_zipcode on constants.zipcode (zipcode);
create index fki_searchprofile_district_1 on propertysearcher.searchprofile_district (district_id);
create index fki_searchprofile_district_2 on propertysearcher.searchprofile_district (searchprofile_id);
create index idx_zip on landlord.property ((data -> 'address' ->> 'zipCode'));
