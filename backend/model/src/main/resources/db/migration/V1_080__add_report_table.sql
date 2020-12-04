CREATE TYPE administration.reporttype AS ENUM ('LANDLORD', 'PROPERTYSEARCHER', 'TOTAL');
CREATE TYPE administration.timespantype AS ENUM ('MONTHLY', 'WEEKLY', 'CUSTOM');

CREATE TABLE administration.report
(
  id      bigint NOT NULL,
  type    administration.reporttype,
  timespan  administration.timespantype,
  report  text   NOT NULL,
  startdate timestamp without time zone,
  enddate timestamp without time zone,
  created timestamp without time zone,
  updated timestamp without time zone,
  CONSTRAINT pk_report PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_report_type ON administration.report(type);
