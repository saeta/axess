// Copyright 2012 Brennan Saeta
//
// This file is part of Axess
//
// Axess is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Axess is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with Axess.  If not, see <http://www.gnu.org/licenses/>.

package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class DashboardEntry(tag: String, siteId: Long, numErrors: Long,
  scanId: Long)

object DashboardEntry {
  val entry = {
    str("tag") ~
      long("siteId") ~
      long("cnt") ~
      long("id") map {
        case tag ~ siteId ~ cnt ~ scanId =>
          DashboardEntry(tag, siteId, cnt, scanId)
      }
  }

  def get() = DB.withConnection { implicit c =>
    SQL("""
        SELECT Sites.tag, siteId, cnt, Scan.id
          FROM (SELECT scanId, COUNT(DISTINCT url) as cnt 
                  FROM ScanMsg GROUP BY scanId) as s, 
               Scan, 
               Sites
         WHERE scanId = Scan.id 
           AND Scan.id IN (SELECT max(id) FROM Scan GROUP BY siteId)
           AND siteId = Sites.id""").as(entry *)
  }
}
