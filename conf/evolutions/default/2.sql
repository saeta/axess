# Scan and ScanMsg Schema

# --- !Ups
CREATE TABLE Scan (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- pk
  siteId INT NOT NULL, -- id in Sites table (consider index)
  start_time TIMESTAMP NULL DEFAULT NULL,
  end_time TIMESTAMP NULL DEFAULT NULL,
  error BOOLEAN NOT NULL DEFAULT FALSE,
  errorMsg TEXT,
  pagesFound INT NOT NULL DEFAULT 0,
  pagesScanned INT NOT NULL DEFAULT 0
);

CREATE TABLE ScanMsg (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- pk
  scanId INT NOT NULL, -- id in the Scan table above
  tag VARCHAR(400) NOT NULL,
  title VARCHAR(1000) NOT NULL,
  url VARCHAR(500) NOT NULL,
  msg VARCHAR(1000) NOT NULL
);

-- Debugging info for determining which pages are scanned.
CREATE TABLE ScanPage (
  scanId INT NOT NULL, -- id in Scan table above
  url VARCHAR(1000) NOT NULL
);


# --- !Downs
DROP TABLE Scan;
DROP TABLE ScanMsg;
