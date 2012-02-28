# AuthData Schema

# --- !Ups

CREATE TABLE AuthData (
  username VARCHAR(100) NOT NULL,
  passwd VARCHAR(100) NOT NULL
);

# --- !Downs

DROP TABLE AuthData;