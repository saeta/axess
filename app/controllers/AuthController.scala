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
import com.cautiousfireball.axess.Axess
import play.api.Play._
import play.api.data._
import play.api.data.Forms._
import models.AuthData

object AuthController extends Controller {

  // TODO: rework auth into a list of everything, with a form to add new on the
  // site, which posts to newauth method
  val newAuthForm = Form(
    tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText))

  def newAuth = Action { implicit request =>
    newAuthForm.bindFromRequest.fold(
      errors => {
        BadRequest(views.html.newauth(errors))
      },
      a => {
        AuthData.create(a._1, a._2)
        Redirect(routes.AuthController.all)
      })
  }

  def all = Action {
    Ok(views.html.newauth(newAuthForm))
  }

  //  def authAdd = TODO

}