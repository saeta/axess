# Scan and ScanMsg Schema

# --- !Ups
CREATE TABLE Scan (
  id MEDIUMINT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- pk
  siteId INT NOT NULL, -- id in Sites table (consider index)
  started BOOLEAN NOT NULL DEFAULT FALSE,
  finished BOOLEAN NOT NULL DEFAULT FALSE,
  error BOOLEAN NOT NULL DEFAULT FALSE,
  errorMsg TEXT
);

CREATE TABLE ScanMsg (
  id MEDIUMINT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- pk
  scanId INT NOT NULL, -- id in the Scan table above
  tag VARCHAR(400) NOT NULL,
  url VARCHAR(500) NOT NULL,
  msg VARCHAR(1000) NOT NULL
);


# --- !Downs
DROP TABLE Scan;
DROP TABLE ScanMsg;
