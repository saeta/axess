package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

/**
 * TODO: include description field, and perhaps an ID
 */
case class Credentials(id: Long, username: String, password: String, desc: String)

object Credentials {
  val authData = {
    get[Long]("id") ~
      get[String]("username") ~
      get[String]("passwd") ~
      get[String]("dsc") map {
        case id ~ username ~ password ~ desc =>
          Credentials(id, username, password, desc)
      }
  }

  def all() = DB.withConnection { implicit c =>
    SQL("SELECT * FROM Creds ORDER BY id").as(authData *)
  }

  def auth(username: String) = DB.withConnection { implicit c =>
    SQL("SELECT * FROM Creds WHERE username = {username}").on(
      'username -> username).as(authData.singleOpt)
  }

  def getCred(id: Long) = DB.withConnection { implicit c =>
    SQL("SELECT * FROM Creds WHERE id = {id}").on(
      'id -> id).as(authData.singleOpt)
  }

  def create(username: String, password: String, desc: String = "") =
    DB.withConnection { implicit c =>
      SQL("""INSERT INTO Creds (username, passwd, dsc)
             VALUES ({username}, {password}, {desc})""").on(
        'username -> username,
        'password -> password,
        'desc -> desc).executeUpdate()
      true
    }

  def delete(id: Long) = DB.withConnection { implicit c =>
    SQL("DELETE FROM Creds WHERE id = {id}").on(
      'id -> id).executeUpdate()
  }
}