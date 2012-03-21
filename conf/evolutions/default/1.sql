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

# AuthData Schema

# --- !Ups

CREATE TABLE Sites (
  `id` MEDIUMINT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- pk
  `tag` VARCHAR(50) NOT NULL, -- short human-readable tag
  `username` VARCHAR(100) NOT NULL, -- login info
  `password` VARCHAR(100) NOT NULL,
  `home` VARCHAR(100) NOT NULL, -- home page url
  `type` VARCHAR(1000) NOT NULL, -- Class name for site
  `dsc` VARCHAR(1000) NOT NULL DEFAULT ''-- description
);

# --- !Downs

DROP TABLE Sites;

