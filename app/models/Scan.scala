package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.Date

case class Scan(id: Long, siteId: Long, startTime: Option[Date],
  endTime: Option[Date], error: Boolean, errorMsg: Option[String],
  pagesFound: Long, pagesScanned: Long) {
  def finished = endTime != None
  def started = startTime != None
}

case class ScanSummary(scanId: Long, siteId: Long, start_time: Option[Date],
  tag: String, numMsgs: Long)

object Scan {
  val scan = {
    long("id") ~
      long("siteId") ~
      get[Option[Date]]("start_time") ~
      get[Option[Date]]("end_time") ~
      bool("error") ~
      get[Option[String]]("errorMsg") ~
      long("pagesFound") ~
      long("pagesScanned") map {
        case id ~ siteId ~ start_time ~ end_time ~ error ~
          errorMsg ~ pagesFound ~ pagesScanned =>
          Scan(id, siteId, start_time, end_time, error, errorMsg, pagesFound,
            pagesScanned)
      }
  }

  val scanSummary = {
    long("Scan.id") ~
      long("Scan.siteId") ~
      get[Option[Date]]("Scan.start_time") ~
      str("Sites.tag") ~
      long("numMsgs") map {
        case id ~ siteId ~ start_time ~ tag ~ numMsgs =>
          ScanSummary(id, siteId, start_time, tag, numMsgs)
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
    SQL("UPDATE Scan SET start_time=NOW() WHERE id={id}").on('id -> id).executeUpdate() == 1
  }

  def finish(id: Long) = DB.withConnection { implicit c =>
    SQL("UPDATE Scan SET end_time=NOW() WHERE id={id}").on('id -> id).executeUpdate() == 1
  }

  def error(id: Long, msg: String) = DB.withConnection { implicit c =>
    SQL("UPDATE Scan SET end_time=NOW(), error=true, errorMsg={msg} WHERE id = {id}").on(
      'id -> id,
      'msg -> msg).executeUpdate() == 1
  }

  def updateScanStats(id: Long, pagesFound: Long, pagesScanned: Long) = DB.withConnection {
    implicit c =>
      SQL("""
          UPDATE Scan 
             SET pagesFound={pagesFound}, pagesScanned={pagesScanned} 
           WHERE id={id}""").on(
        'pagesFound -> pagesFound,
        'pagesScanned -> pagesScanned,
        'id -> id).executeUpdate() == 1
  }

  def errorAllUnterminatedScans() = DB.withConnection { implicit c =>
    SQL("""
        UPDATE Scan 
           SET error=true, 
               errorMsg='Terminated on boot' 
         WHERE end_time IS NULL AND error=false""").executeUpdate()
  }

  def scanSummaries() = DB.withConnection { implicit c =>
    SQL("""
        SELECT s.id, siteId, start_time, si.tag,
               (SELECT count(*) FROM ScanMsg WHERE scanId = s.id) as numMsgs 
          FROM Scan s, Sites si 
         WHERE si.id = s.siteId 
           AND s.id IN (SELECT max(id) FROM Scan GROUP BY siteId) 
      ORDER BY siteId
        """).as(scanSummary *)
  }
}
