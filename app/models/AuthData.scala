package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class AuthData(username: String, password: String)

object AuthData {
  val authData = {
    get[String]("username") ~
      get[String]("passwd") map {
        case username ~ password => AuthData(username, password)
      }
  }

  def all() = DB.withConnection { implicit c =>
    SQL("SELECT * FROM AuthData").as(authData *)
  }

  def auth(username: String) = DB.withConnection { implicit c =>
    SQL("SELECT * FROM AuthData WHERE username = {username}").on(
      'username -> username).as(authData.singleOpt)
  }

  def create(username: String, password: String) = DB.withConnection { implicit c =>
    SQL("INSERT INTO AuthData (username, passwd) values ({username}, {password})").on(
      'username -> username,
      'password -> password).executeUpdate()
  }
}