package controllers

import play.api._
import play.api.mvc._
import play.api.libs.concurrent._
import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.pattern._
import akka.util.duration._
import akka.util.Timeout
import axess.Axess
import play.api.Play._
import play.api.data._
import play.api.data.Forms._
import models.Credentials

object CredentialsController extends Controller {

  val newAuthForm = Form(
    tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
      "description" -> text))

  def newAuth = Action { implicit request =>
    newAuthForm.bindFromRequest.fold(
      errors => {
        BadRequest(views.html.creds(Credentials.all(), errors))
      },
      a => {
        Credentials.create(a._1, a._2, a._3)
        Redirect(routes.CredentialsController.all)
      })
  }

  def all = Action {
    Ok(views.html.creds(Credentials.all(), newAuthForm))
  }

  def delete(id: Long) = Action {
    Credentials.delete(id)
    Redirect(routes.CredentialsController.all)
  }
}