create type report_chart as enum ('PROPERTY_CREATED', 'PROPERTY_PUBLISHED', 'APPLICATIONS_TOTAL', 'APPLICATIONS_BY_PORTAL', 'APPLICATIONS_INTENTIONS', 'APPLICATIONS_APPOINTMENT_ACCEPTANCES', 'PROPOSAL_OFFERED', 'APPOINTMENT_OCCURENCES');

create table landlord.chart_setting
(
    id bigint NOT NULL,
    user_id bigint not null,
    chart report_chart,
    favourite boolean,
    CONSTRAINT pk_chart_setting PRIMARY KEY (id),
    CONSTRAINT fk_chart_setting_01 FOREIGN KEY (user_id)
        REFERENCES landlord.user (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

create index fki_user_chart_settings_01 on landlord.chart_setting (user_id);
create unique index ui_user_chart_settings_01 on landlord.chart_setting (user_id, chart);
