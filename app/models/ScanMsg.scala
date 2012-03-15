package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import play.Play

case class ScanMsg(id: Long, scanId: Long, tag: String, title: String,
  url: String, msg: String)

case class ScanMsgGrouped(id: Long, scanId: Long, tag: String, title: String,
  url: String, msg: String, numPages: Long)

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
  val scanMsgG = {
    long("id") ~
      long("scanId") ~
      str("tag") ~
      str("title") ~
      str("url") ~
      str("msg") ~
      long("cnt") map {
        case id ~ scanId ~ tag ~ title ~ url ~ msg ~ cnt =>
          ScanMsgGrouped(id, scanId, tag, title, url, msg, cnt)
      }
  }

  def all = DB.withConnection { implicit c =>
    SQL("SELECT * FROM ScanMsg ORDER BY id").as(scanMsg *)
  }

  def all(scanId: Long) = DB.withConnection { implicit c =>
    SQL("SELECT * FROM ScanMsg WHERE scanId = {scanId} ORDER BY id").on(
      'scanId -> scanId).as(scanMsg *)
  }

  def numIssues(scanId: Long) = DB.withConnection { implicit c =>
    SQL("SELECT count(*) FROM ScanMsg WHERE scanId = {sid}").on(
      'sid -> scanId).as(scalar[Long].single)
  }

  def numPagesWithIssues(scanId: Long) = DB.withConnection(implicit c =>
    SQL("SELECT count(DISTINCT url) FROM ScanMsg WHERE scanID = {sid}").on(
      'sid -> scanId).as(scalar[Long].single))

  def allGrouped(scanId: Long) = DB.withConnection { implicit c =>
    SQL("SELECT *, count(*) as cnt FROM ScanMsg WHERE scanId = {sid} GROUP BY msg").on(
      'sid -> scanId).as(scanMsgG *)
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

  def foundPage(scanId: Long, url: String) = if (!Play.isProd()) {
    DB.withConnection { implicit c =>
      SQL("INSERT INTO ScanPage (scanId, url) VALUES ({sid}, {url})").on(
        'sid -> scanId,
        'url -> url).executeUpdate() == 1
    }
  } else {
    1
  }
}
