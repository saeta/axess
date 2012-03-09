package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class ScanMsg(id: Long, scanId: Long, tag: String, url: String, msg: String)

object ScanMsg {
  val scanMsg = {
    long("id") ~ long("scanId") ~ str("tag") ~ str("url") ~ str("msg") map {
      case id ~ scanId ~ tag ~ url ~ msg => ScanMsg(id, scanId, tag, url, msg)
    }
  }

  def all = DB.withConnection { implicit c =>
    SQL("SELECT * FROM ScanMsg ORDER BY id").as(scanMsg *)
  }

  def all(scanId: Long) = DB.withConnection { implicit c =>
    SQL("SELECT * FROM ScanMsg WHERE scanId = {scanId} ORDER BY id").on(
      'scanId -> scanId).as(scanMsg *)
  }

  def addMsg(scanId: Long, tag: String, url: String, msg: String) =
    DB.withConnection { implicit c =>
      SQL("INSERT INTO ScanMsg (scanId, tag, url, msg) VALUES ({scanId}, {tag}, {url}, {msg})").on(
        'scanId -> scanId,
        'tag -> tag,
        'url -> url,
        'msg -> msg).executeUpdate() == 1
    }
}
