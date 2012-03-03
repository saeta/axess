# AuthData Schema

# --- !Ups

CREATE TABLE Sites (
  id SERIAL NOT NULL PRIMARY KEY, -- pk
  username VARCHAR(100) NOT NULL, -- login info
  passwd VARCHAR(100) NOT NULL,
  home VARCHAR(100) NOT NULL, -- home page url
  type VARCHAR(1000) NOT NULL, -- Class name for site
  dsc VARCHAR(1000) -- description
);

# --- !Downs

DROP TABLE Sites;

