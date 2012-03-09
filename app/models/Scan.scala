package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Scan(id: Long, siteId: Long, started: Boolean, finished: Boolean,
  error: Boolean, errorMsg: Option[String])

object Scan {
  val scan = {
    long("id") ~
      long("siteId") ~
      bool("started") ~
      bool("finished") ~
      bool("error") ~
      get[Option[String]]("errorMsg") map {
        case id ~ siteId ~ started ~ finished ~ error ~ errorMsg =>
          Scan(id, siteId, started, finished, error, errorMsg)
      }
  }

  def all() = DB.withConnection { implicit c =>
    SQL("SELECT * FROM Scan ORDER BY id").as(scan *)
  }

  def newScan(siteId: Long) = DB.withConnection { implicit c =>
    SQL("""INSERT INTO Scan (siteId) VALUES ({siteId})""").on(
      'siteId -> siteId).executeUpdate()
    SQL("SELECT max(id) AS id FROM Scan WHERE siteId={siteId}").on(
      'siteId -> siteId).as(scalar[Long].single)
  }

  def getScan(id: Long) = DB.withConnection { implicit c =>
    SQL("SELECT * FROM Scan WHERE id = {id}").on('id -> id).as(scan.singleOpt)
  }

  def start(id: Long) = DB.withConnection { implicit c =>
    SQL("UPDATE Scan SET started=true WHERE id={id}").on('id -> id).executeUpdate() == 1
  }

  def finish(id: Long) = DB.withConnection { implicit c =>
    SQL("UPDATE Scan SET finished=true WHERE id={id}").on('id -> id).executeUpdate() == 1
  }

  def error(id: Long, msg: String) = DB.withConnection { implicit c =>
    SQL("UPDATE Scan SET error=true, errorMsg={msg} WHERE id = {id}").on(
      'id -> id,
      'msg -> msg).executeUpdate() == 1
  }
}
