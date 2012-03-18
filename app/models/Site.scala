package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Site(id: Long, tag: String, usr: String, pwd: String, home: String,
  stype: String, dsc: String)
case class NewSite(tag: String, usr: String, pwd: String, home: String,
  typ: String, dsc: String)

object Site {

  val site = {
    long("id") ~
      str("tag") ~
      str("username") ~
      str("password") ~
      str("home") ~
      str("type") ~
      str("dsc") map {
        case id ~ tag ~ usr ~ pwd ~ home ~ stype ~ dsc =>
          Site(id, tag, usr, pwd, home, stype, dsc)
      }
  }

  def all() = DB.withConnection { implicit c =>
    SQL("""SELECT * FROM Sites ORDER BY id""").as(site *)
  }

  def create(ns: NewSite) =
    DB.withConnection { implicit c =>
      SQL("""INSERT INTO Sites (tag, username, password, home, type, dsc) 
           VALUES ({tag}, {usr}, {pwd}, {home}, {typ}, {dsc})""").on(
        'usr -> ns.usr,
        'pwd -> ns.pwd,
        'home -> ns.home,
        'tag -> ns.tag,
        'typ -> ns.typ,
        'dsc -> ns.dsc).executeUpdate()
      SQL("SELECT max(id) FROM Sites WHERE tag={tag} AND username={usr} AND password={pwd} AND home={home} AND type={typ}").on(
        'tag -> ns.tag,
        'usr -> ns.usr,
        'pwd -> ns.pwd,
        'home -> ns.home,
        'typ -> ns.typ).as(scalar[Long].single)
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
    SQL("SELECT DISTINCT type FROM Sites")().map(row => row[String]("type")).toList
  }

}