package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class ScanMsg(id: Long, scanId: Long, tag: String, title: String,
  url: String, msg: String)

object ScanMsg {
  val scanMsg = {
    long("id") ~
      long("scanId") ~
      str("tag") ~
      str("title") ~
      str("url") ~
      str("msg") map {
        case id ~ scanId ~ tag ~ title ~ url ~ msg =>
          ScanMsg(id, scanId, tag, title, url, msg)
      }
  }

  def all = DB.withConnection { implicit c =>
    SQL("SELECT * FROM ScanMsg ORDER BY id").as(scanMsg *)
  }

  def all(scanId: Long) = DB.withConnection { implicit c =>
    SQL("SELECT * FROM ScanMsg WHERE scanId = {scanId} ORDER BY id").on(
      'scanId -> scanId).as(scanMsg *)
  }

  def addMsg(scanId: Long, tag: String, title: String, url: String, msg: String) =
    DB.withConnection { implicit c =>
      SQL("INSERT INTO ScanMsg (scanId, tag, title, url, msg) VALUES ({scanId}, {tag}, {title}, {url}, {msg})").on(
        'scanId -> scanId,
        'tag -> tag,
        'title -> title,
        'url -> url,
        'msg -> msg).executeUpdate() == 1
    }
}
