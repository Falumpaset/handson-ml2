ALTER TABLE shared.note DROP comments;

ALTER TABLE shared.note
  ADD COLUMN rating double precision NOT NULL DEFAULT 0
  CHECK(rating BETWEEN 0 AND 5);

ALTER TABLE shared.note
  ADD COLUMN created timestamp without time zone NOT NULL;

ALTER TABLE shared.note
  ADD COLUMN updated timestamp without time zone NOT NULL;

CREATE TABLE shared.note_comment (
  id          bigint NOT NULL,
  user_id    bigint NOT NULL,
  note_id    bigint NOT NULL,
  "comment" varchar(512),
  created timestamp without time zone,
  updated timestamp without time zone,

  CONSTRAINT note_comment_pkey PRIMARY KEY (id),
  CONSTRAINT fk_note_comment_user FOREIGN KEY (user_id)
    REFERENCES landlord."user" (id) MATCH SIMPLE
      ON UPDATE CASCADE
      ON DELETE CASCADE,
  CONSTRAINT fk_note_comment_note FOREIGN KEY (note_id)
    REFERENCES shared.note (id) MATCH SIMPLE
      ON UPDATE CASCADE
      ON DELETE CASCADE
);
