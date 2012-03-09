package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

// TODO: handle nulls!
case class Site(id: Long, tag: String, usr: String, pwd: String, home: String,
  stype: Option[String], dsc: Option[String]) {
  def scannable = stype != None

  def reason = "Type of site undefined!"
}

object Site {

  val site = {
    long("id") ~
      str("tag") ~
      str("username") ~
      str("password") ~
      str("home") ~
      get[Option[String]]("type") ~
      get[Option[String]]("dsc") map {
        case id ~ tag ~ usr ~ pwd ~ home ~ stype ~ dsc =>
          Site(id, tag, usr, pwd, home, stype, dsc)
      }
  }

  def all() = DB.withConnection { implicit c =>
    SQL("""SELECT * FROM Sites ORDER BY id""").as(site *)
  }

  def create(tag: String, usr: String, pwd: String, home: String) =
    DB.withConnection { implicit c =>
      SQL("""INSERT INTO Sites (tag, username, password, home) 
           VALUES ({tag}, {usr}, {pwd}, {home})""").on(
        'usr -> usr,
        'pwd -> pwd,
        'home -> home,
        'tag -> tag).executeUpdate()
      SQL("SELECT id FROM Sites WHERE tag={tag}, username={usr}, password={pwd}, home={home}").on(
        'tag -> tag,
        'usr -> usr,
        'pwd -> pwd,
        'home -> home).as(scalar[Long].single)
    }

  def delete(id: Long) = DB.withConnection { implicit c =>
    SQL("DELETE FROM Sites WHERE id = {id}").on('id -> id).executeUpdate()
  }

  def getSite(id: Long) = DB.withConnection { implicit c =>
    SQL("SELECT * FROM Sites WHERE id = {id}").on('id -> id).as(site.singleOpt)
  }

  def update(s: Site) = DB.withConnection { implicit c =>
    SQL("""UPDATE Sites SET tag={tag}, username={usr}, password={pwd}, home={home}, type={typ}, dsc={dsc} WHERE id={id}""").on(
      'id -> s.id,
      'tag -> s.tag,
      'usr -> s.usr,
      'pwd -> s.pwd,
      'home -> s.home,
      'typ -> s.stype,
      'dsc -> s.dsc).executeUpdate()
  }

  def siteTypes() = DB.withConnection { implicit c =>
    SQL("SELECT UNIQUE type FROM Sites")().map(row => row[String]("type")).toList
  }

}