package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

// TODO: handle nulls!
case class Site(id: Long, tag: String, usr: String, pwd: String, home: String,
  stype: String, dsc: String)

object Site {

  // TODO: handle nulls!
  val site = {
    get[Long]("id") ~
      get[String]("tag") ~
      get[String]("username") ~
      get[String]("password") ~
      get[String]("home") ~
      get[String]("type") ~
      get[String]("dsc") map {
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
    }

  def delete(id: Long) = DB.withConnection { implicit c =>
    SQL("DELETE FROM Sites WHERE id = {id}").on('id -> id).executeUpdate()
  }

  def getSite(id: Long) = DB.withConnection { implicit c =>
    SQL("SELECT * FROM Sites WHERE id = {id}").on('id -> id).as(site.singleOpt)
  }

  //  def update() // TODO: Site.update

}