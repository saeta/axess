# Copyright 2012 Brennan Saeta
#
# This file is part of Axess
#
# Axess is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# Axess is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with Axess.  If not, see <http://www.gnu.org/licenses/>.

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
