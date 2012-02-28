package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

/**
 * TODO: include description field, and perhaps an ID
 */
case class Credentials(username: String, password: String)

object Credentials {
  val authData = {
    get[String]("username") ~
      get[String]("passwd") map {
        case username ~ password => Credentials(username, password)
      }
  }

  def all() = DB.withConnection { implicit c =>
    SQL("SELECT * FROM AuthData").as(authData *)
  }

  def auth(username: String) = DB.withConnection { implicit c =>
    SQL("SELECT * FROM AuthData WHERE username = {username}").on(
      'username -> username).as(authData.singleOpt)
  }

  /**
   * Make sure username is unique?
   */
  def create(username: String, password: String) = DB.withConnection { implicit c =>
    SQL("INSERT INTO AuthData (username, passwd) values ({username}, {password})").on(
      'username -> username,
      'password -> password).executeUpdate()
    true
  }
}