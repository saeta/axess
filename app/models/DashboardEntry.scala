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
