# AuthData Schema

# --- !Ups

CREATE TABLE Creds (
  id SERIAL NOT NULL PRIMARY KEY,
  username VARCHAR(100) NOT NULL,
  passwd VARCHAR(100) NOT NULL,
  dsc VARCHAR(1000)
);

# --- !Downs

DROP TABLE Creds;
