# AuthData Schema

# --- !Ups

CREATE TABLE Sites (
  id MEDIUMINT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- pk
  tag VARCHAR(50) NOT NULL, -- short human-readable tag
  username VARCHAR(100) NOT NULL, -- login info
  password VARCHAR(100) NOT NULL,
  home VARCHAR(100) NOT NULL, -- home page url
  type VARCHAR(1000), -- Class name for site
  dsc VARCHAR(1000) -- description
);

# --- !Downs

DROP TABLE Sites;

